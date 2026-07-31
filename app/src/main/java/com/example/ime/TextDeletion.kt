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

private fun deleteGraphemeBackward(ic: InputConnection) {
    ic.deleteSurroundingText(1, 0)
}
