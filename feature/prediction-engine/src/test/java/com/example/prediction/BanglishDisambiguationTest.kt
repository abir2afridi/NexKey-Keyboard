package com.example.prediction

import com.example.prediction.banglish.ScriptIntentDetector
import com.example.prediction.correction.AutoCorrectionEngine
import com.example.prediction.engine.SuggestionEngine
import com.example.prediction.personal.PersonalTrieIndex
import com.example.prediction.testutil.TestHarness
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PHASE 6: "korbo", "korteci", "korsi" are distinct learned words — never
 * typo-corrected into each other, each independently ranked after repeated use.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BanglishDisambiguationTest {

    @Test
    fun `korbo korteci korsi rank independently and never cross-correct`() = runBlocking {
        val db = TestHarness.database()
        val trie = PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie)
        val engine = SuggestionEngine(
            builtin = TestHarness.englishRepository(),
            personalTrie = trie,
            ngram = TestHarness.ngram(),
            ranking = TestHarness.ranking(),
            threshold = { 3 }
        )
        val autoCorrection = AutoCorrectionEngine(TestHarness.englishRepository(), trie, { 3 })
        val now = System.currentTimeMillis()

        repeat(5) {
            learning.onCommit("korbo", false, "bn-Latn", emptyList(), now, false)
            learning.onCommit("korteci", false, "bn-Latn", emptyList(), now, false)
            learning.onCommit("korsi", false, "bn-Latn", emptyList(), now, false)
        }

        val korboSuggestions = engine.getSuggestions("korb", false, emptyList(), false, 3, now, false)
        assertTrue(korboSuggestions.any { it.word == "korbo" })

        val korteciSuggestions = engine.getSuggestions("korte", false, emptyList(), false, 3, now, false)
        assertTrue(korteciSuggestions.any { it.word == "korteci" })

        val korsiSuggestions = engine.getSuggestions("kors", false, emptyList(), false, 3, now, false)
        assertTrue(korsiSuggestions.any { it.word == "korsi" })

        assertNull("korsi must never be rewritten into korbo", autoCorrection.correct("korsi", false, now, true))
        assertNull("korbo must never be rewritten", autoCorrection.correct("korbo", false, now, true))
        assertNull("korteci must never be rewritten", autoCorrection.correct("korteci", false, now, true))

        db.close()
    }

    @Test
    fun `detector classifies bangla script and banglish latin tokens`() {
        val detector = ScriptIntentDetector()
        assertEquals(ScriptIntentDetector.Intent.BANGLA, detector.classify("ভালো"))
        assertEquals(ScriptIntentDetector.Intent.BANGLISH, detector.classify("vhalo"))
        assertEquals(ScriptIntentDetector.Intent.ENGLISH, detector.classify("keyboard"))
        assertEquals(ScriptIntentDetector.Intent.BANGLISH, detector.classify("korsi"))
    }
}
