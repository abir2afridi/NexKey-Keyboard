package com.example.prediction.engine

/**
 * In-memory LRU prediction cache for repeated-prefix queries within a fast typing burst.
 * Never persisted. Hit-rate is exposed for testing (see /docs/APK_SIZE_BUDGET.md).
 */
class CacheManager(private val maxEntries: Int = 128) {

    private val map = object : LinkedHashMap<String, List<PredictionCandidate>>(maxEntries, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, List<PredictionCandidate>>?): Boolean {
            return size > maxEntries
        }
    }

    private var hits = 0L
    private var misses = 0L

    @Synchronized
    fun get(key: String): List<PredictionCandidate>? {
        val value = map[key]
        if (value != null) hits++ else misses++
        return value
    }

    @Synchronized
    fun put(key: String, value: List<PredictionCandidate>) {
        map[key] = value
    }

    @Synchronized
    fun clear() {
        map.clear()
    }

    @Synchronized
    fun hitRate(): Double {
        val total = hits + misses
        return if (total == 0L) 0.0 else hits.toDouble() / total
    }

    @Synchronized
    fun size(): Int = map.size
}
