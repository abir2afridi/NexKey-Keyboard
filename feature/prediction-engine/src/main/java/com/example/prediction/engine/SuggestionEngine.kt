package com.example.prediction.engine

import com.example.prediction.builtin.BuiltInDictionaryRepository
import com.example.prediction.emoji.EmojiPredictionEngine
import com.example.prediction.ngram.NgramIndex
import com.example.prediction.personal.PersonalTrieIndex
import com.example.prediction.ranking.RankingEngine

/**
 * Per-keystroke prefix search: builtin DAWG results and the personal trie are merged
 * in parallel and ranked by RankingEngine. Emoji keyword matches join the same strip.
 * Runs against in-memory structures only, so it is safe to call per keystroke.
 */
class SuggestionEngine(
    private val builtin: BuiltInDictionaryRepository,
    private val personalTrie: PersonalTrieIndex,
    private val ngram: NgramIndex,
    private val ranking: RankingEngine,
    private val threshold: () -> Int,
    private val corrector: ((String, Boolean, Long) -> String?)? = null
) {

    private class Accum {
        var builtinFreq = 0
        var personalFreq = 0
        var lastUsedAt = 0L
        var emoji = false
    }

    fun getSuggestions(
        prefix: String,
        isBangla: Boolean,
        previousWords: List<String>,
        showTypedWordFirst: Boolean,
        limit: Int,
        now: Long,
        emojiEnabled: Boolean
    ): List<PredictionCandidate> {
        val query = prefix.trim().lowercase()
        if (query.isEmpty() || limit <= 0) return emptyList()

        val accs = HashMap<String, Accum>()

        for ((word, freq) in builtin.topSuggestions(query, isBangla, 60)) {
            accs.getOrPut(word) { Accum() }.builtinFreq = freq
        }
        for ((word, entry) in personalTrie.collectPrefix(query, 400)) {
            val acc = accs.getOrPut(word) { Accum() }
            acc.personalFreq = entry.frequency
            acc.lastUsedAt = entry.lastUsedAt
        }
        if (emojiEnabled) {
            for (emoji in EmojiPredictionEngine.getByPrefix(query)) {
                accs.getOrPut(emoji) { Accum() }.emoji = true
            }
        }

        val context = ngram.nextCandidates(previousWords, 20) { 0.0 }
            .associate { it.first to it.second }

        val thresholdValue = threshold()
        val results = accs.map { (word, acc) ->
            val confidence = ranking.confidenceFactor(acc.personalFreq, thresholdValue)
            val score = if (acc.emoji) {
                3.0 + ranking.confidenceFactor(acc.personalFreq, thresholdValue)
            } else {
                ranking.score(
                    builtinFrequency = acc.builtinFreq,
                    personalFrequency = acc.personalFreq,
                    personalConfidence = confidence,
                    lastUsedAt = acc.lastUsedAt,
                    now = now,
                    contextScore = context[word] ?: 0.0,
                    exactPrefix = true,
                    editDistance = 0.0
                )
            }
            PredictionCandidate(
                word = word,
                score = score,
                source = when {
                    acc.emoji -> CandidateSource.EMOJI
                    acc.personalFreq > 0 && acc.builtinFreq == 0 -> CandidateSource.PERSONAL
                    acc.personalFreq > 0 -> CandidateSource.LEARNING
                    else -> CandidateSource.BUILTIN
                },
                confident = acc.emoji || acc.personalFreq >= thresholdValue || acc.builtinFreq > 0
            )
        }.sortedByDescending { it.score }.take(limit).toMutableList()

        if (results.isEmpty() && corrector != null && query.length >= 3) {
            // Typo prefix with no dictionary matches: the strip still offers the
            // corrected spelling (advisory — no rewriting happens here).
            val correction = corrector(query, isBangla, now)
            if (correction != null && correction != query) {
                results.add(0, PredictionCandidate(correction, Double.MAX_VALUE, CandidateSource.BUILTIN))
            }
        }

        if (showTypedWordFirst && query.isNotEmpty() && results.none { it.word == query }) {
            results.add(0, PredictionCandidate(query, Double.MAX_VALUE, CandidateSource.BUILTIN))
        }
        return results.take(limit)
    }
}
