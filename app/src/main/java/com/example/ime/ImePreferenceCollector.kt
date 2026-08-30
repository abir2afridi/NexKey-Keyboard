package com.example.ime

import androidx.compose.ui.graphics.Color
import com.example.clipboard.ClipboardManager
import com.example.theme.KeyboardTheme
import com.example.theme.MeterTheme
import com.example.theme.MeterThemePreset
import com.example.theme.ThemePreset
import com.example.ui.KeyboardMode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.json.JSONArray

internal fun NexKeyInputMethodService.collectAllPreferences() {
    scope.launch {
        launch {
            combine(
                userPreferences.theme,
                userPreferences.customBgColor,
                userPreferences.customKeyBgColor,
                userPreferences.customKeySpecialColor,
                userPreferences.customKeyTextColor,
                userPreferences.customKeySpecialTextColor,
                userPreferences.customAccentColor,
                userPreferences.customSuggestionBgColor,
                userPreferences.customSuggestionTextColor,
                userPreferences.customPopupBgColor,
                userPreferences.customPopupTextColor,
                userPreferences.customKeyHintColor
            ) { values ->
                val savedTheme = values[0] as String
                val preset = try { ThemePreset.valueOf(savedTheme) } catch (e: Exception) { ThemePreset.DARK_NEON }

                if (preset == ThemePreset.CUSTOM) {
                    fun parseColor(s: Any): Color {
                        return try { Color(android.graphics.Color.parseColor(s as String)) } catch (_: Exception) { Color.White }
                    }
                    KeyboardTheme(
                        preset = ThemePreset.CUSTOM,
                        backgroundColor = parseColor(values[1]),
                        keyBackgroundColor = parseColor(values[2]),
                        keySpecialColor = parseColor(values[3]),
                        keyTextColor = parseColor(values[4]),
                        keySpecialTextColor = parseColor(values[5]),
                        accentColor = parseColor(values[6]),
                        suggestionBgColor = parseColor(values[7]),
                        suggestionTextColor = parseColor(values[8]),
                        popupBackgroundColor = parseColor(values[9]),
                        popupTextColor = parseColor(values[10]),
                        keyHintColor = parseColor(values[11])
                    )
                } else {
                    KeyboardTheme.fromPreset(preset)
                }
            }.collectLatest { currentTheme = it }
        }
        launch {
            userPreferences.language.collectLatest { savedLanguage ->
                val mode = try { KeyboardMode.valueOf(savedLanguage) } catch (_: Exception) { KeyboardMode.BANGLA_JATIYO }
                // Don't override transient modes (EMOJI, SYMBOLS, NUMBERS, CLIPBOARD).
                // These are managed by handleModeChange and should not be reset by
                // stale DataStore emissions from a prior async setLanguage() call.
                if (currentMode == KeyboardMode.EMOJI || currentMode == KeyboardMode.SYMBOLS ||
                    currentMode == KeyboardMode.NUMBERS || currentMode == KeyboardMode.CLIPBOARD) {
                    return@collectLatest
                }
                if (mode != KeyboardMode.SYMBOLS && mode != KeyboardMode.NUMBERS && mode != KeyboardMode.EMOJI && mode != KeyboardMode.CLIPBOARD) {
                    lastTextMode = mode
                    currentMode = mode
                } else {
                    currentMode = mode
                }
            }
        }
        launch {
            userPreferences.incognito.collectLatest { incognitoEnabled ->
                isIncognito = incognitoEnabled
                ClipboardManager.setIncognito(incognitoEnabled)
            }
        }
        launch { userPreferences.haptics.collectLatest { hapticsEnabled = it } }
        launch { userPreferences.sound.collectLatest { soundEnabled = it } }
        launch { userPreferences.autoCapitalize.collectLatest { autoCapEnabled = it } }
        launch { userPreferences.smartPunctuation.collectLatest { smartPuncEnabled = it } }
        launch { userPreferences.keyHeight.collectLatest { keyHeight = it } }
        launch { userPreferences.keyRadius.collectLatest { keyRadius = it } }
        launch { userPreferences.showNumberRow.collectLatest { showNumRow = it } }
        launch { userPreferences.hideLongPressHints.collectLatest { hideLongPressHints = it } }
        launch { userPreferences.kbHeightPortrait.collectLatest { kbHeightPortrait = it } }
        launch { userPreferences.oneHandedWidthPortrait.collectLatest { oneHandedWidth = it } }
        launch { userPreferences.hapticIntensity.collectLatest { hapticLevel = it } }
        launch { userPreferences.soundVolume.collectLatest { soundLevel = it } }

        // New collections
        launch { userPreferences.doubleSpaceTab.collectLatest { doubleSpaceTabEnabled = it } }
        launch { userPreferences.voiceInputKey.collectLatest { voiceInputKeyEnabled = it } }
        launch { userPreferences.showEmojiKey.collectLatest { showEmojiKeyEnabled = it } }
        launch { userPreferences.showGlobeKey.collectLatest { showGlobeKeyEnabled = it } }
        launch { userPreferences.allowOtherKeyboards.collectLatest { allowOtherKeyboardsEnabled = it } }
        launch { userPreferences.moveCursorSpace.collectLatest { moveCursorSpaceEnabled = it } }
        launch { userPreferences.volumeCursor.collectLatest { volumeCursorEnabled = it } }
        launch { userPreferences.smartVolumeControl.collectLatest { smartVolumeControlEnabled = it } }
        launch { userPreferences.popupOnKeypress.collectLatest { popupOnKeypressEnabled = it } }
        launch { userPreferences.showSuggestions.collectLatest { showSuggestionsEnabled = it } }
        launch { userPreferences.personalizedSuggestions.collectLatest { personalizedSuggestionsEnabled = it } }
        launch { userPreferences.enableKbResizing.collectLatest { enableResizing = it } }
        launch { userPreferences.largeNumberRow.collectLatest { largeNumberRowEnabled = it } }
        launch { userPreferences.kbHeightLandscape.collectLatest { kbHeightLandscape = it } }
        launch { userPreferences.oneHandedWidthLandscape.collectLatest { oneHandedWidthLandscape = it } }
        launch { userPreferences.spacebarLanguageSwitch.collectLatest { spacebarLanguageSwitchEnabled = it } }
        launch { userPreferences.enableArabic.collectLatest { enableArabic = it } }
        launch { userPreferences.forcedEnter.collectLatest { forcedEnterEnabled = it } }
        launch { userPreferences.longPressDelayMs.collectLatest { longPressDelayMsState = it } }
        launch { userPreferences.spaceCursorDelay.collectLatest { spaceCursorDelayState = it } }
        launch { userPreferences.spaceCursorSpeed.collectLatest { spaceCursorSpeedState = it } }
        launch { userPreferences.showTypedWordFirst.collectLatest { showTypedWordFirstEnabled = it } }
        launch { userPreferences.clipboardExpiry.collectLatest {
            clipboardExpiryMinutes = it
            ClipboardManager.setExpiryMinutes(it)
        } }
        launch { userPreferences.autoCorrection.collectLatest { autoCorrectionEnabled = it } }
        launch { userPreferences.phoneticAutoCorrection.collectLatest { phoneticAutoCorrectionEnabled = it } }
        launch { userPreferences.nextWordSuggestions.collectLatest { nextWordSuggestionsEnabled = it } }
        launch { userPreferences.clipboardRecent.collectLatest { clipboardRecentEnabled = it } }
        launch { userPreferences.physicalKbEmoji.collectLatest { physicalKbEmojiEnabled = it } }
        launch { userPreferences.popupDismissDelay.collectLatest { popupDismissDelayState = it } }
        launch { userPreferences.holdPasteEnabled.collectLatest { holdPasteEnabled = it } }
        launch { userPreferences.holdPasteDuration.collectLatest { holdPasteDuration = it } }
        launch { userPreferences.holdPasteTriggerKey.collectLatest { holdPasteTriggerKey = it } }
        launch { userPreferences.alwaysShowSuggestions.collectLatest { alwaysShowSuggestions = it } }
        launch { userPreferences.unifiedHeader.collectLatest { unifiedHeader = it } }
        launch { userPreferences.toolbarAutoShowDelay.collectLatest { toolbarAutoShowDelay = it } }
        launch { userPreferences.headerAnimation.collectLatest { headerAnimation = it } }
        launch { userPreferences.backspaceRepeatDelay.collectLatest { backspaceRepeatDelayMsState = it } }
        launch { userPreferences.backspaceRepeatSpeed.collectLatest { backspaceRepeatSpeedMsState = it } }
        launch {
            userPreferences.meterTheme.collectLatest { themeName ->
                currentMeterTheme = try {
                    MeterTheme.fromPreset(MeterThemePreset.valueOf(themeName))
                } catch (_: Exception) {
                    MeterTheme.Calculator
                }
            }
        }
        launch { userPreferences.meterFont.collectLatest { currentMeterFont = it } }
        launch { userPreferences.meterIdleMs.collectLatest { meterIdleMsState = it } }
        launch { userPreferences.meterInterval.collectLatest { meterIntervalState = it } }
        launch { userPreferences.meterEnabled.collectLatest { enabled ->
            meterEnabled = enabled
            if (!enabled) {
                isTypingActive = false
                typingStopJob?.cancel()
                meterPhase = SpeedMeterPhase.WAITING
                meterResultLines = emptyList()
                lastPressedWord = ""
            }
        } }
        launch { userPreferences.meterPosition.collectLatest { meterPositionState = it } }
        launch { userPreferences.infoBoxFrame.collectLatest { infoBoxFrameState = it } }
        launch { userPreferences.infoBoxTextColor.collectLatest { infoBoxTextColorState = it } }
        launch { userPreferences.infoBoxInfoColor.collectLatest { infoBoxInfoColorState = it } }
        launch { userPreferences.infoBoxCustomTextColor.collectLatest { infoBoxCustomTextColorState = it } }
        launch { userPreferences.infoBoxCustomTexts.collectLatest { infoBoxCustomTextsState = it } }
        launch { userPreferences.infoBoxCustomMode.collectLatest { infoBoxCustomModeState = it } }
        launch { userPreferences.infoBoxCustomSec.collectLatest { infoBoxCustomSecState = it } }
        launch { userPreferences.infoBoxSwipeTimeoutSec.collectLatest { infoBoxSwipeTimeoutSecState = it } }
        launch { userPreferences.infoBoxEnabled.collectLatest { infoBoxEnabledState = it } }
        launch { userPreferences.infoBoxFont.collectLatest { infoBoxFontState = it } }
        launch { userPreferences.meterDisplayMode.collectLatest { meterDisplayModeState = it } }
        launch { userPreferences.meterCountMode.collectLatest { meterCountModeState = it } }
        launch {
            userPreferences.recentEmojiExpiry.collectLatest { recentEmojiExpiryDays = it }
        }
        launch { userPreferences.emojiSearchVisibleRows.collectLatest { emojiSearchVisibleRows = it } }
        launch { userPreferences.emojiSearchHorizontal.collectLatest { emojiSearchHorizontal = it } }
        launch {
            userPreferences.recentEmojis.collectLatest { json ->
                val expiryMs = recentEmojiExpiryDays.toLong() * 24 * 60 * 60 * 1000
                val now = System.currentTimeMillis()
                recentEmojis = try {
                    val arr = JSONArray(json)
                    val filtered = mutableListOf<String>()
                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        val ts = obj.getLong("ts")
                        if (expiryMs <= 0 || (now - ts) < expiryMs) {
                            filtered.add(obj.getString("emoji"))
                        }
                    }
                    filtered
                } catch (_: Exception) {
                    emptyList()
                }
                recentEmojisList.clear()
                recentEmojisList.addAll(recentEmojis)
            }
        }
    }
}
