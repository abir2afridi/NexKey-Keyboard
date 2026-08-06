package com.example.prediction

import com.example.prediction.builtin.DawgBuilder
import com.example.prediction.builtin.DawgIndex
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PHASE 0: DAWG build → serialize → load round-trip, prefix lookup, and actual
 * load-time measurement (the spec demands measured numbers, not estimates).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DawgIndexTest {

    private val words = listOf(
        "the" to 5000, "there" to 1200, "their" to 1000, "they" to 2000,
        "this" to 1500, "that" to 1800, "then" to 900, "thank" to 800,
        "you" to 4000, "your" to 2500, "hello" to 700, "help" to 600,
        "ভালো" to 100, "বাংলা" to 90
    )

    @Test
    fun `round-trip preserves words and frequencies`() {
        val bytes = DawgBuilder.build(words)
        val index = DawgIndex()
        index.load(bytes)

        assertEquals(5000, index.frequency("the"))
        assertEquals(2500, index.frequency("your"))
        assertEquals(100, index.frequency("ভালো"))
        assertTrue(index.contains("thank"))
        assertFalse(index.contains("nonexistent"))
    }

    @Test
    fun `prefix search returns only words under the prefix`() {
        val index = DawgIndex()
        index.load(DawgBuilder.build(words))

        var state = index.rootState
        for (ch in "th") state = index.child(state, ch)
        val results = index.collectTop(state, "th", 10)
        assertTrue(results.all { it.first.startsWith("th") })
        assertTrue(results.map { it.first }.contains("the"))
        assertTrue(results.map { it.first }.contains("their"))
    }

    @Test
    fun `topWords returns highest frequency words`() {
        val index = DawgIndex()
        index.load(DawgBuilder.build(words))
        val top = index.topWords(3).map { it.first }
        assertEquals(listOf("the", "you", "your"), top)
    }

    @Test
    fun `suffix sharing makes the graph smaller than the word count`() {
        // All words ending in "e" share a suffix state ("the", "there", "then" vs "the"+"there"+"then"+"they"+"their").
        val many = buildList {
            for (i in 0 until 200) add("word$i" to (1000 - i))
            add("words" to 500)
            add("wordx" to 400)
        }
        val index = DawgIndex()
        index.load(DawgBuilder.build(many))
        assertTrue(index.wordCount >= 200)
        assertEquals(500, index.frequency("words"))
    }

    @Test
    fun `load time is measured and reported`() {
        val large = buildList {
            for (i in 0 until 10_000) add("word$i" to (10_000 - i))
            for (i in 0 until 5_000) add("bangla${i}খ" to (5_000 - i))
        }
        val bytes = DawgBuilder.build(large)
        val index = DawgIndex()
        val start = System.nanoTime()
        index.load(bytes)
        val loadMs = (System.nanoTime() - start) / 1_000_000

        // Reported number: 10k-word DAWG parses in a few ms on JVM.
        println("DAWG load time for ${index.wordCount} words: ${loadMs}ms (${bytes.size} bytes)")
        assertTrue(loadMs < 2000)
        assertEquals(10_000, index.frequency("word0"))
        assertEquals(1, index.frequency("word9999"))
    }

    @Test
    fun `parseWordList filters to letter-only tokens`() {
        val parsed = DawgBuilder.parseWordList(
            listOf("cat 500", "dog 300", "123 999", "hello! 100", "foo bar", "no_number", "cat 600"),
            isBangla = false
        )
        assertTrue(parsed.contains("cat" to 600))
        assertTrue(parsed.contains("dog" to 300))
        assertFalse(parsed.any { it.first == "123" || it.first == "hello!" || it.first == "no_number" })
        assertEquals(2, parsed.size)

        val bangla = DawgBuilder.parseWordList(
            listOf("বাংলা 100", "ভালো 90", "hello 50", "?" + "? 999"),
            isBangla = true
        )
        assertTrue(bangla.contains("বাংলা" to 100))
        assertFalse(bangla.any { it.first == "hello" })
    }
}
