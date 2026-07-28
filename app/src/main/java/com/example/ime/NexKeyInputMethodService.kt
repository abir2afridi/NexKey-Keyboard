package com.example.ime

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.InputType
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import com.example.clipboard.ClipboardManager
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
    private var lastSpaceTime = 0L

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

    private val predictionEngine = PredictionEngine()
    private var speechRecognizer: SpeechRecognizer? = null
    private lateinit var userPreferences: UserPreferences
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    private var keyboardView: View? = null

    override fun onCreate() {
        super.onCreate()
        ClipboardManager.init(this)
        predictionEngine.init(this)
        userPreferences = UserPreferences(this)

        scope.launch {
            launch {
                userPreferences.theme.collectLatest { savedTheme ->
                    currentTheme = try {
                        val preset = ThemePreset.valueOf(savedTheme)
                        when (preset) {
                            ThemePreset.DARK_NEON -> KeyboardTheme.DarkNeon
                            ThemePreset.LIGHT_MINIMAL -> KeyboardTheme.LightMinimal
                            ThemePreset.AMOLED_BLACK -> KeyboardTheme.AmoledBlack
                            ThemePreset.EMERALD_GREEN -> KeyboardTheme.EmeraldGreen
                            else -> KeyboardTheme.DarkNeon
                        }
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
                    suggestions = candidates,
                    actionLabel = actionLabel,
                    showNumberRow = showNumRow,
                    hideLongPressHints = hideLongPressHints,
                    keyboardHeightPortrait = kbHeightPortrait,
                    oneHandedWidth = oneHandedWidth,
                    isIncognito = isIncognito,
                    isPasswordField = isPasswordField,
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
                    onIncognitoToggle = { toggleIncognito() }
                )
            }
        }
        keyboardView = composeView
        return composeView
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
        composingBuffer = ""
        candidates = emptyList()

        val imeAction = info?.imeOptions?.let { it and EditorInfo.IME_MASK_ACTION }
        actionLabel = when (imeAction) {
            EditorInfo.IME_ACTION_SEARCH -> "Search"
            EditorInfo.IME_ACTION_GO -> "Go"
            EditorInfo.IME_ACTION_SEND -> "Send"
            EditorInfo.IME_ACTION_NEXT -> "Next"
            EditorInfo.IME_ACTION_DONE -> "Done"
            else -> "↵"
        }

        detectSensitiveField(info)
    }

    private fun playFeedback() {
        if (hapticsEnabled) {
            keyboardView?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        }
        if (soundEnabled) {
            val am = getSystemService(android.content.Context.AUDIO_SERVICE) as android.media.AudioManager
            am.playSoundEffect(android.media.AudioManager.FX_KEYPRESS_STANDARD)
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
        val ic = currentInputConnection ?: return

        if (!isPasswordField && currentMode == KeyboardMode.BANGLA_PHONETIC && key.all { it.isLetter() || it == '.' || it == '^' }) {
            composingBuffer += key
            val parsedBangla = BanglaPhoneticEngine.parse(composingBuffer)
            ic.setComposingText(parsedBangla, 1)
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
                val parsed = BanglaPhoneticEngine.parse(composingBuffer)
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
            val finalWord = BanglaPhoneticEngine.parse(composingBuffer)
            ic.beginBatchEdit()
            ic.commitText("$finalWord ", 1)
            ic.endBatchEdit()
            if (!isSensitiveField) {
                predictionEngine.learnWord(finalWord, isBangla = true)
                if (!isIncognito) {
                    scope.launch { userPreferences.incrementStats(words = 1, chars = finalWord.length + 1) }
                }
            }
            composingBuffer = ""
            candidates = emptyList()
        } else {
            val now = System.currentTimeMillis()
            if (smartPuncEnabled && now - lastSpaceTime < 400) {
                ic.beginBatchEdit()
                ic.deleteSurroundingText(1, 0)
                ic.commitText(". ", 1)
                ic.endBatchEdit()
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
            val finalWord = BanglaPhoneticEngine.parse(composingBuffer)
            ic.beginBatchEdit()
            ic.commitText(finalWord, 1)
            ic.endBatchEdit()
            composingBuffer = ""
            candidates = emptyList()
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
        currentMode = newMode
        scope.launch { userPreferences.setLanguage(newMode) }
    }

    private fun commitSuggestion(word: String) {
        val ic = currentInputConnection ?: return
        ic.beginBatchEdit()
        ic.commitText("$word ", 1)
        ic.endBatchEdit()
        if (!isSensitiveField) {
            predictionEngine.learnWord(word, isBangla = currentMode == KeyboardMode.BANGLA_PHONETIC)
        }
        composingBuffer = ""
        candidates = emptyList()
    }

    private fun updateCandidates(query: String) {
        if (isPasswordField || isSensitiveField) {
            candidates = emptyList()
            return
        }
        candidates = predictionEngine.getPredictions(
            prefix = query,
            isBangla = (currentMode == KeyboardMode.BANGLA_PHONETIC)
        )
    }

    private fun commitComposingBuffer() {
        val ic = currentInputConnection ?: return
        if (composingBuffer.isNotEmpty()) {
            val word = BanglaPhoneticEngine.parse(composingBuffer)
            ic.beginBatchEdit()
            ic.commitText(word, 1)
            ic.endBatchEdit()
            composingBuffer = ""
            candidates = emptyList()
        }
    }

    private fun toggleTheme() {
        currentTheme = when (currentTheme.preset) {
            ThemePreset.DARK_NEON -> KeyboardTheme.LightMinimal
            ThemePreset.LIGHT_MINIMAL -> KeyboardTheme.AmoledBlack
            ThemePreset.AMOLED_BLACK -> KeyboardTheme.EmeraldGreen
            else -> KeyboardTheme.DarkNeon
        }
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

    private fun startVoiceInput() {
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
                    if (currentMode == KeyboardMode.BANGLA_PHONETIC || currentMode == KeyboardMode.BANGLA_JATIYO) "bn-BD" else "en-US"
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
                    Toast.makeText(this@NexKeyInputMethodService, "Voice input error: $error", Toast.LENGTH_SHORT).show()
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
