package com.example.prediction

import kotlinx.coroutines.flow.Flow

/**
 * Live feature flags (backed by the app's DataStore). Every toggle-gated code path
 * reads the current value via Flow so a Settings change takes effect on the next keystroke,
 * never a cached snapshot from IME startup.
 */
interface FeatureFlags {
    val autoCorrectionEnabled: Flow<Boolean>
    val typoCorrectionEnabled: Flow<Boolean>
    val nextWordPredictionEnabled: Flow<Boolean>
    val personalLearningEnabled: Flow<Boolean>
    val personalizedSuggestionsEnabled: Flow<Boolean>
    val emojiPredictionEnabled: Flow<Boolean>
    val incognito: Flow<Boolean>
    val learningThreshold: Flow<Int>
}
