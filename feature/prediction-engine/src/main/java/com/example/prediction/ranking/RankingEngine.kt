package com.example.prediction.ranking

import kotlin.math.ln
import kotlin.math.min

/** Tunable scoring weights — see /docs/SEARCH_DATA_STRUCTURES.md for rationale. */
data class RankingWeights(
    val builtinFrequency: Double = 1.0,
    val personalBoost: Double = 6.0,
    val recency: Double = 0.4,
    val contextBonus: Double = 2.5,
    val exactPrefixBonus: Double = 1.0,
    val editDistancePenalty: Double = 1.2
)

/**
 * The ONLY place the scoring formula lives; builtin and personal results both flow
 * through here to be merged and sorted.
 *
 * score = w1*ln(builtinFreq+1) + w2*ln(personalFreq*confidence+1) + w3*recency
 *         + w4*contextScore + w5*exactPrefixBonus - w6*editDistance
 */
class RankingEngine(private val weights: RankingWeights = RankingWeights()) {

    /** [personalConfidence] ∈ (0,1] scales a below-threshold personal word's boost. */
    fun score(
        builtinFrequency: Int,
        personalFrequency: Int,
        personalConfidence: Double,
        lastUsedAt: Long,
        now: Long,
        contextScore: Double,
        exactPrefix: Boolean,
        editDistance: Double
    ): Double {
        val f1 = if (builtinFrequency > 0) ln(1.0 + builtinFrequency) * weights.builtinFrequency else 0.0
        val f2 = if (personalFrequency > 0) {
            ln(1.0 + personalFrequency * personalConfidence) * weights.personalBoost
        } else 0.0
        val f3 = recencyScore(lastUsedAt, now) * weights.recency
        val f4 = contextScore * weights.contextBonus
        val f5 = if (exactPrefix) weights.exactPrefixBonus else 0.0
        val f6 = editDistance * weights.editDistancePenalty
        return f1 + f2 + f3 + f4 + f5 - f6
    }

    fun recencyScore(lastUsedAt: Long, now: Long): Double {
        if (lastUsedAt <= 0) return 0.0
        val hours = (now - lastUsedAt).coerceAtLeast(0) / 3_600_000.0
        return 1.0 / (1.0 + ln(1.0 + hours / 24.0))
    }

    /** freq == threshold → 1.0 (confident); freq == 1 with threshold 3 → ~0.33. */
    fun confidenceFactor(personalFrequency: Int, threshold: Int): Double {
        if (threshold <= 0) return 1.0
        return min(1.0, personalFrequency / threshold.toDouble())
    }
}
