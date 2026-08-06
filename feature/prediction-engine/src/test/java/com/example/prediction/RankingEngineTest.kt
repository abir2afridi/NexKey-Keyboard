package com.example.prediction

import com.example.prediction.ranking.RankingEngine
import com.example.prediction.ranking.RankingWeights
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * PHASE 2: ranking formula — a personal word (low raw frequency) outranks a more
 * common builtin word for the same prefix thanks to the personal boost multiplier.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class RankingEngineTest {

    private val now = System.currentTimeMillis()

    @Test
    fun `personal boost lets a typed word outrank a common builtin word`() {
        val ranking = RankingEngine()
        // "vhalo" personal freq 3, no builtin freq. "the" builtin freq 5000, no personal freq.
        val personalScore = ranking.score(
            builtinFrequency = 0, personalFrequency = 3, personalConfidence = 1.0,
            lastUsedAt = now, now = now, contextScore = 0.0, exactPrefix = true, editDistance = 0.0
        )
        val builtinScore = ranking.score(
            builtinFrequency = 5000, personalFrequency = 0, personalConfidence = 1.0,
            lastUsedAt = 0L, now = now, contextScore = 0.0, exactPrefix = true, editDistance = 0.0
        )
        assertTrue("personal word must outrank common builtin: $personalScore vs $builtinScore", personalScore > builtinScore)
    }

    @Test
    fun `below-threshold personal word is scaled down`() {
        val ranking = RankingEngine()
        val confident = ranking.score(0, 3, 1.0, now, now, 0.0, true, 0.0)
        val once = ranking.score(0, 1, ranking.confidenceFactor(1, 3), now, now, 0.0, true, 0.0)
        assertTrue("confident word must score higher than single-use word", confident > once)
    }

    @Test
    fun `recency decays with time`() {
        val ranking = RankingEngine()
        val fresh = ranking.recencyScore(now - 3_600_000, now)
        val stale = ranking.recencyScore(now - 30L * 24 * 3_600_000, now)
        assertTrue(fresh > stale)
    }

    @Test
    fun `context bonus and exact prefix raise the score`() {
        val ranking = RankingEngine()
        val base = ranking.score(100, 0, 1.0, 0L, now, 0.0, true, 0.0)
        val withContext = ranking.score(100, 0, 1.0, 0L, now, 0.5, true, 0.0)
        val withPenalty = ranking.score(100, 0, 1.0, 0L, now, 0.0, true, 1.0)
        assertTrue(withContext > base)
        assertTrue(withPenalty < base)
    }

    @Test
    fun `weights are tunable`() {
        val weighted = RankingEngine(RankingWeights(personalBoost = 100.0))
        val light = RankingEngine(RankingWeights(personalBoost = 0.1))
        val scoreHeavy = weighted.score(0, 5, 1.0, now, now, 0.0, true, 0.0)
        val scoreLight = light.score(0, 5, 1.0, now, now, 0.0, true, 0.0)
        assertTrue(scoreHeavy > scoreLight)
    }
}
