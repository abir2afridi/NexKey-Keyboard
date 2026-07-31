package com.example.ime

import android.view.inputmethod.InputConnection
import com.example.ui.KeyboardMode

internal fun NexKeyInputMethodService.handleBackspace() {
    playFeedback()

    if (emojiSearchActive) {
        if (emojiSearchQuery.isNotEmpty()) {
            emojiSearchQuery = emojiSearchQuery.dropLast(1)
        }
        return
    }

    val ic = currentInputConnection ?: return

    if (composingBuffer.isNotEmpty()) {
        composingBuffer = composingBuffer.dropLast(1)
        if (composingBuffer.isNotEmpty()) {
            val parsed = parseComposing(currentMode, composingBuffer)
            ic.setComposingText(parsed, 1)
            updateCandidates(composingBuffer)
        } else {
            ic.finishComposingText()
            candidates = emptyList()
        }
    } else {
        deleteGraphemeBackward(ic)
    }
}

// FIX (selection-delete bug): deleteSurroundingText(1, 0) only deletes ONE char BEFORE the
// cursor and completely ignores an active text selection. If the user selected text
// (e.g. long-press select-all) and presses delete, nothing was removed. A selection must
// be deleted by replacing it with an empty string via commitText("", 1) — commitText
// replaces the current selection (or composing region) in every IME client.
private fun deleteGraphemeBackward(ic: InputConnection) {
    val selectedText = ic.getSelectedText(0)
    if (selectedText != null && selectedText.isNotEmpty()) {
        ic.commitText("", 1)
    } else {
        ic.deleteSurroundingText(1, 0)
    }
}
