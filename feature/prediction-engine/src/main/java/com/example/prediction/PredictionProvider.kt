package com.example.prediction

import com.example.prediction.engine.PredictionCandidate
import com.example.prediction.engine.CorrectionResult

/**
 * The single entry point the keyboard UI/IME layer uses. A future transformer-based
 * model can replace the implementation behind this interface without touching the UI.
 */
interface PredictionProvider {

    /** Loads builtin assets (DAWG), Room-backed personal index and n-gram tables. */
    fun init(context: android.content.Context)

    /** Per-keystroke prefix suggestions (builtin + personal + emoji, ranked). */
    fun getSuggestions(
        prefix: String,
        isBangla: Boolean,
        previousWords: List<String>,
        showTypedWordFirst: Boolean,
        limit: Int,
        now: Long
    ): List<PredictionCandidate>

    /** Next-word prediction from committed context (n-gram backoff). */
    fun getNextWordPredictions(
        previousWords: List<String>,
        isBangla: Boolean,
        limit: Int,
        now: Long
    ): List<PredictionCandidate>

    /** Fuzzy typo correction on a committed token. Null if no confident correction. */
    fun getCorrection(token: String, isBangla: Boolean, now: Long): CorrectionResult?

    /** Called on every word/sentence commit so the engine can learn (respects gates). */
    fun onWordCommitted(token: String, isBangla: Boolean, previousWords: List<String>, now: Long)

    /** True if the token is a confidently learned personal word (protects Banglish variants). */
    fun isTokenConfident(token: String): Boolean

    /** Wipes personal/learned/phrase/recent data (Room + in-memory) — builtin untouched. */
    fun clearPersonalData()
}
