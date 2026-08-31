package com.example.prediction.engine

import android.content.Context
import com.example.prediction.FeatureFlags
import com.example.prediction.PredictionProvider
import com.example.prediction.banglish.BanglishLearningEngine
import com.example.prediction.banglish.ScriptIntentDetector
import com.example.prediction.builtin.BuiltInDictionaryRepository
import com.example.prediction.correction.AutoCorrectionEngine
import com.example.prediction.data.AppDatabase
import com.example.prediction.ngram.NgramIndex
import com.example.prediction.personal.LearningEngine
import com.example.prediction.personal.PersonalTrieIndex
import com.example.prediction.ranking.RankingEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Orchestrates Tier 1 access: builtin DAWG assets, Room-backed personal index,
 * n-gram tables, emoji map, ranking, correction, caching. Implements PredictionProvider
 * so the IME layer never sees internals. All indexing/load work runs on IO.
 */
class DictionaryManager(
    private val flags: FeatureFlags
) : PredictionProvider {

    private val builtin = BuiltInDictionaryRepository()
    private val personalTrie = PersonalTrieIndex()
    private val ngram = NgramIndex()
    private val ranking = RankingEngine()
    private val cache = CacheManager()
    private val detector = ScriptIntentDetector()

    // Cached flag values — updated by a single collector in ImePreferenceCollector.
    // Eliminates runBlocking { flow.first() } on every keystroke.
    @JvmField var cachedLearningThreshold: Int = 3
    @JvmField var cachedEmojiEnabled: Boolean = true
    @JvmField var cachedPersonalLearningEnabled: Boolean = true
    @JvmField var cachedIncognito: Boolean = false

    private val suggestionEngine = SuggestionEngine(builtin, personalTrie, ngram, ranking, {
        cachedLearningThreshold
    }) { prefix, isBangla, now ->
        autoCorrection.correct(prefix, isBangla, now, correctionEnabled = true)?.correction
    }
    private val banglishEngine = BanglishLearningEngine(personalTrie) {
        cachedLearningThreshold
    }
    private val autoCorrection = AutoCorrectionEngine(builtin, personalTrie) {
        cachedLearningThreshold
    }

    private var learningEngine: LearningEngine? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var database: AppDatabase? = null

    var englishLoadMs: Long = 0
        private set
    var banglaLoadMs: Long = 0
        private set

    override fun init(context: Context) {
        try {
            database = AppDatabase.getInstance(context.applicationContext)
            builtin.load(context.applicationContext)
            englishLoadMs = builtin.englishLoadMs
            banglaLoadMs = builtin.banglaLoadMs

            val db = database!!
            learningEngine = LearningEngine(
                learnedWordDao = db.learnedWordDao(),
                learnedPhraseDao = db.learnedPhraseDao(),
                personalWordDao = db.personalWordDao(),
                recentWordDao = db.recentWordDao(),
                personalTrie = personalTrie,
                learningEnabled = { cachedPersonalLearningEnabled },
                incognitoEnabled = { cachedIncognito }
            )
            scope.launch {
                try {
                    learningEngine?.loadExistingIntoMemory()
                    for (phrase in db.learnedPhraseDao().getTopPhrases(2000)) {
                        ngram.learn(phrase.firstWord, phrase.secondWord, phrase.thirdWord)
                    }
                } catch (e: Exception) {
                    android.util.Log.e("DictionaryManager", "Failed to load learning data", e)
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("DictionaryManager", "Failed to initialize prediction engine", e)
        }
    }

    override fun getSuggestions(
        prefix: String,
        isBangla: Boolean,
        previousWords: List<String>,
        showTypedWordFirst: Boolean,
        limit: Int,
        now: Long
    ): List<PredictionCandidate> {
        val cacheKey = "$prefix|$isBangla|${previousWords.lastOrNull()}|$showTypedWordFirst|$limit"
        cache.get(cacheKey)?.let { return it }
        val result = suggestionEngine.getSuggestions(
            prefix, isBangla, previousWords, showTypedWordFirst, limit, now, cachedEmojiEnabled
        )
        cache.put(cacheKey, result)
        return result
    }

    override fun getNextWordPredictions(
        previousWords: List<String>,
        isBangla: Boolean,
        limit: Int,
        now: Long
    ): List<PredictionCandidate> {
        val normalized = previousWords.map { it.lowercase() }.filter { it.isNotEmpty() }
        if (normalized.isEmpty()) return emptyList()
        val unigramScore: (String) -> Double = { word ->
            val freq = builtin.frequency(word, isBangla)
            if (freq > 0) kotlin.math.ln(1.0 + freq) / 12.0 else 0.0
        }
        val candidates = ngram.nextCandidates(normalized, limit * 2, unigramScore)
        val out = candidates.map { (word, score) ->
            PredictionCandidate(word, score, CandidateSource.NEXT_WORD)
        }.take(limit)
        return if (out.isEmpty()) {
            builtin.topWords(isBangla, limit).map { (word, _) ->
                PredictionCandidate(word, 0.0, CandidateSource.NEXT_WORD)
            }
        } else out
    }

    override fun getCorrection(token: String, isBangla: Boolean, now: Long): CorrectionResult? =
        autoCorrection.correct(token, isBangla, now, correctionEnabled = true)

    override fun onWordCommitted(
        token: String,
        isBangla: Boolean,
        previousWords: List<String>,
        now: Long
    ) {
        val word = token.trim()
        if (word.isEmpty()) return
        val tag = detector.classify(word).tag
        scope.launch {
            try {
                learningEngine?.onCommit(word, isBangla, tag, previousWords, now, sensitiveField = false)
            } catch (e: Exception) {
                android.util.Log.e("DictionaryManager", "onWordCommitted failed", e)
            }
        }
    }

    override fun isTokenConfident(token: String): Boolean {
        val freq = personalTrie.get(token.trim().lowercase())?.frequency ?: 0
        return freq >= cachedLearningThreshold
    }

    override fun clearPersonalData() {
        personalTrie.clear()
        ngram.clear()
        cache.clear()
        scope.launch {
            database?.learnedWordDao()?.clearAll()
            database?.learnedPhraseDao()?.clearAll()
            database?.personalWordDao()?.clearAll()
            database?.recentWordDao()?.clearAll()
        }
    }

    fun cacheHitRate(): Double = cache.hitRate()
}
