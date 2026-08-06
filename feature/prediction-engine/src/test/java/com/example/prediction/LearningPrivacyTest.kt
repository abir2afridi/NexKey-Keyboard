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
 * PHASE 10 PRIVACY: incognito mode and sensitive fields (password etc.) must produce
 * ZERO learning writes — verified against the real Room tables, not just a flag.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LearningPrivacyTest {

    @Test
    fun `incognito mode - zero DB writes and zero trie entries`() = runBlocking {
        val db = TestHarness.database()
        val trie = PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie, incognitoEnabled = { true })
        val now = System.currentTimeMillis()

        repeat(3) {
            learning.onCommit("vhalo", false, "bn-Latn", emptyList(), now, false)
        }

        assertEquals("learned words table must be empty in incognito", 0, db.learnedWordDao().count())
        assertEquals("learned phrases table must be empty in incognito", 0, db.learnedPhraseDao().count())
        assertEquals("recent words table must be empty in incognito", 0, db.recentWordDao().count())
        assertEquals("in-memory trie must be empty in incognito", 0, trie.size())
        db.close()
    }

    @Test
    fun `sensitive fields - zero DB writes`() = runBlocking {
        val db = TestHarness.database()
        val trie = PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie)
        val now = System.currentTimeMillis()

        learning.onCommit("password", false, "en", emptyList(), now, sensitiveField = true)

        assertEquals(0, db.learnedWordDao().count())
        assertEquals(0, trie.size())
        db.close()
    }

    @Test
    fun `normal mode - learning writes happen`() = runBlocking {
        val db = TestHarness.database()
        val trie = PersonalTrieIndex()
        val learning = TestHarness.learningEngine(db, trie)
        val now = System.currentTimeMillis()

        learning.onCommit("vhalo", false, "bn-Latn", emptyList(), now, false)

        assertTrue(db.learnedWordDao().count() > 0)
        assertTrue(trie.size() > 0)
        db.close()
    }
}
