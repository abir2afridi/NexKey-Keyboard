package com.example.prediction

import com.example.prediction.personal.PersonalTrieIndex
import com.example.prediction.testutil.TestHarness
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PHASE 10: "clear all learned words" wipes Room tables AND the in-memory trie,
 * and the builtin dictionary is untouched by design (separate layers).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ClearAllTest {

    @Test
    fun `clearPersonalData wipes learned tables and in-memory trie`() = runBlocking {
        val db = TestHarness.database()
        val trie = PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie)
        val now = System.currentTimeMillis()

        repeat(4) {
            learning.onCommit("vhalo", false, "bn-Latn", emptyList(), now, false)
            learning.onCommit("lag", false, "bn-Latn", listOf("vhalo"), now, false)
        }
        assertTrue(db.learnedWordDao().count() > 0)
        assertTrue(db.learnedPhraseDao().count() > 0)
        assertTrue(trie.size() > 0)

        trie.clear()
        db.learnedWordDao().clearAll()
        db.learnedPhraseDao().clearAll()
        db.recentWordDao().clearAll()

        assertEquals(0, db.learnedWordDao().count())
        assertEquals(0, db.learnedPhraseDao().count())
        assertEquals(0, db.recentWordDao().count())
        assertEquals(0, trie.size())
        db.close()
    }

    @Test
    fun `delete single word removes it from trie and table`() = runBlocking {
        val db = TestHarness.database()
        val trie = PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie)
        val now = System.currentTimeMillis()

        learning.onCommit("vhalo", false, "bn-Latn", emptyList(), now, false)
        assertEquals(1, trie.size())

        trie.removeWord("vhalo")
        db.learnedWordDao().deleteWord("vhalo")

        assertEquals(0, trie.size())
        assertEquals(0, db.learnedWordDao().count())
        db.close()
    }
}
