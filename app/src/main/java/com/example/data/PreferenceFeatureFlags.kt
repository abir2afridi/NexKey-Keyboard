package com.example.data

import com.example.prediction.FeatureFlags
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Maps the prediction module's live feature flags onto the app's DataStore-backed
 * [UserPreferences]. Every read goes through a Flow, so a Settings change takes
 * effect on the next keystroke without restarting the IME.
 */
class PreferenceFeatureFlags(private val prefs: UserPreferences) : FeatureFlags {

    override val autoCorrectionEnabled: Flow<Boolean> get() = prefs.autoCorrection

    override val typoCorrectionEnabled: Flow<Boolean> get() = prefs.phoneticAutoCorrection

    override val nextWordPredictionEnabled: Flow<Boolean> get() = prefs.nextWordSuggestions

    override val personalLearningEnabled: Flow<Boolean> get() = prefs.personalizedSuggestions

    override val personalizedSuggestionsEnabled: Flow<Boolean> get() = prefs.personalizedSuggestions

    // No dedicated "emoji prediction" toggle in Settings; strip emoji suggestions
    // follow the emoji feature being available (always on for now).
    override val emojiPredictionEnabled: Flow<Boolean> get() = flowOf(true)

    override val incognito: Flow<Boolean> get() = prefs.incognito

    override val learningThreshold: Flow<Int> get() = flowOf(3)
}
