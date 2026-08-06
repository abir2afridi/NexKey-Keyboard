package com.example.prediction

import com.example.prediction.emoji.EmojiPredictionEngine
import com.example.prediction.engine.SuggestionEngine
import com.example.prediction.testutil.TestHarness
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PHASE 7: the 5 example mappings from the original spec resolve correctly and
 * emoji candidates join the same ranked suggestion strip.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class EmojiPredictionTest {

    private fun emojiFor(keyword: String): String = EmojiPredictionEngine.getByPrefix(keyword).first()

    @Test
    fun `happy maps to smiling face`() {
        assertEquals("\uD83D\uDE0A", emojiFor("happy"))
    }

    @Test
    fun `birthday maps to birthday cake`() {
        assertEquals("\uD83C\uDF82", emojiFor("birthday"))
    }

    @Test
    fun `love maps to red heart`() {
        assertEquals("\u2764\uFE0F", emojiFor("love"))
    }

    @Test
    fun `fire maps to fire`() {
        assertEquals("\uD83D\uDD25", emojiFor("fire"))
    }

    @Test
    fun `laugh maps to face with tears of joy`() {
        assertEquals("\uD83D\uDE02", emojiFor("laugh"))
    }

    @Test
    fun `emoji appears inline in the suggestion strip when keyword matches`() {
        val engine = SuggestionEngine(
            builtin = TestHarness.englishRepository(),
            personalTrie = com.example.prediction.personal.PersonalTrieIndex(),
            ngram = TestHarness.ngram(),
            ranking = TestHarness.ranking(),
            threshold = { 3 }
        )
        val results = engine.getSuggestions("hap", false, emptyList(), false, 5, System.currentTimeMillis(), emojiEnabled = true)
        assertTrue(
            "expected an emoji among suggestions for 'hap', got $results",
            results.any { it.word == "\uD83D\uDE0A" }
        )
    }

    @Test
    fun `emoji candidates disappear when the toggle is off`() {
        val engine = SuggestionEngine(
            builtin = TestHarness.englishRepository(),
            personalTrie = com.example.prediction.personal.PersonalTrieIndex(),
            ngram = TestHarness.ngram(),
            ranking = TestHarness.ranking(),
            threshold = { 3 }
        )
        val results = engine.getSuggestions("hap", false, emptyList(), false, 5, System.currentTimeMillis(), emojiEnabled = false)
        assertTrue(results.none { it.word == "\uD83D\uDE0A" })
    }
}
