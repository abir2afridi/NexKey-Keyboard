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

        // Preference Settings
        private val KEY_DOUBLE_SPACE_TAB = booleanPreferencesKey("double_space_tab")
        private val KEY_CLIPBOARD_RECENT = booleanPreferencesKey("clipboard_recent")
        private val KEY_CLIPBOARD_EXPIRY = intPreferencesKey("clipboard_expiry")
        private val KEY_CLIPBOARD_IMAGES = booleanPreferencesKey("clipboard_images")
        private val KEY_POPUP_ON_KEYPRESS = booleanPreferencesKey("popup_on_keypress")
        private val KEY_VOICE_INPUT_KEY = booleanPreferencesKey("voice_input_key")
        private val KEY_SHOW_EMOJI_KEY = booleanPreferencesKey("show_emoji_key")
        private val KEY_SHOW_GLOBE_KEY = booleanPreferencesKey("show_globe_key")
        private val KEY_ALLOW_OTHER_KEYBOARDS = booleanPreferencesKey("allow_other_keyboards")
        private val KEY_MOVE_CURSOR_SPACE = booleanPreferencesKey("move_cursor_space")
        private val KEY_VOLUME_CURSOR = booleanPreferencesKey("volume_cursor")
        private val KEY_SMART_VOLUME_CONTROL = booleanPreferencesKey("smart_volume_control")

        // Appearance & Layouts
        private val KEY_LARGE_NUMBER_ROW = booleanPreferencesKey("large_number_row")
        private val KEY_HIDE_LONG_PRESS_HINTS = booleanPreferencesKey("hide_long_press_hints")
        private val KEY_ENABLE_KB_RESIZING = booleanPreferencesKey("enable_kb_resizing")
        private val KEY_KB_HEIGHT_PORTRAIT = intPreferencesKey("kb_height_portrait")
        private val KEY_KB_HEIGHT_LANDSCAPE = intPreferencesKey("kb_height_landscape")
        private val KEY_ONE_HANDED_WIDTH_PORTRAIT = intPreferencesKey("one_handed_width_portrait")
        private val KEY_ONE_HANDED_WIDTH_LANDSCAPE = intPreferencesKey("one_handed_width_landscape")
        private val KEY_SPLIT_KEYBOARD = booleanPreferencesKey("split_keyboard")
        private val KEY_FORCED_ENTER = booleanPreferencesKey("forced_enter")

        // Text Correction
        private val KEY_BLOCK_OFFENSIVE = booleanPreferencesKey("block_offensive")
        private val KEY_AUTO_CORRECTION = booleanPreferencesKey("auto_correction")
        private val KEY_PHONETIC_AUTO_CORRECTION = booleanPreferencesKey("phonetic_auto_correction")
        private val KEY_SHOW_SUGGESTIONS = booleanPreferencesKey("show_suggestions")
        private val KEY_PERSONALIZED_SUGGESTIONS = booleanPreferencesKey("personalized_suggestions")
        private val KEY_NEXT_WORD_SUGGESTIONS = booleanPreferencesKey("next_word_suggestions")

        // Advanced
        private val KEY_POPUP_DISMISS_DELAY = stringPreferencesKey("popup_dismiss_delay")
        private val KEY_VIBRATION_DURATION = intPreferencesKey("vibration_duration")
        private val KEY_LONG_PRESS_DELAY_MS = intPreferencesKey("long_press_delay_ms")
        private val KEY_SPACE_CURSOR_DELAY = intPreferencesKey("space_cursor_delay")
        private val KEY_SPACE_CURSOR_SPEED = intPreferencesKey("space_cursor_speed")
        private val KEY_PHYSICAL_KB_EMOJI = booleanPreferencesKey("physical_kb_emoji")
        private val KEY_SHOW_TYPED_WORD_FIRST = booleanPreferencesKey("show_typed_word_first")
        private val KEY_VOICE_TYPING_ENGINE = stringPreferencesKey("voice_typing_engine")

        // Gif Quality
        private val KEY_HIGH_QUALITY_GIFS = booleanPreferencesKey("high_quality_gifs")
        private val KEY_SEND_HIGH_QUALITY_GIFS = booleanPreferencesKey("send_high_quality_gifs")
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

    // New Flows
    val doubleSpaceTab: Flow<Boolean> = context.dataStore.data.map { it[KEY_DOUBLE_SPACE_TAB] ?: false }
    val clipboardRecent: Flow<Boolean> = context.dataStore.data.map { it[KEY_CLIPBOARD_RECENT] ?: true }
    val clipboardExpiry: Flow<Int> = context.dataStore.data.map { it[KEY_CLIPBOARD_EXPIRY] ?: 120 }
    val clipboardImages: Flow<Boolean> = context.dataStore.data.map { it[KEY_CLIPBOARD_IMAGES] ?: true }
    val popupOnKeypress: Flow<Boolean> = context.dataStore.data.map { it[KEY_POPUP_ON_KEYPRESS] ?: true }
    val voiceInputKey: Flow<Boolean> = context.dataStore.data.map { it[KEY_VOICE_INPUT_KEY] ?: true }
    val showEmojiKey: Flow<Boolean> = context.dataStore.data.map { it[KEY_SHOW_EMOJI_KEY] ?: true }
    val showGlobeKey: Flow<Boolean> = context.dataStore.data.map { it[KEY_SHOW_GLOBE_KEY] ?: true }
    val allowOtherKeyboards: Flow<Boolean> = context.dataStore.data.map { it[KEY_ALLOW_OTHER_KEYBOARDS] ?: true }
    val moveCursorSpace: Flow<Boolean> = context.dataStore.data.map { it[KEY_MOVE_CURSOR_SPACE] ?: true }
    val volumeCursor: Flow<Boolean> = context.dataStore.data.map { it[KEY_VOLUME_CURSOR] ?: false }
    val smartVolumeControl: Flow<Boolean> = context.dataStore.data.map { it[KEY_SMART_VOLUME_CONTROL] ?: true }

    val largeNumberRow: Flow<Boolean> = context.dataStore.data.map { it[KEY_LARGE_NUMBER_ROW] ?: false }
    val hideLongPressHints: Flow<Boolean> = context.dataStore.data.map { it[KEY_HIDE_LONG_PRESS_HINTS] ?: false }
    val enableKbResizing: Flow<Boolean> = context.dataStore.data.map { it[KEY_ENABLE_KB_RESIZING] ?: false }
    val kbHeightPortrait: Flow<Int> = context.dataStore.data.map { it[KEY_KB_HEIGHT_PORTRAIT] ?: 100 }
    val kbHeightLandscape: Flow<Int> = context.dataStore.data.map { it[KEY_KB_HEIGHT_LANDSCAPE] ?: 100 }
    val oneHandedWidthPortrait: Flow<Int> = context.dataStore.data.map { it[KEY_ONE_HANDED_WIDTH_PORTRAIT] ?: 100 }
    val oneHandedWidthLandscape: Flow<Int> = context.dataStore.data.map { it[KEY_ONE_HANDED_WIDTH_LANDSCAPE] ?: 100 }
    val splitKeyboard: Flow<Boolean> = context.dataStore.data.map { it[KEY_SPLIT_KEYBOARD] ?: false }
    val forcedEnter: Flow<Boolean> = context.dataStore.data.map { it[KEY_FORCED_ENTER] ?: false }

    val blockOffensive: Flow<Boolean> = context.dataStore.data.map { it[KEY_BLOCK_OFFENSIVE] ?: true }
    val autoCorrection: Flow<Boolean> = context.dataStore.data.map { it[KEY_AUTO_CORRECTION] ?: true }
    val phoneticAutoCorrection: Flow<Boolean> = context.dataStore.data.map { it[KEY_PHONETIC_AUTO_CORRECTION] ?: true }
    val showSuggestions: Flow<Boolean> = context.dataStore.data.map { it[KEY_SHOW_SUGGESTIONS] ?: true }
    val personalizedSuggestions: Flow<Boolean> = context.dataStore.data.map { it[KEY_PERSONALIZED_SUGGESTIONS] ?: true }
    val nextWordSuggestions: Flow<Boolean> = context.dataStore.data.map { it[KEY_NEXT_WORD_SUGGESTIONS] ?: true }

    val popupDismissDelay: Flow<String> = context.dataStore.data.map { it[KEY_POPUP_DISMISS_DELAY] ?: "Default" }
    val vibrationDuration: Flow<Int> = context.dataStore.data.map { it[KEY_VIBRATION_DURATION] ?: 0 } // 0 means system default
    val longPressDelayMs: Flow<Int> = context.dataStore.data.map { it[KEY_LONG_PRESS_DELAY_MS] ?: 300 }
    val spaceCursorDelay: Flow<Int> = context.dataStore.data.map { it[KEY_SPACE_CURSOR_DELAY] ?: 1000 }
    val spaceCursorSpeed: Flow<Int> = context.dataStore.data.map { it[KEY_SPACE_CURSOR_SPEED] ?: 150 }
    val physicalKbEmoji: Flow<Boolean> = context.dataStore.data.map { it[KEY_PHYSICAL_KB_EMOJI] ?: true }
    val showTypedWordFirst: Flow<Boolean> = context.dataStore.data.map { it[KEY_SHOW_TYPED_WORD_FIRST] ?: true }
    val voiceTypingEngine: Flow<String> = context.dataStore.data.map { it[KEY_VOICE_TYPING_ENGINE] ?: "Default" }

    val highQualityGifs: Flow<Boolean> = context.dataStore.data.map { it[KEY_HIGH_QUALITY_GIFS] ?: true }
    val sendHighQualityGifs: Flow<Boolean> = context.dataStore.data.map { it[KEY_SEND_HIGH_QUALITY_GIFS] ?: true }

    // Setters
    suspend fun setDoubleSpaceTab(enabled: Boolean) = context.dataStore.edit { it[KEY_DOUBLE_SPACE_TAB] = enabled }
    suspend fun setClipboardRecent(enabled: Boolean) = context.dataStore.edit { it[KEY_CLIPBOARD_RECENT] = enabled }
    suspend fun setClipboardExpiry(mins: Int) = context.dataStore.edit { it[KEY_CLIPBOARD_EXPIRY] = mins }
    suspend fun setClipboardImages(enabled: Boolean) = context.dataStore.edit { it[KEY_CLIPBOARD_IMAGES] = enabled }
    suspend fun setPopupOnKeypress(enabled: Boolean) = context.dataStore.edit { it[KEY_POPUP_ON_KEYPRESS] = enabled }
    suspend fun setVoiceInputKey(enabled: Boolean) = context.dataStore.edit { it[KEY_VOICE_INPUT_KEY] = enabled }
    suspend fun setShowEmojiKey(enabled: Boolean) = context.dataStore.edit { it[KEY_SHOW_EMOJI_KEY] = enabled }
    suspend fun setShowGlobeKey(enabled: Boolean) = context.dataStore.edit { it[KEY_SHOW_GLOBE_KEY] = enabled }
    suspend fun setAllowOtherKeyboards(enabled: Boolean) = context.dataStore.edit { it[KEY_ALLOW_OTHER_KEYBOARDS] = enabled }
    suspend fun setMoveCursorSpace(enabled: Boolean) = context.dataStore.edit { it[KEY_MOVE_CURSOR_SPACE] = enabled }
    suspend fun setVolumeCursor(enabled: Boolean) = context.dataStore.edit { it[KEY_VOLUME_CURSOR] = enabled }
    suspend fun setSmartVolumeControl(enabled: Boolean) = context.dataStore.edit { it[KEY_SMART_VOLUME_CONTROL] = enabled }

    suspend fun setLargeNumberRow(enabled: Boolean) = context.dataStore.edit { it[KEY_LARGE_NUMBER_ROW] = enabled }
    suspend fun setHideLongPressHints(enabled: Boolean) = context.dataStore.edit { it[KEY_HIDE_LONG_PRESS_HINTS] = enabled }
    suspend fun setEnableKbResizing(enabled: Boolean) = context.dataStore.edit { it[KEY_ENABLE_KB_RESIZING] = enabled }
    suspend fun setKbHeightPortrait(percentage: Int) = context.dataStore.edit { it[KEY_KB_HEIGHT_PORTRAIT] = percentage }
    suspend fun setKbHeightLandscape(percentage: Int) = context.dataStore.edit { it[KEY_KB_HEIGHT_LANDSCAPE] = percentage }
    suspend fun setOneHandedWidthPortrait(percentage: Int) = context.dataStore.edit { it[KEY_ONE_HANDED_WIDTH_PORTRAIT] = percentage }
    suspend fun setOneHandedWidthLandscape(percentage: Int) = context.dataStore.edit { it[KEY_ONE_HANDED_WIDTH_LANDSCAPE] = percentage }
    suspend fun setSplitKeyboard(enabled: Boolean) = context.dataStore.edit { it[KEY_SPLIT_KEYBOARD] = enabled }
    suspend fun setForcedEnter(enabled: Boolean) = context.dataStore.edit { it[KEY_FORCED_ENTER] = enabled }

    suspend fun setBlockOffensive(enabled: Boolean) = context.dataStore.edit { it[KEY_BLOCK_OFFENSIVE] = enabled }
    suspend fun setAutoCorrection(enabled: Boolean) = context.dataStore.edit { it[KEY_AUTO_CORRECTION] = enabled }
    suspend fun setPhoneticAutoCorrection(enabled: Boolean) = context.dataStore.edit { it[KEY_PHONETIC_AUTO_CORRECTION] = enabled }
    suspend fun setShowSuggestions(enabled: Boolean) = context.dataStore.edit { it[KEY_SHOW_SUGGESTIONS] = enabled }
    suspend fun setPersonalizedSuggestions(enabled: Boolean) = context.dataStore.edit { it[KEY_PERSONALIZED_SUGGESTIONS] = enabled }
    suspend fun setNextWordSuggestions(enabled: Boolean) = context.dataStore.edit { it[KEY_NEXT_WORD_SUGGESTIONS] = enabled }

    suspend fun setPopupDismissDelay(delay: String) = context.dataStore.edit { it[KEY_POPUP_DISMISS_DELAY] = delay }
    suspend fun setVibrationDuration(duration: Int) = context.dataStore.edit { it[KEY_VIBRATION_DURATION] = duration }
    suspend fun setLongPressDelayMs(delay: Int) = context.dataStore.edit { it[KEY_LONG_PRESS_DELAY_MS] = delay }
    suspend fun setSpaceCursorDelay(delay: Int) = context.dataStore.edit { it[KEY_SPACE_CURSOR_DELAY] = delay }
    suspend fun setSpaceCursorSpeed(speed: Int) = context.dataStore.edit { it[KEY_SPACE_CURSOR_SPEED] = speed }
    suspend fun setPhysicalKbEmoji(enabled: Boolean) = context.dataStore.edit { it[KEY_PHYSICAL_KB_EMOJI] = enabled }
    suspend fun setShowTypedWordFirst(enabled: Boolean) = context.dataStore.edit { it[KEY_SHOW_TYPED_WORD_FIRST] = enabled }
    suspend fun setVoiceTypingEngine(engine: String) = context.dataStore.edit { it[KEY_VOICE_TYPING_ENGINE] = engine }

    suspend fun setHighQualityGifs(enabled: Boolean) = context.dataStore.edit { it[KEY_HIGH_QUALITY_GIFS] = enabled }
    suspend fun setSendHighQualityGifs(enabled: Boolean) = context.dataStore.edit { it[KEY_SEND_HIGH_QUALITY_GIFS] = enabled }

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
