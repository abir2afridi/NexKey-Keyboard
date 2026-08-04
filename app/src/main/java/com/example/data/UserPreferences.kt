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
        private val KEY_APP_LANGUAGE = stringPreferencesKey("app_language")
        private val KEY_NAV_STYLE = stringPreferencesKey("nav_style")
        private val KEY_ACCENT_COLOR = stringPreferencesKey("accent_color")

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

        // Emoji
        private val KEY_RECENT_EMOJIS = stringPreferencesKey("recent_emojis")
        private val KEY_RECENT_EMOJI_EXPIRY = intPreferencesKey("recent_emoji_expiry")
        private val KEY_EMOJI_SEARCH_VISIBLE_ROWS = intPreferencesKey("emoji_search_visible_rows")
        private val KEY_EMOJI_SEARCH_HORIZONTAL = booleanPreferencesKey("emoji_search_horizontal")

        // Gif Quality
        private val KEY_HIGH_QUALITY_GIFS = booleanPreferencesKey("high_quality_gifs")
        private val KEY_SEND_HIGH_QUALITY_GIFS = booleanPreferencesKey("send_high_quality_gifs")

        // Hold to Paste
        private val KEY_HOLD_PASTE_ENABLED = booleanPreferencesKey("hold_paste_enabled")
        private val KEY_HOLD_PASTE_DURATION = intPreferencesKey("hold_paste_duration")
        private val KEY_HOLD_PASTE_TRIGGER_KEY = stringPreferencesKey("hold_paste_trigger_key")

        // Language enable/disable
        private val KEY_ENABLE_BANGLA_PHONETIC = booleanPreferencesKey("enable_bangla_phonetic")
        private val KEY_ENABLE_BANGLA_JATIYO = booleanPreferencesKey("enable_bangla_jatiyo")
        private val KEY_ENABLE_AVRO = booleanPreferencesKey("enable_avro")
        private val KEY_ENABLE_ARABIC = booleanPreferencesKey("enable_arabic")

        // Suggestion & Toolbar
        private val KEY_ALWAYS_SHOW_SUGGESTIONS = booleanPreferencesKey("always_show_suggestions")
        private val KEY_UNIFIED_HEADER = booleanPreferencesKey("unified_header")
        private val KEY_TOOLBAR_AUTO_SHOW_DELAY = intPreferencesKey("toolbar_auto_show_delay")
        private val KEY_HEADER_ANIMATION = stringPreferencesKey("header_animation")

        // Backspace repeat
        private val KEY_BACKSPACE_REPEAT_DELAY = intPreferencesKey("backspace_repeat_delay")
        private val KEY_BACKSPACE_REPEAT_SPEED = intPreferencesKey("backspace_repeat_speed")
        private val KEY_METER_THEME = stringPreferencesKey("meter_theme")
        private val KEY_METER_FONT = stringPreferencesKey("meter_font")
        private val KEY_METER_IDLE_MS = intPreferencesKey("meter_idle_ms")
        private val KEY_METER_INTERVAL = stringPreferencesKey("meter_interval")
        private val KEY_METER_ENABLED = booleanPreferencesKey("meter_enabled")
        private val KEY_METER_POSITION = stringPreferencesKey("meter_position")
        private val KEY_METER_DISPLAY_MODE = stringPreferencesKey("meter_display_mode")
        private val KEY_METER_COUNT_MODE = stringPreferencesKey("meter_count_mode")

        // Info box (second box frame next to the speed meter)
        private val KEY_INFOBOX_FRAME = stringPreferencesKey("infobox_frame")
        private val KEY_INFOBOX_TEXT_COLOR = stringPreferencesKey("infobox_text_color")
        private val KEY_INFOBOX_INFO_COLOR = stringPreferencesKey("infobox_info_color")
        private val KEY_INFOBOX_CUSTOM_TEXTS = stringPreferencesKey("infobox_custom_texts")
        private val KEY_INFOBOX_CUSTOM_MODE = stringPreferencesKey("infobox_custom_mode")
        private val KEY_INFOBOX_CUSTOM_SEC = intPreferencesKey("infobox_custom_sec")
        private val KEY_INFOBOX_SWIPE_TIMEOUT_SEC = intPreferencesKey("infobox_swipe_timeout_sec")
        private val KEY_INFOBOX_ENABLED = booleanPreferencesKey("infobox_enabled")
        private val KEY_INFOBOX_CUSTOM_TEXT_COLOR = stringPreferencesKey("infobox_custom_text_color")
        private val KEY_INFOBOX_FONT = stringPreferencesKey("infobox_font")

        // Custom Theme Colors
        private val KEY_CUSTOM_BG_COLOR = stringPreferencesKey("custom_bg_color")
        private val KEY_CUSTOM_KEY_BG_COLOR = stringPreferencesKey("custom_key_bg_color")
        private val KEY_CUSTOM_KEY_SPECIAL_COLOR = stringPreferencesKey("custom_key_special_color")
        private val KEY_CUSTOM_KEY_TEXT_COLOR = stringPreferencesKey("custom_key_text_color")
        private val KEY_CUSTOM_KEY_SPECIAL_TEXT_COLOR = stringPreferencesKey("custom_key_special_text_color")
        private val KEY_CUSTOM_ACCENT_COLOR = stringPreferencesKey("custom_accent_color")
        private val KEY_CUSTOM_SUGGESTION_BG_COLOR = stringPreferencesKey("custom_suggestion_bg_color")
        private val KEY_CUSTOM_SUGGESTION_TEXT_COLOR = stringPreferencesKey("custom_suggestion_text_color")
        private val KEY_CUSTOM_POPUP_BG_COLOR = stringPreferencesKey("custom_popup_bg_color")
        private val KEY_CUSTOM_POPUP_TEXT_COLOR = stringPreferencesKey("custom_popup_text_color")
        private val KEY_CUSTOM_KEY_HINT_COLOR = stringPreferencesKey("custom_key_hint_color")
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
    val appLanguage: Flow<String> = context.dataStore.data.map { it[KEY_APP_LANGUAGE] ?: "en" }
    val navigationStyle: Flow<String> = context.dataStore.data.map { it[KEY_NAV_STYLE] ?: "STANDARD" }
    val accentColor: Flow<String> = context.dataStore.data.map { it[KEY_ACCENT_COLOR] ?: "#FF2E7D32" }

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

    val recentEmojis: Flow<String> = context.dataStore.data.map { it[KEY_RECENT_EMOJIS] ?: "[]" }
    val recentEmojiExpiry: Flow<Int> = context.dataStore.data.map { it[KEY_RECENT_EMOJI_EXPIRY] ?: 30 }
    val emojiSearchVisibleRows: Flow<Int> = context.dataStore.data.map { it[KEY_EMOJI_SEARCH_VISIBLE_ROWS] ?: 2 }
    val emojiSearchHorizontal: Flow<Boolean> = context.dataStore.data.map { it[KEY_EMOJI_SEARCH_HORIZONTAL] ?: true }

    val highQualityGifs: Flow<Boolean> = context.dataStore.data.map { it[KEY_HIGH_QUALITY_GIFS] ?: true }
    val sendHighQualityGifs: Flow<Boolean> = context.dataStore.data.map { it[KEY_SEND_HIGH_QUALITY_GIFS] ?: true }

    val holdPasteEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_HOLD_PASTE_ENABLED] ?: false }
    val holdPasteDuration: Flow<Int> = context.dataStore.data.map { it[KEY_HOLD_PASTE_DURATION] ?: 400 }
    val holdPasteTriggerKey: Flow<String> = context.dataStore.data.map { it[KEY_HOLD_PASTE_TRIGGER_KEY] ?: "v" }

    val enableBanglaPhonetic: Flow<Boolean> = context.dataStore.data.map { it[KEY_ENABLE_BANGLA_PHONETIC] ?: true }
    val enableBanglaJatiyo: Flow<Boolean> = context.dataStore.data.map { it[KEY_ENABLE_BANGLA_JATIYO] ?: true }
    val enableAvro: Flow<Boolean> = context.dataStore.data.map { it[KEY_ENABLE_AVRO] ?: true }
    val enableArabic: Flow<Boolean> = context.dataStore.data.map { it[KEY_ENABLE_ARABIC] ?: true }

    val alwaysShowSuggestions: Flow<Boolean> = context.dataStore.data.map { it[KEY_ALWAYS_SHOW_SUGGESTIONS] ?: true }
    val unifiedHeader: Flow<Boolean> = context.dataStore.data.map { it[KEY_UNIFIED_HEADER] ?: false }
    val toolbarAutoShowDelay: Flow<Int> = context.dataStore.data.map { it[KEY_TOOLBAR_AUTO_SHOW_DELAY] ?: 10 }
    val headerAnimation: Flow<String> = context.dataStore.data.map { it[KEY_HEADER_ANIMATION] ?: "FADE" }

    val backspaceRepeatDelay: Flow<Int> = context.dataStore.data.map { it[KEY_BACKSPACE_REPEAT_DELAY] ?: 400 }
    val backspaceRepeatSpeed: Flow<Int> = context.dataStore.data.map { it[KEY_BACKSPACE_REPEAT_SPEED] ?: 50 }
    val meterTheme: Flow<String> = context.dataStore.data.map { it[KEY_METER_THEME] ?: "CALCULATOR" }
    val meterFont: Flow<String> = context.dataStore.data.map { it[KEY_METER_FONT] ?: "DIGITAL" }
    val meterIdleMs: Flow<Int> = context.dataStore.data.map { it[KEY_METER_IDLE_MS] ?: 5000 }
    val meterInterval: Flow<String> = context.dataStore.data.map { it[KEY_METER_INTERVAL] ?: "5s" }
    val meterEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_METER_ENABLED] ?: true }
    val meterPosition: Flow<String> = context.dataStore.data.map { it[KEY_METER_POSITION] ?: "right" }
    val infoBoxFrame: Flow<String> = context.dataStore.data.map { it[KEY_INFOBOX_FRAME] ?: "CLASSIC" }
    val infoBoxTextColor: Flow<String> = context.dataStore.data.map { it[KEY_INFOBOX_TEXT_COLOR] ?: "#00FF41" }
    val infoBoxInfoColor: Flow<String> = context.dataStore.data.map { it[KEY_INFOBOX_INFO_COLOR] ?: "#00FF41" }
    val infoBoxCustomTextColor: Flow<String> = context.dataStore.data.map { it[KEY_INFOBOX_CUSTOM_TEXT_COLOR] ?: "#FFFFFF" }
    val infoBoxCustomTexts: Flow<String> = context.dataStore.data.map { it[KEY_INFOBOX_CUSTOM_TEXTS] ?: "[]" }
    val infoBoxCustomMode: Flow<String> = context.dataStore.data.map { it[KEY_INFOBOX_CUSTOM_MODE] ?: "off" }
    val infoBoxCustomSec: Flow<Int> = context.dataStore.data.map { it[KEY_INFOBOX_CUSTOM_SEC] ?: 5 }
    val infoBoxSwipeTimeoutSec: Flow<Int> = context.dataStore.data.map { it[KEY_INFOBOX_SWIPE_TIMEOUT_SEC] ?: 10 }
    val infoBoxEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_INFOBOX_ENABLED] ?: true }
    val infoBoxFont: Flow<String> = context.dataStore.data.map { it[KEY_INFOBOX_FONT] ?: "DEFAULT" }
    val meterDisplayMode: Flow<String> = context.dataStore.data.map { it[KEY_METER_DISPLAY_MODE] ?: "speed" }
    val meterCountMode: Flow<String> = context.dataStore.data.map { it[KEY_METER_COUNT_MODE] ?: "keys" }

    // Custom Theme Flows
    val customBgColor: Flow<String> = context.dataStore.data.map { it[KEY_CUSTOM_BG_COLOR] ?: "#FF12131C" }
    val customKeyBgColor: Flow<String> = context.dataStore.data.map { it[KEY_CUSTOM_KEY_BG_COLOR] ?: "#FF1E2136" }
    val customKeySpecialColor: Flow<String> = context.dataStore.data.map { it[KEY_CUSTOM_KEY_SPECIAL_COLOR] ?: "#FF2A2E4B" }
    val customKeyTextColor: Flow<String> = context.dataStore.data.map { it[KEY_CUSTOM_KEY_TEXT_COLOR] ?: "#FFF1F3FB" }
    val customKeySpecialTextColor: Flow<String> = context.dataStore.data.map { it[KEY_CUSTOM_KEY_SPECIAL_TEXT_COLOR] ?: "#FF80D8FF" }
    val customAccentColor: Flow<String> = context.dataStore.data.map { it[KEY_CUSTOM_ACCENT_COLOR] ?: "#FF00E5FF" }
    val customSuggestionBgColor: Flow<String> = context.dataStore.data.map { it[KEY_CUSTOM_SUGGESTION_BG_COLOR] ?: "#FF1A1C29" }
    val customSuggestionTextColor: Flow<String> = context.dataStore.data.map { it[KEY_CUSTOM_SUGGESTION_TEXT_COLOR] ?: "#FFF1F3FB" }
    val customPopupBgColor: Flow<String> = context.dataStore.data.map { it[KEY_CUSTOM_POPUP_BG_COLOR] ?: "#FF2A2E4B" }
    val customPopupTextColor: Flow<String> = context.dataStore.data.map { it[KEY_CUSTOM_POPUP_TEXT_COLOR] ?: "#FF00E5FF" }
    val customKeyHintColor: Flow<String> = context.dataStore.data.map { it[KEY_CUSTOM_KEY_HINT_COLOR] ?: "#66F1F3FB" }

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

    suspend fun setRecentEmojis(json: String) = context.dataStore.edit { it[KEY_RECENT_EMOJIS] = json }
    suspend fun setRecentEmojiExpiry(days: Int) = context.dataStore.edit { it[KEY_RECENT_EMOJI_EXPIRY] = days }
    suspend fun setEmojiSearchVisibleRows(rows: Int) = context.dataStore.edit { it[KEY_EMOJI_SEARCH_VISIBLE_ROWS] = rows }
    suspend fun setEmojiSearchHorizontal(horizontal: Boolean) = context.dataStore.edit { it[KEY_EMOJI_SEARCH_HORIZONTAL] = horizontal }

    suspend fun setHighQualityGifs(enabled: Boolean) = context.dataStore.edit { it[KEY_HIGH_QUALITY_GIFS] = enabled }
    suspend fun setSendHighQualityGifs(enabled: Boolean) = context.dataStore.edit { it[KEY_SEND_HIGH_QUALITY_GIFS] = enabled }

    suspend fun setHoldPasteEnabled(enabled: Boolean) = context.dataStore.edit { it[KEY_HOLD_PASTE_ENABLED] = enabled }
    suspend fun setHoldPasteDuration(durationMs: Int) = context.dataStore.edit { it[KEY_HOLD_PASTE_DURATION] = durationMs }
    suspend fun setHoldPasteTriggerKey(key: String) = context.dataStore.edit { it[KEY_HOLD_PASTE_TRIGGER_KEY] = key }

    suspend fun setEnableBanglaPhonetic(enabled: Boolean) = context.dataStore.edit { it[KEY_ENABLE_BANGLA_PHONETIC] = enabled }
    suspend fun setEnableBanglaJatiyo(enabled: Boolean) = context.dataStore.edit { it[KEY_ENABLE_BANGLA_JATIYO] = enabled }
    suspend fun setEnableAvro(enabled: Boolean) = context.dataStore.edit { it[KEY_ENABLE_AVRO] = enabled }
    suspend fun setEnableArabic(enabled: Boolean) = context.dataStore.edit { it[KEY_ENABLE_ARABIC] = enabled }

    suspend fun setAlwaysShowSuggestions(enabled: Boolean) = context.dataStore.edit { it[KEY_ALWAYS_SHOW_SUGGESTIONS] = enabled }
    suspend fun setUnifiedHeader(enabled: Boolean) = context.dataStore.edit { it[KEY_UNIFIED_HEADER] = enabled }
    suspend fun setToolbarAutoShowDelay(seconds: Int) = context.dataStore.edit { it[KEY_TOOLBAR_AUTO_SHOW_DELAY] = seconds }
    suspend fun setHeaderAnimation(name: String) = context.dataStore.edit { it[KEY_HEADER_ANIMATION] = name }

    suspend fun setBackspaceRepeatDelay(ms: Int) = context.dataStore.edit { it[KEY_BACKSPACE_REPEAT_DELAY] = ms }
    suspend fun setBackspaceRepeatSpeed(ms: Int) = context.dataStore.edit { it[KEY_BACKSPACE_REPEAT_SPEED] = ms }
    suspend fun setMeterTheme(themeName: String) = context.dataStore.edit { it[KEY_METER_THEME] = themeName }
    suspend fun setMeterFont(fontName: String) = context.dataStore.edit { it[KEY_METER_FONT] = fontName }
    suspend fun setMeterIdleMs(ms: Int) = context.dataStore.edit { it[KEY_METER_IDLE_MS] = ms }
    suspend fun setMeterInterval(interval: String) = context.dataStore.edit { it[KEY_METER_INTERVAL] = interval }
    suspend fun setMeterEnabled(enabled: Boolean) = context.dataStore.edit { it[KEY_METER_ENABLED] = enabled }
    suspend fun setMeterPosition(position: String) = context.dataStore.edit { it[KEY_METER_POSITION] = position }
    suspend fun setInfoBoxFrame(frame: String) = context.dataStore.edit { it[KEY_INFOBOX_FRAME] = frame }
    suspend fun setInfoBoxTextColor(color: String) = context.dataStore.edit { it[KEY_INFOBOX_TEXT_COLOR] = color }
    suspend fun setInfoBoxInfoColor(color: String) = context.dataStore.edit { it[KEY_INFOBOX_INFO_COLOR] = color }
    suspend fun setInfoBoxCustomTextColor(color: String) = context.dataStore.edit { it[KEY_INFOBOX_CUSTOM_TEXT_COLOR] = color }
    suspend fun setInfoBoxCustomTexts(json: String) = context.dataStore.edit { it[KEY_INFOBOX_CUSTOM_TEXTS] = json }
    suspend fun setInfoBoxCustomMode(mode: String) = context.dataStore.edit { it[KEY_INFOBOX_CUSTOM_MODE] = mode }
    suspend fun setInfoBoxCustomSec(sec: Int) = context.dataStore.edit { it[KEY_INFOBOX_CUSTOM_SEC] = sec }
    suspend fun setInfoBoxSwipeTimeoutSec(sec: Int) = context.dataStore.edit { it[KEY_INFOBOX_SWIPE_TIMEOUT_SEC] = sec }
    suspend fun setInfoBoxEnabled(enabled: Boolean) = context.dataStore.edit { it[KEY_INFOBOX_ENABLED] = enabled }
    suspend fun setInfoBoxFont(fontName: String) = context.dataStore.edit { it[KEY_INFOBOX_FONT] = fontName }
    suspend fun setMeterDisplayMode(mode: String) = context.dataStore.edit { it[KEY_METER_DISPLAY_MODE] = mode }
    suspend fun setMeterCountMode(mode: String) = context.dataStore.edit { it[KEY_METER_COUNT_MODE] = mode }

    suspend fun updateCustomTheme(
        bgColor: String? = null,
        keyBgColor: String? = null,
        keySpecialColor: String? = null,
        keyTextColor: String? = null,
        keySpecialTextColor: String? = null,
        accentColor: String? = null,
        suggestionBgColor: String? = null,
        suggestionTextColor: String? = null,
        popupBgColor: String? = null,
        popupTextColor: String? = null,
        keyHintColor: String? = null
    ) {
        context.dataStore.edit { prefs ->
            bgColor?.let { prefs[KEY_CUSTOM_BG_COLOR] = it }
            keyBgColor?.let { prefs[KEY_CUSTOM_KEY_BG_COLOR] = it }
            keySpecialColor?.let { prefs[KEY_CUSTOM_KEY_SPECIAL_COLOR] = it }
            keyTextColor?.let { prefs[KEY_CUSTOM_KEY_TEXT_COLOR] = it }
            keySpecialTextColor?.let { prefs[KEY_CUSTOM_KEY_SPECIAL_TEXT_COLOR] = it }
            accentColor?.let { prefs[KEY_CUSTOM_ACCENT_COLOR] = it }
            suggestionBgColor?.let { prefs[KEY_CUSTOM_SUGGESTION_BG_COLOR] = it }
            suggestionTextColor?.let { prefs[KEY_CUSTOM_SUGGESTION_TEXT_COLOR] = it }
            popupBgColor?.let { prefs[KEY_CUSTOM_POPUP_BG_COLOR] = it }
            popupTextColor?.let { prefs[KEY_CUSTOM_POPUP_TEXT_COLOR] = it }
            keyHintColor?.let { prefs[KEY_CUSTOM_KEY_HINT_COLOR] = it }
        }
    }

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

    suspend fun setAppLanguage(language: String) {
        context.dataStore.edit { it[KEY_APP_LANGUAGE] = language }
    }

    suspend fun setNavigationStyle(style: String) {
        context.dataStore.edit { it[KEY_NAV_STYLE] = style }
    }

    suspend fun setAccentColor(color: String) {
        context.dataStore.edit { it[KEY_ACCENT_COLOR] = color }
    }
}
