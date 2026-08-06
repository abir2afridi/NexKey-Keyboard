package com.example.prediction.correction

import kotlin.math.abs
import kotlin.math.hypot

/**
 * QWERTY key-coordinate map used to weight substitution costs in fuzzy matching:
 * adjacent keys (fat-finger patterns) cost less than distant ones.
 * Non-letter or unknown chars cost the full weight. Bangla-script tokens fall back
 * to uniform weight (documented in /docs/SEARCH_DATA_STRUCTURES.md).
 */
object KeyboardProximityMap {

    private val rows = listOf(
        "qwertyuiop",
        "asdfghjkl",
        "zxcvbnm"
    )

    private val positions: Map<Char, Pair<Double, Double>> = buildMap {
        for ((rowIndex, row) in rows.withIndex()) {
            for ((colIndex, ch) in row.withIndex()) {
                put(ch, (colIndex + rowIndex * 0.5).toDouble() to rowIndex.toDouble())
            }
        }
    }

    private val cache = HashMap<String, Double>()

    /**
     * Substitution cost for replacing [a] with [b]. Same key = 0, adjacent ≈ 0.3,
     * far keys ≈ 1.0. Non-letters always cost 1.0.
     */
    fun substitutionCost(a: Char, b: Char): Double {
        val key = "$a$b"
        cache[key]?.let { return it }
        val cost = compute(a, b)
        cache[key] = cost
        return cost
    }

    private fun compute(a: Char, b: Char): Double {
        val pa = positions[a] ?: return 1.0
        val pb = positions[b] ?: return 1.0
        if (pa == pb) return 0.0
        val dist = hypot(abs(pa.first - pb.first), abs(pa.second - pb.second))
        return minOf(1.0, 0.2 + dist * 0.5)
    }
}
