package com.example.prediction

import com.example.prediction.engine.CandidateSource
import com.example.prediction.engine.SuggestionEngine
import com.example.prediction.testutil.TestHarness
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PHASE 5 ACCEPTANCE SCENARIO (docs/SuggestionEngine.md §5.2):
 * commit "vhalo lagtase nah" three times (threshold = 3), then type "vha":
 * "vhalo" must be the #1 ranked suggestion; after "vhalo", "lagtase" must be
 * offered as a next-word prediction. A one-off typo must NOT be promoted.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PersonalLearningScenarioTest {

    @Test
    fun `vha surfaces vhalo as number one after 3 commits`() = runBlocking {
        val db = TestHarness.database()
        val trie = com.example.prediction.personal.PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie)
        val engine = SuggestionEngine(
            builtin = TestHarness.englishRepository(),
            personalTrie = trie,
            ngram = TestHarness.ngram(),
            ranking = TestHarness.ranking(),
            threshold = { 3 }
        )
        val now = System.currentTimeMillis()

        repeat(3) {
            learning.onCommit("vhalo", isBangla = false, languageTag = "bn-Latn", previousWords = emptyList(), now = now, sensitiveField = false)
            learning.onCommit("lagtase", isBangla = false, languageTag = "bn-Latn", previousWords = listOf("vhalo"), now = now, sensitiveField = false)
            learning.onCommit("nah", isBangla = false, languageTag = "bn-Latn", previousWords = listOf("vhalo", "lagtase"), now = now, sensitiveField = false)
        }

        val results = engine.getSuggestions(
            prefix = "vha",
            isBangla = false,
            previousWords = emptyList(),
            showTypedWordFirst = false,
            limit = 3,
            now = now,
            emojiEnabled = false
        )

        assertTrue("expected at least 1 result, got $results", results.isNotEmpty())
        assertEquals("vhalo must be #1", "vhalo", results[0].word)
        assertTrue("vhalo must be confident", results[0].confident)
        assertEquals(CandidateSource.PERSONAL, results[0].source)
        db.close()
    }

    @Test
    fun `after committing vhalo, lagtase is offered as next-word`() = runBlocking {
        val db = TestHarness.database()
        val trie = com.example.prediction.personal.PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie)
        val ngram = TestHarness.ngram()
        val now = System.currentTimeMillis()

        repeat(3) {
            learning.onCommit("vhalo", false, "bn-Latn", emptyList(), now, false)
            learning.onCommit("lagtase", false, "bn-Latn", listOf("vhalo"), now, false)
        }
        TestHarness.loadNgramsFromDb(db, ngram)

        val next = ngram.nextCandidates(listOf("vhalo"), limit = 3) { 0.0 }
        assertTrue("expected lagtase among next candidates, got $next", next.isNotEmpty())
        assertEquals("lagtase", next.first().first)
        db.close()
    }

    @Test
    fun `one-off typo is never promoted to a confident suggestion`() = runBlocking {
        val db = TestHarness.database()
        val trie = com.example.prediction.personal.PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie)
        val engine = SuggestionEngine(
            builtin = TestHarness.englishRepository(),
            personalTrie = trie,
            ngram = TestHarness.ngram(),
            ranking = TestHarness.ranking(),
            threshold = { 3 }
        )
        val now = System.currentTimeMillis()

        learning.onCommit("vhal", false, "bn-Latn", emptyList(), now, false)
        val results = engine.getSuggestions("vha", false, emptyList(), false, 3, now, false)

        assertFalse(
            "single-commit word must not be marked confident: $results",
            results.any { it.word == "vhal" && it.confident }
        )

        learning.onCommit("vhalo", false, "bn-Latn", emptyList(), now, false)
        repeat(2) {
            learning.onCommit("vhalo", false, "bn-Latn", emptyList(), now, false)
        }
        val promoted = engine.getSuggestions("vha", false, emptyList(), false, 3, now, false)
        assertEquals("vhalo", promoted.first().word)
        assertTrue(promoted.first().confident)
        db.close()
    }

    @Test
    fun `learned word is indexed by partial prefix before word boundary`() {
        val trie = com.example.prediction.personal.PersonalTrieIndex()
        trie.addWord("vhalo", frequency = 3, lastUsedAt = System.currentTimeMillis())
        val results = trie.collectPrefix("vha")
        assertTrue(results.any { it.first == "vhalo" })
        assertNotNull(trie.get("vhalo"))
    }
}
