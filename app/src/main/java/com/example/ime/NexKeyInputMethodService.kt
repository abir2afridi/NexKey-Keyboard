package com.example.ime

import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.InputType
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import com.example.clipboard.ClipboardManager
import com.example.data.TypingAnalytics
import com.example.data.UserPreferences
import com.example.engine.BanglaPhoneticEngine
import com.example.engine.PredictionEngine
import com.example.theme.KeyboardTheme
import com.example.theme.ThemePreset
import com.example.ui.KeyboardComposeView
import com.example.ui.KeyboardMode
import com.example.ui.ShiftState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class NexKeyInputMethodService : LifecycleInputMethodService() {

    private var currentMode by mutableStateOf(KeyboardMode.BANGLA_PHONETIC)
    private var shiftState by mutableStateOf(ShiftState.OFF)
    private var currentTheme by mutableStateOf(KeyboardTheme.DarkNeon)
    private var composingBuffer by mutableStateOf("")
    private var candidates by mutableStateOf<List<String>>(emptyList())
    private var actionLabel by mutableStateOf("↵")
    private var isIncognito by mutableStateOf(false)
    private var isPasswordField by mutableStateOf(false)
    private var isSensitiveField by mutableStateOf(false)
    private var isMultilineField by mutableStateOf(false)
    private var lastSpaceTime = 0L

    // Speed Meter States
    private var currentLiveCps by mutableStateOf(0f)
    private var maxBurstCps by mutableStateOf(0f)
    private var isTypingActive by mutableStateOf(false)
    private var burstStartTime = 0L
    private var burstKeyCount = 0
    private var lastKeyPressTime = 0L
    private var typingStopJob: kotlinx.coroutines.Job? = null

    // Live Preferences
    private var hapticsEnabled by mutableStateOf(true)
    private var soundEnabled by mutableStateOf(true)
    private var autoCapEnabled by mutableStateOf(true)
    private var smartPuncEnabled by mutableStateOf(true)
    private var keyHeight by mutableStateOf(54)
    private var keyRadius by mutableStateOf(10)
    private var showNumRow by mutableStateOf(false)
    private var hideLongPressHints by mutableStateOf(false)
    private var kbHeightPortrait by mutableStateOf(100)
    private var oneHandedWidth by mutableStateOf(100)
    private var hapticLevel by mutableStateOf(50)
    private var soundLevel by mutableStateOf(50)

    // New preference collections
    private var doubleSpaceTabEnabled by mutableStateOf(false)
    private var voiceInputKeyEnabled by mutableStateOf(true)
    private var showEmojiKeyEnabled by mutableStateOf(true)
    private var showGlobeKeyEnabled by mutableStateOf(true)
    private var allowOtherKeyboardsEnabled by mutableStateOf(true)
    private var moveCursorSpaceEnabled by mutableStateOf(true)
    private var volumeCursorEnabled by mutableStateOf(false)
    private var smartVolumeControlEnabled by mutableStateOf(true)
    private var popupOnKeypressEnabled by mutableStateOf(true)
    private var showSuggestionsEnabled by mutableStateOf(true)
    private var personalizedSuggestionsEnabled by mutableStateOf(true)
    private var blockOffensiveEnabled by mutableStateOf(true)
    private var enableResizing by mutableStateOf(false)
    private var largeNumberRowEnabled by mutableStateOf(false)
    private var kbHeightLandscape by mutableStateOf(100)
    private var oneHandedWidthLandscape by mutableStateOf(40)
    private var splitKeyboardEnabled by mutableStateOf(false)
    private var forcedEnterEnabled by mutableStateOf(false)
    private var longPressDelayMsState by mutableStateOf(300)
    private var spaceCursorDelayState by mutableStateOf(1000)
    private var spaceCursorSpeedState by mutableStateOf(150)
    private var showTypedWordFirstEnabled by mutableStateOf(true)
    private var clipboardExpiryMinutes by mutableStateOf(120)
    private var autoCorrectionEnabled by mutableStateOf(true)
    private var phoneticAutoCorrectionEnabled by mutableStateOf(true)
    private var nextWordSuggestionsEnabled by mutableStateOf(true)
    private var clipboardRecentEnabled by mutableStateOf(true)
    private var clipboardImagesEnabled by mutableStateOf(true)
    private var physicalKbEmojiEnabled by mutableStateOf(true)
    private var popupDismissDelayState by mutableStateOf("Default")
    private var holdPasteEnabled by mutableStateOf(false)
    private var holdPasteDuration by mutableStateOf(400)
    private var holdPasteTriggerKey by mutableStateOf("v")
    private var alwaysShowSuggestions by mutableStateOf(false)
    private var autoHideToolbar by mutableStateOf(false)
    private var backspaceRepeatDelayMsState by mutableStateOf(400)
    private var backspaceRepeatSpeedMsState by mutableStateOf(50)

    private val predictionEngine = PredictionEngine()
    private var speechRecognizer: SpeechRecognizer? = null
    private lateinit var userPreferences: UserPreferences
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var keyboardView: View? = null
    private var vibrator: Vibrator? = null

    override fun onCreate() {
        super.onCreate()
        ClipboardManager.init(this)
        TypingAnalytics.init(this)
        predictionEngine.init(this)
        userPreferences = UserPreferences(this)
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        scope.launch {
            launch {
                userPreferences.theme.collectLatest { savedTheme ->
                    currentTheme = try {
                        val preset = ThemePreset.valueOf(savedTheme)
                        KeyboardTheme.fromPreset(preset)
                    } catch (e: Exception) {
                        KeyboardTheme.DarkNeon
                    }
                }
            }
            launch {
                userPreferences.language.collectLatest { savedLanguage ->
                    currentMode = try { KeyboardMode.valueOf(savedLanguage) } catch (_: Exception) { KeyboardMode.BANGLA_PHONETIC }
                }
            }
            launch {
                userPreferences.incognito.collectLatest { incognitoEnabled ->
                    isIncognito = incognitoEnabled
                    ClipboardManager.setIncognito(incognitoEnabled)
                    predictionEngine.setIncognito(incognitoEnabled)
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
            launch { userPreferences.blockOffensive.collectLatest { blockOffensiveEnabled = it } }
            launch { userPreferences.enableKbResizing.collectLatest { enableResizing = it } }
            launch { userPreferences.largeNumberRow.collectLatest { largeNumberRowEnabled = it } }
            launch { userPreferences.kbHeightLandscape.collectLatest { kbHeightLandscape = it } }
            launch { userPreferences.oneHandedWidthLandscape.collectLatest { oneHandedWidthLandscape = it } }
            launch { userPreferences.splitKeyboard.collectLatest { splitKeyboardEnabled = it } }
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
            launch { userPreferences.clipboardImages.collectLatest { clipboardImagesEnabled = it } }
            launch { userPreferences.physicalKbEmoji.collectLatest { physicalKbEmojiEnabled = it } }
            launch { userPreferences.popupDismissDelay.collectLatest { popupDismissDelayState = it } }
            launch { userPreferences.holdPasteEnabled.collectLatest { holdPasteEnabled = it } }
            launch { userPreferences.holdPasteDuration.collectLatest { holdPasteDuration = it } }
            launch { userPreferences.holdPasteTriggerKey.collectLatest { holdPasteTriggerKey = it } }
            launch { userPreferences.alwaysShowSuggestions.collectLatest { alwaysShowSuggestions = it } }
            launch { userPreferences.autoHideToolbar.collectLatest { autoHideToolbar = it } }
            launch { userPreferences.backspaceRepeatDelay.collectLatest { backspaceRepeatDelayMsState = it } }
            launch { userPreferences.backspaceRepeatSpeed.collectLatest { backspaceRepeatSpeedMsState = it } }
        }
    }

    override fun onCreateInputView(): View {
        val composeView = ComposeView(this).apply {
            setLifecycleOwners()
            setContent {
                val themeWithPrefs = currentTheme.copy(
                    keyHeightDp = keyHeight,
                    keyRadiusDp = keyRadius
                )

                KeyboardComposeView(
                    mode = currentMode,
                    shiftState = shiftState,
                    theme = themeWithPrefs,
                    composingText = composingBuffer,
                    suggestions = if (showSuggestionsEnabled) candidates else emptyList(),
                    actionLabel = actionLabel,
                    showNumberRow = showNumRow,
                    hideLongPressHints = hideLongPressHints,
                    keyboardHeightPortrait = if (enableResizing) kbHeightPortrait else 100,
                    keyboardHeightLandscape = kbHeightLandscape,
                    oneHandedWidth = oneHandedWidth,
                    oneHandedWidthLandscape = oneHandedWidthLandscape,
                    isIncognito = isIncognito,
                    isPasswordField = isPasswordField,
                    showVoiceKey = voiceInputKeyEnabled,
                    showEmojiKey = showEmojiKeyEnabled,
                    showGlobeKey = showGlobeKeyEnabled,
                    moveCursorSpaceEnabled = moveCursorSpaceEnabled,
                    popupOnKeypressEnabled = popupOnKeypressEnabled,
                    largeNumberRowEnabled = largeNumberRowEnabled,
                    longPressDelayMs = longPressDelayMsState.toLong(),
                    spaceCursorSpeed = spaceCursorSpeedState,
                    spaceCursorDelay = spaceCursorDelayState,
                    splitKeyboardEnabled = splitKeyboardEnabled,
                    popupDismissDelay = popupDismissDelayState,
                    physicalKbEmojiEnabled = physicalKbEmojiEnabled,
                    holdPasteEnabled = holdPasteEnabled,
                    holdPasteTriggerKey = holdPasteTriggerKey,
                    holdPasteDuration = holdPasteDuration,
                    alwaysShowSuggestions = alwaysShowSuggestions,
                    autoHideToolbar = autoHideToolbar,
                    backspaceRepeatDelayMs = backspaceRepeatDelayMsState.toLong(),
                    backspaceRepeatSpeedMs = backspaceRepeatSpeedMsState.toLong(),
                    liveCps = currentLiveCps,
                    maxBurstCps = maxBurstCps,
                    isSpeedActive = isTypingActive,
                    onKeyTap = { key -> handleKeyTap(key) },
                    onBackspaceTap = { handleBackspace() },
                    onSpaceTap = { handleSpace() },
                    onEnterTap = { handleEnter() },
                    onShiftTap = { handleShiftToggle() },
                    onModeChange = { newMode -> handleModeChange(newMode) },
                    onSuggestionSelect = { word -> commitSuggestion(word) },
                    onVoiceClick = { startVoiceInput() },
                    onThemeToggle = { toggleTheme() },
                    onOpenSettings = { launchSettingsActivity() },
                    onCursorMove = { direction -> handleCursorMove(direction) },
                    onIncognitoToggle = { toggleIncognito() },
                    onHoldPaste = { handlePasteClipboard() }
                )
            }
        }
        keyboardView = composeView
        return composeView
    }

    // Volume key interception for cursor control
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (volumeCursorEnabled && (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            if (smartVolumeControlEnabled) {
                val am = getSystemService(AUDIO_SERVICE) as AudioManager
                if (am.isMusicActive) return false
            }
            val direction = if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) 1 else -1
            handleCursorMove(direction)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (volumeCursorEnabled && (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun handleCursorMove(direction: Int) {
        val ic = currentInputConnection ?: return
        ic.beginBatchEdit()
        ic.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, if (direction > 0) android.view.KeyEvent.KEYCODE_DPAD_RIGHT else android.view.KeyEvent.KEYCODE_DPAD_LEFT))
        ic.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, if (direction > 0) android.view.KeyEvent.KEYCODE_DPAD_RIGHT else android.view.KeyEvent.KEYCODE_DPAD_LEFT))
        ic.endBatchEdit()
    }

    override fun onEvaluateFullscreenMode(): Boolean {
        return false
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        TypingAnalytics.startSession()
        composingBuffer = ""
        candidates = emptyList()

        detectSensitiveField(info)

        val imeAction = info?.imeOptions?.let { it and EditorInfo.IME_MASK_ACTION }
        actionLabel = if (isMultilineField) {
            "↵"
        } else when (imeAction) {
            EditorInfo.IME_ACTION_SEARCH -> "Search"
            EditorInfo.IME_ACTION_GO -> "Go"
            EditorInfo.IME_ACTION_SEND -> "Send"
            EditorInfo.IME_ACTION_NEXT -> "Next"
            EditorInfo.IME_ACTION_DONE -> "Done"
            else -> "↵"
        }

        detectSensitiveField(info)
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        TypingAnalytics.endSession()
        super.onFinishInputView(finishingInput)
    }

    private fun playFeedback() {
        if (hapticsEnabled) {
            if (hapticLevel > 0) {
                try {
                    val duration = (hapticLevel * 2).toLong()
                    val amplitude = ((hapticLevel / 100f) * 255).toInt().coerceIn(1, 255)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator?.vibrate(VibrationEffect.createOneShot(duration, amplitude))
                    } else {
                        keyboardView?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    }
                } catch (_: Exception) {
                    keyboardView?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                }
            } else {
                keyboardView?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            }
        }
        if (soundEnabled) {
            val am = getSystemService(android.content.Context.AUDIO_SERVICE) as android.media.AudioManager
            val volume = (soundLevel / 100f).coerceIn(0f, 1f)
            try {
                am.playSoundEffect(android.media.AudioManager.FX_KEYPRESS_STANDARD, volume)
            } catch (_: Exception) {
                am.playSoundEffect(android.media.AudioManager.FX_KEYPRESS_STANDARD)
            }
        }
    }

    private fun detectSensitiveField(info: EditorInfo?) {
        val inputType = info?.inputType ?: 0
        val variation = inputType and InputType.TYPE_MASK_VARIATION
        val isPassword = variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
                variation == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD ||
                inputType and InputType.TYPE_NUMBER_VARIATION_PASSWORD == InputType.TYPE_NUMBER_VARIATION_PASSWORD

        val isSensitive = variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS ||
                variation == InputType.TYPE_TEXT_VARIATION_URI ||
                variation == InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS ||
                isPassword

        isPasswordField = isPassword
        isSensitiveField = isSensitive
        isMultilineField = inputType and InputType.TYPE_TEXT_FLAG_MULTI_LINE != 0

        if (isPassword || isSensitive) {
            candidates = emptyList()
            predictionEngine.setIncognito(true)
            ClipboardManager.setIncognito(true)
        } else if (!isIncognito) {
            predictionEngine.setIncognito(false)
            ClipboardManager.setIncognito(false)
        }
    }

    private fun handleKeyTap(key: String) {
        playFeedback()
        if (!isIncognito && !isSensitiveField) {
            TypingAnalytics.trackKeyPress()
            
            // Live Speed Meter Logic
            val now = System.currentTimeMillis()
            if (now - lastKeyPressTime > 2000) {
                burstStartTime = now
                burstKeyCount = 0
                isTypingActive = true
                currentLiveCps = 0f
                maxBurstCps = 0f
            }
            
            lastKeyPressTime = now
            burstKeyCount++
            
            val elapsedSec = (now - burstStartTime) / 1000f
            if (elapsedSec > 0.1f) {
                currentLiveCps = burstKeyCount / elapsedSec
                if (currentLiveCps > maxBurstCps) {
                    maxBurstCps = currentLiveCps
                }
            }
            
            typingStopJob?.cancel()
            typingStopJob = scope.launch {
                kotlinx.coroutines.delay(2000)
                isTypingActive = false
            }
        }
        val ic = currentInputConnection ?: return

        val isAlphaKey = key.length == 1 && key[0].isLetter()
        val isBanglaComposing = !isPasswordField && (currentMode == KeyboardMode.BANGLA_PHONETIC || currentMode == KeyboardMode.AVRO) && isAlphaKey
        val isEnglishComposing = !isPasswordField && (currentMode == KeyboardMode.ENGLISH || currentMode == KeyboardMode.ARABIC) && isAlphaKey

        if (isBanglaComposing) {
            composingBuffer += key
            val parsedBangla = BanglaPhoneticEngine.parse(composingBuffer)
            ic.setComposingText(parsedBangla, 1)
            updateCandidates(composingBuffer)
        } else if (isEnglishComposing) {
            composingBuffer += key
            ic.setComposingText(composingBuffer, 1)
            updateCandidates(composingBuffer)
        } else {
            if (composingBuffer.isNotEmpty()) {
                commitComposingBuffer()
            }
            ic.beginBatchEdit()

            val textToCommit = if (autoCapEnabled && key.length == 1 && isNewSentence()) {
                key.uppercase()
            } else {
                key
            }

            ic.commitText(textToCommit, 1)
            ic.endBatchEdit()

            if (!isIncognito && !isSensitiveField) {
                scope.launch {
                    userPreferences.incrementStats(words = 0, chars = 1)
                }
            }

            if (key.length > 5 && !isSensitiveField) {
                ClipboardManager.addClip(key)
            }
        }

        if (shiftState == ShiftState.SHIFT) {
            shiftState = ShiftState.OFF
        }
    }

    private fun isNewSentence(): Boolean {
        val ic = currentInputConnection ?: return true
        val before = ic.getTextBeforeCursor(2, 0) ?: ""
        return before.isEmpty() || before.endsWith(". ") || before.endsWith("! ") || before.endsWith("? ") || before.endsWith("\n") || before.endsWith("। ")
    }

    private fun handleBackspace() {
        playFeedback()
        val ic = currentInputConnection ?: return

        if (composingBuffer.isNotEmpty()) {
            composingBuffer = composingBuffer.dropLast(1)
            if (composingBuffer.isNotEmpty()) {
                val parsed = if (currentMode == KeyboardMode.BANGLA_PHONETIC || currentMode == KeyboardMode.AVRO) {
                    BanglaPhoneticEngine.parse(composingBuffer)
                } else {
                    composingBuffer
                }
                ic.setComposingText(parsed, 1)
                updateCandidates(composingBuffer)
            } else {
                ic.finishComposingText()
                candidates = emptyList()
            }
        } else {
            ic.deleteSurroundingText(1, 0)
        }
    }

    private fun handleSpace() {
        playFeedback()
        val ic = currentInputConnection ?: return

        if (composingBuffer.isNotEmpty()) {
            val isBangla = currentMode == KeyboardMode.BANGLA_PHONETIC || currentMode == KeyboardMode.AVRO
            val rawWord = if (isBangla) BanglaPhoneticEngine.parse(composingBuffer) else composingBuffer
            val correctedWord = if (autoCorrectionEnabled && !isSensitiveField) {
                val correction = predictionEngine.getCorrection(rawWord, isBangla)
                if (correction != null && rawWord.length > 2) correction else rawWord
            } else {
                rawWord
            }
            ic.beginBatchEdit()
            ic.commitText("$correctedWord ", 1)
            ic.endBatchEdit()
            if (!isSensitiveField) {
                predictionEngine.learnWord(correctedWord, isBangla = isBangla)
                predictionEngine.setLastTypedWord(correctedWord)
                if (!isIncognito) {
                    scope.launch { userPreferences.incrementStats(words = 1, chars = correctedWord.length + 1) }
                }
                if (nextWordSuggestionsEnabled) {
                    candidates = predictionEngine.getNextWordPredictions()
                }
            }
            composingBuffer = ""
        } else {
            val now = System.currentTimeMillis()
            if (now - lastSpaceTime < 400) {
                if (doubleSpaceTabEnabled) {
                    ic.beginBatchEdit()
                    ic.deleteSurroundingText(1, 0)
                    ic.commitText("\t", 1)
                    ic.endBatchEdit()
                } else if (smartPuncEnabled) {
                    ic.beginBatchEdit()
                    ic.deleteSurroundingText(1, 0)
                    ic.commitText(". ", 1)
                    ic.endBatchEdit()
                } else {
                    ic.commitText(" ", 1)
                }
            } else {
                ic.commitText(" ", 1)
            }
        }
        lastSpaceTime = System.currentTimeMillis()
    }

    private fun handleEnter() {
        playFeedback()
        val ic = currentInputConnection ?: return

        if (composingBuffer.isNotEmpty()) {
            commitComposingBuffer()
        }

        if (forcedEnterEnabled || isMultilineField) {
            ic?.commitText("\n", 1)
            return
        }

        val info = currentInputEditorInfo
        val imeAction = info?.imeOptions?.let { it and EditorInfo.IME_MASK_ACTION }
        if (imeAction != null && imeAction != EditorInfo.IME_ACTION_NONE && imeAction != EditorInfo.IME_ACTION_UNSPECIFIED) {
            ic?.performEditorAction(imeAction)
        } else {
            ic?.commitText("\n", 1)
        }
    }

    private fun handleShiftToggle() {
        shiftState = when (shiftState) {
            ShiftState.OFF -> ShiftState.SHIFT
            ShiftState.SHIFT -> ShiftState.CAPS_LOCK
            ShiftState.CAPS_LOCK -> ShiftState.OFF
        }
    }

    private fun handleModeChange(newMode: KeyboardMode) {
        if (newMode == KeyboardMode.ENGLISH && currentMode == KeyboardMode.ENGLISH && allowOtherKeyboardsEnabled) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            imm.switchToLastInputMethod(window?.window?.decorView?.windowToken)
            return
        }
        currentMode = newMode
        scope.launch { userPreferences.setLanguage(newMode) }
    }

    private fun commitSuggestion(word: String) {
        val ic = currentInputConnection ?: return
        ic.beginBatchEdit()
        ic.commitText("$word ", 1)
        ic.endBatchEdit()
        if (!isSensitiveField && personalizedSuggestionsEnabled) {
            predictionEngine.learnWord(word, isBangla = currentMode == KeyboardMode.BANGLA_PHONETIC)
            predictionEngine.setLastTypedWord(word)
            if (nextWordSuggestionsEnabled) {
                candidates = predictionEngine.getNextWordPredictions()
            }
        }
        composingBuffer = ""
    }

    private fun updateCandidates(query: String) {
        if (isPasswordField || isSensitiveField) {
            candidates = emptyList()
            return
        }
        val predictions = predictionEngine.getPredictions(
            prefix = query,
            isBangla = (currentMode == KeyboardMode.BANGLA_PHONETIC || currentMode == KeyboardMode.AVRO),
            showTypedWordFirst = showTypedWordFirstEnabled
        )
        candidates = if (blockOffensiveEnabled) {
            predictions.filterNot { isOffensive(it) }
        } else {
            predictions
        }
    }

    private val offensiveWords = setOf(
        "fuck", "shit", "damn", "ass", "bitch", "bastard", "crap", "dick",
        "piss", "slut", "whore", "cock", "cunt", "douche", "fag", "nigger",
        "motherfucker", "bullshit", "asshole", "dumbass", "jackass"
    )

    private fun isOffensive(word: String): Boolean {
        return word.lowercase() in offensiveWords
    }

    private fun commitComposingBuffer() {
        val ic = currentInputConnection ?: return
        if (composingBuffer.isNotEmpty()) {
            val word = if (currentMode == KeyboardMode.BANGLA_PHONETIC || currentMode == KeyboardMode.AVRO) {
                BanglaPhoneticEngine.parse(composingBuffer)
            } else {
                composingBuffer
            }
            ic.beginBatchEdit()
            ic.commitText(word, 1)
            ic.endBatchEdit()
            if (!isSensitiveField && !isIncognito) {
            predictionEngine.learnWord(word, isBangla = currentMode == KeyboardMode.BANGLA_PHONETIC || currentMode == KeyboardMode.AVRO)
                scope.launch { userPreferences.incrementStats(words = 1, chars = word.length) }
            }
            composingBuffer = ""
            candidates = emptyList()
        }
    }

    private fun toggleTheme() {
        val themes = ThemePreset.values()
        val nextIndex = (currentTheme.preset.ordinal + 1) % themes.size
        currentTheme = KeyboardTheme.fromPreset(themes[nextIndex])
        scope.launch { userPreferences.setTheme(currentTheme.preset) }
    }

    private fun toggleIncognito() {
        isIncognito = !isIncognito
        ClipboardManager.setIncognito(isIncognito)
        predictionEngine.setIncognito(isIncognito)
        scope.launch { userPreferences.setIncognito(isIncognito) }
        if (isIncognito) {
            Toast.makeText(this, "Incognito mode ON", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Incognito mode OFF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handlePasteClipboard() {
        val ic = currentInputConnection ?: return
        val clipboard = getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = clipboard.primaryClip ?: return
        if (clip.itemCount > 0) {
            val text = clip.getItemAt(0).coerceToText(this)?.toString() ?: return
            if (composingBuffer.isNotEmpty()) commitComposingBuffer()
            ic.beginBatchEdit()
            ic.commitText(text, 1)
            ic.endBatchEdit()
        }
    }

    private fun startVoiceInput() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Grant microphone permission in Settings > Apps > NexKey > Permissions to use voice typing.", Toast.LENGTH_LONG).show()
            try {
                val intent = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = android.net.Uri.fromParts("package", packageName, null)
                    addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            } catch (_: Exception) {}
            return
        }

        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Voice typing is not available", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            speechRecognizer?.destroy()
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    if (currentMode == KeyboardMode.BANGLA_PHONETIC || currentMode == KeyboardMode.BANGLA_JATIYO || currentMode == KeyboardMode.AVRO) "bn-BD" else "en-US"
                )
            }

            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    Toast.makeText(this@NexKeyInputMethodService, "Listening...", Toast.LENGTH_SHORT).show()
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        currentInputConnection?.beginBatchEdit()
                        currentInputConnection?.commitText(matches[0] + " ", 1)
                        currentInputConnection?.endBatchEdit()
                    }
                }

                override fun onError(error: Int) {
                    val msg = when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH -> "No speech detected"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech timed out"
                        SpeechRecognizer.ERROR_NETWORK -> "Network error"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
                        9 -> "Permission denied"
                        else -> "Error: $error"
                    }
                    Toast.makeText(this@NexKeyInputMethodService, msg, Toast.LENGTH_SHORT).show()
                }

                override fun onBeginningOfSpeech() {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onRmsChanged(rmsdB: Float) {}
            })

            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to start voice input: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchSettingsActivity() {
        val intent = Intent(this, com.example.MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
    }
}
