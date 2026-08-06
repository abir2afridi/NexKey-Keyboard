package com.example.prediction.testutil

import com.example.prediction.FeatureFlags
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestFeatureFlags(
    autoCorrection: Boolean = true,
    typoCorrection: Boolean = true,
    nextWord: Boolean = true,
    personalLearning: Boolean = true,
    personalizedSuggestions: Boolean = true,
    emoji: Boolean = true,
    incognito: Boolean = false,
    threshold: Int = 3
) : FeatureFlags {

    val autoCorrectionFlow = MutableStateFlow(autoCorrection)
    val typoCorrectionFlow = MutableStateFlow(typoCorrection)
    val nextWordFlow = MutableStateFlow(nextWord)
    val personalLearningFlow = MutableStateFlow(personalLearning)
    val personalizedSuggestionsFlow = MutableStateFlow(personalizedSuggestions)
    val emojiFlow = MutableStateFlow(emoji)
    val incognitoFlow = MutableStateFlow(incognito)
    val thresholdFlow = MutableStateFlow(threshold)

    override val autoCorrectionEnabled: Flow<Boolean> get() = autoCorrectionFlow
    override val typoCorrectionEnabled: Flow<Boolean> get() = typoCorrectionFlow
    override val nextWordPredictionEnabled: Flow<Boolean> get() = nextWordFlow
    override val personalLearningEnabled: Flow<Boolean> get() = personalLearningFlow
    override val personalizedSuggestionsEnabled: Flow<Boolean> get() = personalizedSuggestionsFlow
    override val emojiPredictionEnabled: Flow<Boolean> get() = emojiFlow
    override val incognito: Flow<Boolean> get() = incognitoFlow
    override val learningThreshold: Flow<Int> get() = thresholdFlow
}
