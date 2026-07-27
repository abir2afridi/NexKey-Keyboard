package com.example.ime

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.example.clipboard.ClipboardManager
import com.example.engine.BanglaPhoneticEngine
import com.example.engine.PredictionEngine
import com.example.theme.KeyboardTheme
import com.example.ui.KeyboardComposeView
import com.example.ui.KeyboardMode
import com.example.ui.ShiftState

/**
 * NexKey InputMethodService - Main System Keyboard Implementation.
 * Manages InputConnection, Composing Region, Language Switching,
 * Transliteration Engine, Predictions, Voice Input, and Compose View.
 */
class NexKeyInputMethodService : LifecycleInputMethodService() {

    private var currentMode by mutableStateOf(KeyboardMode.BANGLA_PHONETIC)
    private var shiftState by mutableStateOf(ShiftState.OFF)
    private var currentTheme by mutableStateOf(KeyboardTheme.DarkNeon)
    private var composingBuffer by mutableStateOf("")
    private var candidates by mutableStateOf<List<String>>(emptyList())
    private var actionLabel by mutableStateOf("↵")

    private val predictionEngine = PredictionEngine()
    private var speechRecognizer: SpeechRecognizer? = null

    override fun onCreateInputView(): View {
        val composeView = ComposeView(this).apply {
            setLifecycleOwners()
            setContent {
                KeyboardComposeView(
                    mode = currentMode,
                    shiftState = shiftState,
                    theme = currentTheme,
                    composingText = composingBuffer,
                    suggestions = candidates,
                    actionLabel = actionLabel,
                    onKeyTap = { key -> handleKeyTap(key) },
                    onBackspaceTap = { handleBackspace() },
                    onSpaceTap = { handleSpace() },
                    onEnterTap = { handleEnter() },
                    onShiftTap = { handleShiftToggle() },
                    onModeChange = { newMode -> currentMode = newMode },
                    onSuggestionSelect = { word -> commitSuggestion(word) },
                    onVoiceClick = { startVoiceInput() },
                    onThemeToggle = { toggleTheme() },
                    onOpenSettings = { launchSettingsActivity() },
                    onAiAction = { action -> handleAiAction(action) }
                )
            }
        }
        return composeView
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        composingBuffer = ""
        candidates = emptyList()

        // Determine Action button label from EditorInfo imeOptions
        val imeAction = info?.imeOptions?.let { it and EditorInfo.IME_MASK_ACTION }
        actionLabel = when (imeAction) {
            EditorInfo.IME_ACTION_SEARCH -> "Search"
            EditorInfo.IME_ACTION_GO -> "Go"
            EditorInfo.IME_ACTION_SEND -> "Send"
            EditorInfo.IME_ACTION_NEXT -> "Next"
            EditorInfo.IME_ACTION_DONE -> "Done"
            else -> "↵"
        }
    }

    private fun handleKeyTap(key: String) {
        val ic = currentInputConnection ?: return

        if (currentMode == KeyboardMode.BANGLA_PHONETIC && key.all { it.isLetter() || it == '.' || it == '^' }) {
            // Append to Latin composing buffer for Phonetic Transliteration
            composingBuffer += key
            val parsedBangla = BanglaPhoneticEngine.parse(composingBuffer)
            ic.setComposingText(parsedBangla, 1)
            updateCandidates(composingBuffer)
        } else {
            // If there's an active composing buffer, commit it first
            if (composingBuffer.isNotEmpty()) {
                commitComposingBuffer()
            }
            ic.commitText(key, 1)
            // Add copied text to clipboard history if it's long
            if (key.length > 5) {
                ClipboardManager.addClip(key)
            }
        }

        // Reset single-tap SHIFT state back to OFF
        if (shiftState == ShiftState.SHIFT) {
            shiftState = ShiftState.OFF
        }
    }

    private fun handleBackspace() {
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
        val ic = currentInputConnection ?: return

        if (composingBuffer.isNotEmpty()) {
            val finalWord = BanglaPhoneticEngine.parse(composingBuffer)
            ic.commitText("$finalWord ", 1)
            predictionEngine.learnWord(finalWord, isBangla = true)
            composingBuffer = ""
            candidates = emptyList()
        } else {
            ic.commitText(" ", 1)
        }
    }

    private fun handleEnter() {
        val ic = currentInputConnection ?: return

        if (composingBuffer.isNotEmpty()) {
            val finalWord = BanglaPhoneticEngine.parse(composingBuffer)
            ic.commitText(finalWord, 1)
            composingBuffer = ""
            candidates = emptyList()
        }

        val info = currentInputEditorInfo
        val imeAction = info?.imeOptions?.let { it and EditorInfo.IME_MASK_ACTION }
        if (imeAction != null && imeAction != EditorInfo.IME_ACTION_NONE) {
            ic.performEditorAction(imeAction)
        } else {
            ic.commitText("\n", 1)
        }
    }

    private fun handleShiftToggle() {
        shiftState = when (shiftState) {
            ShiftState.OFF -> ShiftState.SHIFT
            ShiftState.SHIFT -> ShiftState.CAPS_LOCK
            ShiftState.CAPS_LOCK -> ShiftState.OFF
        }
    }

    private fun commitSuggestion(word: String) {
        val ic = currentInputConnection ?: return
        ic.commitText("$word ", 1)
        if (currentMode == KeyboardMode.BANGLA_PHONETIC) {
            predictionEngine.learnWord(word, isBangla = true)
        } else {
            predictionEngine.learnWord(word, isBangla = false)
        }
        composingBuffer = ""
        candidates = emptyList()
    }

    private fun updateCandidates(query: String) {
        candidates = predictionEngine.getPredictions(
            prefix = query,
            isBangla = (currentMode == KeyboardMode.BANGLA_PHONETIC)
        )
    }

    private fun commitComposingBuffer() {
        val ic = currentInputConnection ?: return
        if (composingBuffer.isNotEmpty()) {
            val word = BanglaPhoneticEngine.parse(composingBuffer)
            ic.commitText(word, 1)
            composingBuffer = ""
            candidates = emptyList()
        }
    }

    private fun toggleTheme() {
        currentTheme = when (currentTheme.preset) {
            com.example.theme.ThemePreset.DARK_NEON -> KeyboardTheme.LightMinimal
            com.example.theme.ThemePreset.LIGHT_MINIMAL -> KeyboardTheme.AmoledBlack
            com.example.theme.ThemePreset.AMOLED_BLACK -> KeyboardTheme.EmeraldGreen
            else -> KeyboardTheme.DarkNeon
        }
    }

    private fun startVoiceInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Voice typing is not available on this device", Toast.LENGTH_SHORT).show()
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
                        currentInputConnection?.commitText(matches[0] + " ", 1)
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

    private fun handleAiAction(prompt: String) {
        val ic = currentInputConnection ?: return
        val selectedText = ic.getSelectedText(0)?.toString() ?: ""
        if (selectedText.isNotEmpty()) {
            // Demonstrate smart AI text transformation
            val result = if (prompt.contains("Rewrite")) {
                "✨ $selectedText"
            } else if (prompt.contains("Grammar")) {
                selectedText.replace("teh", "the").replace("im", "I'm")
            } else if (prompt.contains("Translate")) {
                BanglaPhoneticEngine.parse(selectedText)
            } else {
                "Professional: $selectedText"
            }
            ic.commitText(result, 1)
        } else {
            Toast.makeText(this, "Highlight text first to run AI action!", Toast.LENGTH_SHORT).show()
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
