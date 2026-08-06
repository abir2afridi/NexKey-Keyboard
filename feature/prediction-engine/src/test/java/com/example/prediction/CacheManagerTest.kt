package com.example.prediction

import com.example.prediction.engine.CacheManager
import com.example.prediction.engine.PredictionCandidate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PHASE 10: bounded LRU prediction cache — repeated-prefix queries within a burst
 * hit the cache, eviction is LRU, and hit-rate is measurable.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CacheManagerTest {

    private fun entry(word: String) = PredictionCandidate(word, 1.0, com.example.prediction.engine.CandidateSource.BUILTIN)

    @Test
    fun `repeat query hits the cache`() {
        val cache = CacheManager(maxEntries = 16)
        val results = listOf(entry("the"))
        cache.put("en|th|1", results)
        assertEquals(results, cache.get("en|th|1"))
        assertNull(cache.get("en|th|2"))
        assertTrue(cache.hitRate() >= 0.5)
    }

    @Test
    fun `oldest entries are evicted first`() {
        val cache = CacheManager(maxEntries = 3)
        cache.put("k1", listOf(entry("a")))
        cache.put("k2", listOf(entry("b")))
        cache.put("k3", listOf(entry("c")))
        cache.get("k1") // touch k1
        cache.put("k4", listOf(entry("d"))) // evicts k2 (least recently used)
        assertNotNull(cache.get("k1"))
        assertNull(cache.get("k2"))
        assertNotNull(cache.get("k3"))
        assertNotNull(cache.get("k4"))
    }

    @Test
    fun `clear empties the cache`() {
        val cache = CacheManager(maxEntries = 8)
        cache.put("k1", listOf(entry("a")))
        cache.clear()
        assertNull(cache.get("k1"))
        assertEquals(0, cache.size())
    }
}
