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
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import com.example.clipboard.ClipboardManager
import com.example.data.TypingAnalytics
import com.example.data.UserPreferences
import com.example.engine.PredictionEngine
import com.example.theme.KeyboardTheme
import com.example.theme.ThemePreset
import com.example.theme.MeterTheme
import com.example.ui.KeyboardComposeView
import com.example.ui.KeyboardMode
import com.example.ui.HeaderAnimation
import com.example.ui.ShiftState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class NexKeyInputMethodService : LifecycleInputMethodService() {

    internal var currentMode by mutableStateOf(KeyboardMode.BANGLA_JATIYO)
    internal var lastTextMode by mutableStateOf(KeyboardMode.BANGLA_JATIYO)
    internal var shiftState by mutableStateOf(ShiftState.OFF)
    internal var currentTheme by mutableStateOf(KeyboardTheme.DarkNeon)
    internal var composingBuffer by mutableStateOf("")
    internal var candidates by mutableStateOf<List<String>>(emptyList())
    private var actionLabel by mutableStateOf("↵")
    internal var isIncognito by mutableStateOf(false)
    internal var isPasswordField by mutableStateOf(false)
    internal var isSensitiveField by mutableStateOf(false)
    internal var isMultilineField by mutableStateOf(false)
    internal var lastSpaceTime = 0L

    // Speed Meter States
    internal var currentLiveCps by mutableStateOf(0f)
    internal var maxBurstCps by mutableStateOf(0f)
    internal var isTypingActive by mutableStateOf(false)
    internal var burstStartTime = 0L
    internal var burstKeyCount = 0
    internal var lastKeyPressTime = 0L
    internal var typingStopJob: kotlinx.coroutines.Job? = null

    // Live Preferences
    internal var hapticsEnabled by mutableStateOf(true)
    internal var soundEnabled by mutableStateOf(true)
    internal var autoCapEnabled by mutableStateOf(true)
    internal var smartPuncEnabled by mutableStateOf(true)
    internal var keyHeight by mutableStateOf(54)
    internal var keyRadius by mutableStateOf(10)
    internal var showNumRow by mutableStateOf(false)
    internal var hideLongPressHints by mutableStateOf(false)
    internal var kbHeightPortrait by mutableStateOf(100)
    internal var oneHandedWidth by mutableStateOf(100)
    internal var hapticLevel by mutableStateOf(50)
    internal var soundLevel by mutableStateOf(50)

    // New preference collections
    internal var doubleSpaceTabEnabled by mutableStateOf(false)
    internal var voiceInputKeyEnabled by mutableStateOf(true)
    internal var showEmojiKeyEnabled by mutableStateOf(true)
    internal var showGlobeKeyEnabled by mutableStateOf(true)
    internal var allowOtherKeyboardsEnabled by mutableStateOf(true)
    internal var moveCursorSpaceEnabled by mutableStateOf(true)
    internal var volumeCursorEnabled by mutableStateOf(false)
    internal var smartVolumeControlEnabled by mutableStateOf(true)
    internal var popupOnKeypressEnabled by mutableStateOf(true)
    internal var showSuggestionsEnabled by mutableStateOf(true)
    internal var personalizedSuggestionsEnabled by mutableStateOf(true)
    internal var enableResizing by mutableStateOf(false)
    internal var largeNumberRowEnabled by mutableStateOf(false)
    internal var kbHeightLandscape by mutableStateOf(100)
    internal var oneHandedWidthLandscape by mutableStateOf(40)
    internal var splitKeyboardEnabled by mutableStateOf(false)
    internal var forcedEnterEnabled by mutableStateOf(false)
    internal var longPressDelayMsState by mutableStateOf(300)
    internal var spaceCursorDelayState by mutableStateOf(1000)
    internal var spaceCursorSpeedState by mutableStateOf(150)
    internal var showTypedWordFirstEnabled by mutableStateOf(true)
    internal var clipboardExpiryMinutes by mutableStateOf(120)
    internal var autoCorrectionEnabled by mutableStateOf(true)
    internal var phoneticAutoCorrectionEnabled by mutableStateOf(true)
    internal var nextWordSuggestionsEnabled by mutableStateOf(true)
    internal var clipboardRecentEnabled by mutableStateOf(true)
    internal var clipboardImagesEnabled by mutableStateOf(true)
    internal var physicalKbEmojiEnabled by mutableStateOf(true)
    internal var popupDismissDelayState by mutableStateOf("Default")
    internal var holdPasteEnabled by mutableStateOf(false)
    internal var holdPasteDuration by mutableStateOf(400)
    internal var holdPasteTriggerKey by mutableStateOf("v")
    internal var alwaysShowSuggestions by mutableStateOf(false)
    internal var unifiedHeader by mutableStateOf(false)
    internal var toolbarAutoShowDelay by mutableStateOf(10)
    internal var headerAnimation by mutableStateOf("FADE")
    internal var backspaceRepeatDelayMsState by mutableStateOf(400)
    internal var backspaceRepeatSpeedMsState by mutableStateOf(50)
    internal var currentMeterTheme by mutableStateOf(MeterTheme.Calculator)
    internal var currentMeterFont by mutableStateOf("DIGITAL")
    internal var recentEmojis by mutableStateOf<List<String>>(emptyList())
    internal var recentEmojiExpiryDays by mutableStateOf(30)
    internal var emojiSearchActive by mutableStateOf(false)
    internal var emojiSearchQuery by mutableStateOf("")
    internal var emojiSearchVisibleRows by mutableStateOf(2)
    internal var emojiSearchHorizontal by mutableStateOf(true)

    internal val predictionEngine = PredictionEngine()
    private var speechRecognizer: SpeechRecognizer? = null
    internal lateinit var userPreferences: UserPreferences
    internal val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

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

        collectAllPreferences()
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
                    lastTextMode = lastTextMode,
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
                    unifiedHeader = unifiedHeader,
                    toolbarAutoShowDelay = toolbarAutoShowDelay,
                    headerAnimation = HeaderAnimation.fromName(headerAnimation),
                    backspaceRepeatDelayMs = backspaceRepeatDelayMsState.toLong(),
                    backspaceRepeatSpeedMs = backspaceRepeatSpeedMsState.toLong(),
                    liveCps = currentLiveCps,
                    maxBurstCps = maxBurstCps,
                    isSpeedActive = isTypingActive,
                    meterTheme = currentMeterTheme,
                    meterFont = currentMeterFont,
                    recentEmojis = androidx.compose.runtime.mutableStateListOf<String>().also { it.addAll(recentEmojis) },
                    onRecentEmojisChanged = { emojis -> saveRecentEmojis(emojis) },
                    emojiSearchActive = emojiSearchActive,
                    emojiSearchQuery = emojiSearchQuery,
                    emojiSearchVisibleRows = emojiSearchVisibleRows,
                    emojiSearchHorizontal = emojiSearchHorizontal,
                    onEmojiSearchToggle = { toggleEmojiSearch() },
                    onEmojiSearchQueryChange = { query -> emojiSearchQuery = query },
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

    override fun onUpdateSelection(
        oldSelStart: Int,
        oldSelEnd: Int,
        newSelStart: Int,
        newSelEnd: Int,
        candidatesStart: Int,
        candidatesEnd: Int
    ) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd)

        // While we are composing, the cursor always sits at the END of the composing
        // region (setComposingText moves it there). If the cursor moved anywhere else —
        // even INSIDE the region, e.g. the user tapped between letters of a composed
        // word — the composing text must be committed and the buffer reset. Otherwise
        // the next keystroke replaces the whole composing word at its old position
        // and the cursor jumps to the end (e.g. "aple" + cursor after "p" + "p" = "aplep"
        // instead of "apple").
        if (composingBuffer.isEmpty()) return
        val selectionChanged = oldSelStart != newSelStart || oldSelEnd != newSelEnd
        if (!selectionChanged) return
        val hasComposingRegion = candidatesStart >= 0 && candidatesEnd > candidatesStart
        val cursorAtRegionEnd = hasComposingRegion &&
            newSelStart == candidatesEnd && newSelEnd == candidatesEnd
        if (!cursorAtRegionEnd) {
            currentInputConnection?.finishComposingText()
            composingBuffer = ""
            candidates = emptyList()
        }
    }

    internal fun playFeedback() {
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


    private fun saveRecentEmojis(emojis: List<String>) {
        val arr = org.json.JSONArray()
        val now = System.currentTimeMillis()
        for (emoji in emojis) {
            val obj = org.json.JSONObject()
            obj.put("emoji", emoji)
            obj.put("ts", now)
            arr.put(obj)
        }
        scope.launch { userPreferences.setRecentEmojis(arr.toString()) }
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
