package com.example.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.theme.ThemePreset
import com.example.ui.KeyboardMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "nexkey_preferences")

class UserPreferences(private val context: Context) {

    companion object {
        private val KEY_THEME = stringPreferencesKey("theme")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_KEY_HEIGHT = intPreferencesKey("key_height")
        private val KEY_KEY_RADIUS = intPreferencesKey("key_radius")
        private val KEY_HAPTICS = booleanPreferencesKey("enable_haptics")
        private val KEY_SOUND = booleanPreferencesKey("enable_sound")
        private val KEY_INCOGNITO = booleanPreferencesKey("incognito_mode")
        private val KEY_SMART_PUNCTUATION = booleanPreferencesKey("smart_punctuation")
        private val KEY_AUTO_CAP = booleanPreferencesKey("auto_capitalize")
        private val KEY_SHOW_NUMBER_ROW = booleanPreferencesKey("show_number_row")
        private val KEY_HAPTIC_INTENSITY = intPreferencesKey("haptic_intensity")
        private val KEY_SOUND_VOLUME = intPreferencesKey("sound_volume")
        private val KEY_TOTAL_WORDS = intPreferencesKey("total_words_typed")
        private val KEY_TOTAL_CHARS = intPreferencesKey("total_chars_typed")
        private val KEY_APP_THEME = stringPreferencesKey("app_theme")
    }

    val theme: Flow<String> = context.dataStore.data.map { it[KEY_THEME] ?: ThemePreset.DARK_NEON.name }
    val language: Flow<String> = context.dataStore.data.map { it[KEY_LANGUAGE] ?: KeyboardMode.BANGLA_PHONETIC.name }
    val keyHeight: Flow<Int> = context.dataStore.data.map { it[KEY_KEY_HEIGHT] ?: 54 }
    val keyRadius: Flow<Int> = context.dataStore.data.map { it[KEY_KEY_RADIUS] ?: 10 }
    val haptics: Flow<Boolean> = context.dataStore.data.map { it[KEY_HAPTICS] ?: true }
    val sound: Flow<Boolean> = context.dataStore.data.map { it[KEY_SOUND] ?: true }
    val incognito: Flow<Boolean> = context.dataStore.data.map { it[KEY_INCOGNITO] ?: false }
    val smartPunctuation: Flow<Boolean> = context.dataStore.data.map { it[KEY_SMART_PUNCTUATION] ?: true }
    val autoCapitalize: Flow<Boolean> = context.dataStore.data.map { it[KEY_AUTO_CAP] ?: true }
    val showNumberRow: Flow<Boolean> = context.dataStore.data.map { it[KEY_SHOW_NUMBER_ROW] ?: false }
    val hapticIntensity: Flow<Int> = context.dataStore.data.map { it[KEY_HAPTIC_INTENSITY] ?: 50 }
    val soundVolume: Flow<Int> = context.dataStore.data.map { it[KEY_SOUND_VOLUME] ?: 50 }
    val totalWords: Flow<Int> = context.dataStore.data.map { it[KEY_TOTAL_WORDS] ?: 0 }
    val totalChars: Flow<Int> = context.dataStore.data.map { it[KEY_TOTAL_CHARS] ?: 0 }
    val appTheme: Flow<String> = context.dataStore.data.map { it[KEY_APP_THEME] ?: "SYSTEM" }

    suspend fun setTheme(theme: ThemePreset) {
        context.dataStore.edit { it[KEY_THEME] = theme.name }
    }

    suspend fun setLanguage(language: KeyboardMode) {
        context.dataStore.edit { it[KEY_LANGUAGE] = language.name }
    }

    suspend fun setKeyHeight(height: Int) {
        context.dataStore.edit { it[KEY_KEY_HEIGHT] = height }
    }

    suspend fun setKeyRadius(radius: Int) {
        context.dataStore.edit { it[KEY_KEY_RADIUS] = radius }
    }

    suspend fun setHaptics(enabled: Boolean) {
        context.dataStore.edit { it[KEY_HAPTICS] = enabled }
    }

    suspend fun setSound(enabled: Boolean) {
        context.dataStore.edit { it[KEY_SOUND] = enabled }
    }

    suspend fun setIncognito(enabled: Boolean) {
        context.dataStore.edit { it[KEY_INCOGNITO] = enabled }
    }

    suspend fun setSmartPunctuation(enabled: Boolean) {
        context.dataStore.edit { it[KEY_SMART_PUNCTUATION] = enabled }
    }

    suspend fun setAutoCapitalize(enabled: Boolean) {
        context.dataStore.edit { it[KEY_AUTO_CAP] = enabled }
    }

    suspend fun setShowNumberRow(show: Boolean) {
        context.dataStore.edit { it[KEY_SHOW_NUMBER_ROW] = show }
    }

    suspend fun setHapticIntensity(intensity: Int) {
        context.dataStore.edit { it[KEY_HAPTIC_INTENSITY] = intensity }
    }

    suspend fun setSoundVolume(volume: Int) {
        context.dataStore.edit { it[KEY_SOUND_VOLUME] = volume }
    }

    suspend fun incrementStats(words: Int, chars: Int) {
        context.dataStore.edit {
            val currentWords = it[KEY_TOTAL_WORDS] ?: 0
            val currentChars = it[KEY_TOTAL_CHARS] ?: 0
            it[KEY_TOTAL_WORDS] = currentWords + words
            it[KEY_TOTAL_CHARS] = currentChars + chars
        }
    }

    suspend fun setAppTheme(theme: String) {
        context.dataStore.edit { it[KEY_APP_THEME] = theme }
    }
}
