package com.example.prediction

import com.example.prediction.correction.AutoCorrectionEngine
import com.example.prediction.engine.SuggestionEngine
import com.example.prediction.engine.WordBoundaryCommitHandler
import com.example.prediction.personal.PersonalTrieIndex
import com.example.prediction.testutil.TestFeatureFlags
import com.example.prediction.testutil.TestHarness
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PHASE 10 TOGGLES: each independent toggle must actually stop its code path —
 * a settings switch that changes a stored value nobody reads is a critical bug.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ToggleEnforcementTest {

    private var correctionCalls = 0
    private var nextWordCalls = 0

    private fun trackingAutoCorrection(): AutoCorrectionEngine {
        correctionCalls = 0
        return object : AutoCorrectionEngine(TestHarness.englishRepository(), PersonalTrieIndex(), { 3 }) {
            override fun correct(token: String, isBangla: Boolean, now: Long, correctionEnabled: Boolean) =
                super.correct(token, isBangla, now, correctionEnabled).also { correctionCalls++ }
        }
    }

    private fun handler(
        flags: TestFeatureFlags,
        autoCorrection: AutoCorrectionEngine,
        db: com.example.prediction.data.AppDatabase
    ): WordBoundaryCommitHandler = WordBoundaryCommitHandler(
        flags = flags,
        provider = object : com.example.prediction.PredictionProvider {
            override fun init(context: android.content.Context) {}
            override fun getSuggestions(prefix: String, isBangla: Boolean, previousWords: List<String>, showTypedWordFirst: Boolean, limit: Int, now: Long) = emptyList<com.example.prediction.engine.PredictionCandidate>()
            override fun getNextWordPredictions(previousWords: List<String>, isBangla: Boolean, limit: Int, now: Long): List<com.example.prediction.engine.PredictionCandidate> {
                nextWordCalls++
                return emptyList()
            }
            override fun getCorrection(token: String, isBangla: Boolean, now: Long): com.example.prediction.engine.CorrectionResult? = null
            override fun onWordCommitted(token: String, isBangla: Boolean, previousWords: List<String>, now: Long) {}
            override fun isTokenConfident(token: String) = false
            override fun clearPersonalData() {}
        },
        autoCorrection = autoCorrection,
        detector = com.example.prediction.banglish.ScriptIntentDetector()
    )

    @Test
    fun `typo correction toggle OFF - correction path not invoked, commit unchanged`() = runBlocking {
        val flags = TestFeatureFlags(typoCorrection = false)
        val db = TestHarness.database()
        val outcome = handler(flags, trackingAutoCorrection(), db)
            .handleCommit("recive", false, emptyList(), false, System.currentTimeMillis())
        assertEquals(0, correctionCalls)
        assertEquals("recive", outcome.committed)
        db.close()
    }

    @Test
    fun `typo correction toggle ON - correction path invoked`() = runBlocking {
        val flags = TestFeatureFlags(typoCorrection = true)
        val db = TestHarness.database()
        val outcome = handler(flags, trackingAutoCorrection(), db)
            .handleCommit("recive", false, emptyList(), false, System.currentTimeMillis())
        assertTrue(correctionCalls > 0)
        assertEquals("receive", outcome.committed)
        db.close()
    }

    @Test
    fun `next-word toggle OFF - next-word path not invoked and no next words`() = runBlocking {
        nextWordCalls = 0
        val flags = TestFeatureFlags(nextWord = false)
        val db = TestHarness.database()
        val outcome = handler(flags, trackingAutoCorrection(), db)
            .handleCommit("hello", false, emptyList(), false, System.currentTimeMillis())
        assertEquals(0, nextWordCalls)
        assertTrue(outcome.nextWords.isEmpty())
        db.close()
    }

    @Test
    fun `next-word toggle ON - next-word path invoked`() = runBlocking {
        nextWordCalls = 0
        val flags = TestFeatureFlags(nextWord = true)
        val db = TestHarness.database()
        handler(flags, trackingAutoCorrection(), db)
            .handleCommit("hello", false, emptyList(), false, System.currentTimeMillis())
        assertTrue(nextWordCalls > 0)
        db.close()
    }

    @Test
    fun `personal learning toggle OFF - zero learning writes`() = runBlocking {
        val flags = TestFeatureFlags(personalLearning = false)
        val db = TestHarness.database()
        val trie = PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie, learningEnabled = { flags.personalLearningFlow.value })
        val now = System.currentTimeMillis()

        learning.onCommit("vhalo", false, "bn-Latn", emptyList(), now, false)

        assertEquals(0, db.learnedWordDao().count())
        assertEquals(0, trie.size())
        db.close()
    }

    @Test
    fun `personal learning toggle ON - learning writes happen`() = runBlocking {
        val flags = TestFeatureFlags(personalLearning = true)
        val db = TestHarness.database()
        val trie = PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie, learningEnabled = { flags.personalLearningFlow.value })
        val now = System.currentTimeMillis()

        learning.onCommit("vhalo", false, "bn-Latn", emptyList(), now, false)

        assertTrue(db.learnedWordDao().count() > 0)
        db.close()
    }

    @Test
    fun `emoji toggle OFF - no emoji in strip even when keyword matches`() {
        val engine = SuggestionEngine(
            builtin = TestHarness.englishRepository(),
            personalTrie = PersonalTrieIndex(),
            ngram = TestHarness.ngram(),
            ranking = TestHarness.ranking(),
            threshold = { 3 }
        )
        val results = engine.getSuggestions("hap", false, emptyList(), false, 5, System.currentTimeMillis(), emojiEnabled = false)
        assertTrue(results.none { it.word == "\uD83D\uDE0A" })
    }
}
