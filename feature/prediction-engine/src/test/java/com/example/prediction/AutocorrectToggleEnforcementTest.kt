package com.example.prediction

import com.example.prediction.correction.AutoCorrectionEngine
import com.example.prediction.engine.SuggestionEngine
import com.example.prediction.engine.WordBoundaryCommitHandler
import com.example.prediction.testutil.TestFeatureFlags
import com.example.prediction.testutil.TestHarness
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PHASE 3.1 SETTINGS-COMPLIANCE: autocorrect OFF -> committed text stays exactly as
 * typed, while the suggestion strip still offers the corrected spelling. ON -> text
 * is auto-rewritten. The gate is a single boolean read at the top of the commit path.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AutocorrectToggleEnforcementTest {

    private val builtin = TestHarness.englishRepository()
    private val trie = com.example.prediction.personal.PersonalTrieIndex()

    private fun correctionEngine(threshold: () -> Int = { 3 }) =
        AutoCorrectionEngine(builtin, trie, threshold)

    @Test
    fun `toggle OFF - typed typo commits unchanged and strip still offers correction`() = runBlocking {
        val flags = TestFeatureFlags(autoCorrection = false)
        val db = TestHarness.database()
        val handler = WordBoundaryCommitHandler(
            flags = flags,
            provider = object : com.example.prediction.PredictionProvider {
                override fun init(context: android.content.Context) {}
                override fun getSuggestions(prefix: String, isBangla: Boolean, previousWords: List<String>, showTypedWordFirst: Boolean, limit: Int, now: Long) = emptyList<com.example.prediction.engine.PredictionCandidate>()
                override fun getNextWordPredictions(previousWords: List<String>, isBangla: Boolean, limit: Int, now: Long) = emptyList<com.example.prediction.engine.PredictionCandidate>()
                override fun getCorrection(token: String, isBangla: Boolean, now: Long): com.example.prediction.engine.CorrectionResult? = null
                override fun onWordCommitted(token: String, isBangla: Boolean, previousWords: List<String>, now: Long) {}
                override fun isTokenConfident(token: String) = false
                override fun clearPersonalData() {}
            },
            autoCorrection = correctionEngine(),
            detector = com.example.prediction.banglish.ScriptIntentDetector()
        )

        val outcome = handler.handleCommit("recive", isBangla = false, previousWords = emptyList(), sensitiveField = false, now = System.currentTimeMillis())

        assertEquals("committed text must stay exactly as typed when autocorrect is OFF", "recive", outcome.committed)
        assertTrue(!outcome.autoCorrected)

        val engine = SuggestionEngine(
            builtin = builtin, personalTrie = trie, ngram = TestHarness.ngram(),
            ranking = TestHarness.ranking(), threshold = { 3 },
            corrector = { prefix, isBangla, _ ->
                correctionEngine().correct(prefix, isBangla, System.currentTimeMillis(), true)?.correction
            }
        )
        val strip = engine.getSuggestions("reciv", false, emptyList(), false, 4, System.currentTimeMillis(), false)
        assertTrue("strip must still contain the corrected spelling when autocorrect OFF", strip.any { it.word == "receive" })
        db.close()
    }

    @Test
    fun `toggle ON - typed typo is auto-corrected at word-boundary commit`() {
        val flags = TestFeatureFlags(autoCorrection = true)
        val handler = WordBoundaryCommitHandler(
            flags = flags,
            provider = object : com.example.prediction.PredictionProvider {
                override fun init(context: android.content.Context) {}
                override fun getSuggestions(prefix: String, isBangla: Boolean, previousWords: List<String>, showTypedWordFirst: Boolean, limit: Int, now: Long) = emptyList<com.example.prediction.engine.PredictionCandidate>()
                override fun getNextWordPredictions(previousWords: List<String>, isBangla: Boolean, limit: Int, now: Long) = emptyList<com.example.prediction.engine.PredictionCandidate>()
                override fun getCorrection(token: String, isBangla: Boolean, now: Long): com.example.prediction.engine.CorrectionResult? = null
                override fun onWordCommitted(token: String, isBangla: Boolean, previousWords: List<String>, now: Long) {}
                override fun isTokenConfident(token: String) = false
                override fun clearPersonalData() {}
            },
            autoCorrection = correctionEngine(),
            detector = com.example.prediction.banglish.ScriptIntentDetector()
        )

        val outcome = runBlocking {
            handler.handleCommit("recive", false, emptyList(), false, System.currentTimeMillis())
        }
        assertEquals("receive", outcome.committed)
        assertTrue(outcome.autoCorrected)
    }

    @Test
    fun `engine never corrects when the gate flag is false`() {
        val result = correctionEngine().correct("recive", isBangla = false, now = System.currentTimeMillis(), correctionEnabled = false)
        assertNull(result)
    }

    @Test
    fun `engine corrects when the gate flag is true`() {
        val result = correctionEngine().correct("recive", isBangla = false, now = System.currentTimeMillis(), correctionEnabled = true)
        assertNotNull(result)
        assertEquals("receive", result!!.correction)
    }
}
