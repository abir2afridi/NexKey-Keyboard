package com.example.prediction

import com.example.prediction.correction.AutoCorrectionEngine
import com.example.prediction.testutil.TestHarness
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PHASE 3: the four exact misspelling examples from the original spec, plus
 * swapped-letters ("hte"→"the") and extra/missing/repeated-letter coverage.
 * No misspelling pairs are hardcoded — the weighted Damerau-Levenshtein engine
 * derives them generically from the builtin dictionary.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class TypoCorrectionTest {

    private val engine = AutoCorrectionEngine(
        builtin = TestHarness.englishRepository(),
        personalTrie = com.example.prediction.personal.PersonalTrieIndex(),
        threshold = { 3 }
    )

    private fun correct(typo: String): String? =
        engine.correct(typo, isBangla = false, now = System.currentTimeMillis(), correctionEnabled = true)?.correction

    @Test
    fun `recive corrects to receive`() {
        assertEquals("receive", correct("recive"))
    }

    @Test
    fun `beleive corrects to believe`() {
        assertEquals("believe", correct("beleive"))
    }

    @Test
    fun `definately corrects to definitely`() {
        assertEquals("definitely", correct("definately"))
    }

    @Test
    fun `enviroment corrects to environment`() {
        assertEquals("environment", correct("enviroment"))
    }

    @Test
    fun `swapped adjacent letters hte corrects to the`() {
        assertEquals("the", correct("hte"))
    }

    @Test
    fun `extra letter in word is removed`() {
        assertEquals("the", correct("thhe"))
    }

    @Test
    fun `missing letter in word is inserted`() {
        assertEquals("good", correct("god"))
    }

    @Test
    fun `repeated letter in word is collapsed`() {
        assertEquals("good", correct("goodd"))
    }

    @Test
    fun `correctly typed words are never corrected`() {
        assertNull(correct("receive"))
        assertNull(correct("the"))
        assertNull(correct("good"))
    }
}
