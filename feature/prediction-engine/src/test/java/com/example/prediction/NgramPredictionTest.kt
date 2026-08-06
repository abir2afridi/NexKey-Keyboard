package com.example.prediction

import com.example.prediction.ngram.NgramIndex
import com.example.prediction.testutil.TestHarness
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PHASE 4: bigram continuation via the interpolated-backoff model, fed through the
 * real learning path (committed phrases). Context-sensitive: after "Good", the
 * continuation words morning/night/luck/job surface together.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class NgramPredictionTest {

    @Test
    fun `Good surfaces morning night luck job among top candidates`() = runBlocking {
        val db = TestHarness.database()
        val trie = com.example.prediction.personal.PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie)
        val ngram = NgramIndex()
        val now = System.currentTimeMillis()

        // Learn each collocation several times so the bigram dominates backoff.
        repeat(5) { i ->
            val words = listOf("Good", listOf("morning", "night", "luck", "job")[i % 4])
            learning.onCommit(words[0].lowercase(), false, "en", emptyList(), now, false)
            learning.onCommit(words[1].lowercase(), false, "en", listOf(words[0].lowercase()), now, false)
        }
        TestHarness.loadNgramsFromDb(db, ngram)

        val candidates = ngram.nextCandidates(listOf("good"), limit = 10) { 0.0 }.map { it.first }
        val expected = listOf("morning", "night", "luck", "job")
        for (word in expected) {
            assertTrue("expected '$word' among next candidates after 'good', got $candidates", candidates.contains(word))
        }
        db.close()
    }

    @Test
    fun `trigram context is preferred over bigram when both exist`() = runBlocking {
        val db = TestHarness.database()
        val trie = com.example.prediction.personal.PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie)
        val ngram = NgramIndex()
        val now = System.currentTimeMillis()

        repeat(3) {
            learning.onCommit("thank", false, "en", emptyList(), now, false)
            learning.onCommit("you", false, "en", listOf("thank"), now, false)
            learning.onCommit("god", false, "en", listOf("thank"), now, false)
            learning.onCommit("so", false, "en", listOf("thank", "you"), now, false)
        }
        TestHarness.loadNgramsFromDb(db, ngram)

        val candidates = ngram.nextCandidates(listOf("thank", "you"), limit = 5) { 0.0 }
        assertEquals("so", candidates.first().first)
        db.close()
    }
}
