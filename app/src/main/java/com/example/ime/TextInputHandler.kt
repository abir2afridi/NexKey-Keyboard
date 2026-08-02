package com.example.ime

import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.example.data.TypingAnalytics
import com.example.ui.KeyboardMode
import com.example.ui.ShiftState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal fun NexKeyInputMethodService.handleKeyTap(key: String) {
    playFeedback()

    if (emojiSearchActive) {
        emojiSearchQuery += key
        return
    }

    if (!isIncognito && !isSensitiveField) {
        TypingAnalytics.trackKeyPress()

        // Live Speed Meter Logic
        val now = System.currentTimeMillis()
        if (now - lastKeyPressTime > meterIdleMsState) {
            burstStartTime = now
            burstKeyCount = 0
            burstWordCount = 0
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
            delay(meterIdleMsState.toLong())
            finalizeSpeedWindow()
        }
    }
    val ic = currentInputConnection ?: return

    val isAlphaKey = key.length == 1 && key[0].isLetter()
    val isEmoji = isEmojiKey(key)
    if (shouldCompose(currentMode, isPasswordField, isAlphaKey)) {
        // FIX (cursor-jump bug): composingBuffer is ONLY valid while the cursor sits at
        // the END of the composing region. onUpdateSelection() in NexKeyInputMethodService
        // clears the buffer whenever the user moves the cursor anywhere else (even inside
        // the word). Do NOT "protect" that clearing logic or restore the old
        // "inside-composing" check — without it, editing mid-word (e.g. "aple" + cursor
        // after "p" + "p") replaces the whole composed word and jumps the cursor to the end.
        composingBuffer += key
        val parsed = parseComposing(currentMode, composingBuffer)
        ic.setComposingText(parsed, 1)
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

        if (!isIncognito && !isSensitiveField && isEmoji) {
            TypingAnalytics.trackEmoji(key)
        }

        if (!isIncognito && !isSensitiveField) {
            scope.launch {
                userPreferences.incrementStats(words = 0, chars = 1)
            }
        }

        if (key.length > 5 && !isSensitiveField) {
            com.example.clipboard.ClipboardManager.addClip(key)
        }
    }

    if (shiftState == ShiftState.SHIFT) {
        shiftState = ShiftState.OFF
    }
}

internal fun isEmojiKey(key: String): Boolean {
    if (key.isEmpty()) return false
    return key.codePoints().anyMatch { cp ->
        (cp in 0x1F000..0x1FAFF) ||
        (cp in 0x2600..0x27BF) ||
        (cp in 0x2B00..0x2BFF) ||
        (cp in 0xFE00..0xFE0F) ||
        cp == 0x200D ||
        cp == 0x20E3
    }
}

internal fun NexKeyInputMethodService.isNewSentence(): Boolean {
    val ic = currentInputConnection ?: return true
    val before = ic.getTextBeforeCursor(2, 0) ?: ""
    return before.isEmpty() || before.endsWith(". ") || before.endsWith("! ") || before.endsWith("? ") || before.endsWith("\n") || before.endsWith("। ")
}

internal fun NexKeyInputMethodService.handleSpace() {
    playFeedback()
    val ic = currentInputConnection ?: return

    if (composingBuffer.isNotEmpty()) {
        val rawWord = parseComposing(currentMode, composingBuffer)
        val isBangla = isBanglaMode(currentMode)
        val correctedWord = if (autoCorrectionEnabled && !isSensitiveField) {
            val correction = predictionEngine.getCorrection(rawWord, isBangla)
            if (correction != null && rawWord.length > 2) correction else rawWord
        } else {
            rawWord
        }
        ic.beginBatchEdit()
        ic.commitText("$correctedWord ", 1)
        ic.endBatchEdit()
        countMeteredWord()
        if (!isSensitiveField) {
            predictionEngine.learnWord(correctedWord, isBangla = isBangla)
            predictionEngine.setLastTypedWord(correctedWord)
            if (!isIncognito) {
                scope.launch { userPreferences.incrementStats(words = 1, chars = correctedWord.length + 1) }
            }
            if (nextWordSuggestionsEnabled) {
                candidates = predictionEngine.getNextWordPredictions(isBangla)
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

internal fun NexKeyInputMethodService.handleEnter() {
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

internal fun NexKeyInputMethodService.handleShiftToggle() {
    shiftState = when (shiftState) {
        ShiftState.OFF -> ShiftState.SHIFT
        ShiftState.SHIFT -> ShiftState.CAPS_LOCK
        ShiftState.CAPS_LOCK -> ShiftState.OFF
    }
}

internal fun NexKeyInputMethodService.toggleEmojiSearch() {
    emojiSearchActive = !emojiSearchActive
    if (!emojiSearchActive) {
        emojiSearchQuery = ""
        // Restore previous mode if we changed it for search
        if (currentMode == KeyboardMode.EMOJI && lastTextMode != KeyboardMode.EMOJI) {
            currentMode = lastTextMode
        }
    } else if (currentMode != KeyboardMode.EMOJI) {
        lastTextMode = currentMode
        currentMode = KeyboardMode.EMOJI
    }
}

internal fun NexKeyInputMethodService.handleModeChange(newMode: KeyboardMode) {
    // Close emoji search if active when switching modes
    if (emojiSearchActive) {
        emojiSearchActive = false
        emojiSearchQuery = ""
    }

    if (newMode == KeyboardMode.SYMBOLS || newMode == KeyboardMode.NUMBERS || newMode == KeyboardMode.EMOJI || newMode == KeyboardMode.CLIPBOARD) {
        currentMode = newMode
        return
    }

    val targetMode = if (currentMode == KeyboardMode.SYMBOLS || currentMode == KeyboardMode.NUMBERS || currentMode == KeyboardMode.EMOJI || currentMode == KeyboardMode.CLIPBOARD) {
        if (newMode == KeyboardMode.ENGLISH && lastTextMode != KeyboardMode.ENGLISH) {
            lastTextMode
        } else {
            newMode
        }
    } else {
        newMode
    }

    if (targetMode == KeyboardMode.ENGLISH && currentMode == KeyboardMode.ENGLISH && allowOtherKeyboardsEnabled) {
        val imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.switchToLastInputMethod(window?.window?.decorView?.windowToken)
        return
    }

    lastTextMode = targetMode
    currentMode = targetMode
    scope.launch { userPreferences.setLanguage(targetMode) }
}

internal fun NexKeyInputMethodService.handleCursorMove(direction: Int) {
    val ic = currentInputConnection ?: return
    // Commit composing buffer before moving cursor so typing resumes at new position
    if (composingBuffer.isNotEmpty()) {
        commitComposingBuffer()
    }
    ic.beginBatchEdit()
    ic.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, if (direction > 0) android.view.KeyEvent.KEYCODE_DPAD_RIGHT else android.view.KeyEvent.KEYCODE_DPAD_LEFT))
    ic.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, if (direction > 0) android.view.KeyEvent.KEYCODE_DPAD_RIGHT else android.view.KeyEvent.KEYCODE_DPAD_LEFT))
    ic.endBatchEdit()
}
