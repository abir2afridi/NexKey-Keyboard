package com.example.ime

import android.view.inputmethod.InputConnection
import com.example.ui.KeyboardMode

internal fun NexKeyInputMethodService.handleBackspace() {
    playFeedback()
    mayStartSentence = true

    if (emojiSearchActive) {
        if (emojiSearchQuery.isNotEmpty()) {
            emojiSearchQuery = emojiSearchQuery.dropLast(1)
        }
        return
    }

    val ic = currentInputConnection ?: return

    if (composingBuffer.isNotEmpty()) {
        composingBuffer = composingBuffer.dropLast(1)
        if (speedMeter.lastPressedWord.isNotEmpty()) {
            speedMeter = speedMeter.copy(lastPressedWord = speedMeter.lastPressedWord.dropLast(1))
        }
        if (composingBuffer.isNotEmpty()) {
            val parsed = parseComposing(currentMode, composingBuffer)
            ic.setComposingText(parsed, 1)
            updateCandidates(composingBuffer)
        } else {
            // FIX (double-press delete bug): finishComposingText() commits the last composing
            // character into the editor instead of deleting it, so the final character (or the
            // final word during hold-delete) always survived and needed one extra press.
            // setComposingText("", 1) replaces the composing region with nothing — it actually
            // removes the last character from the editor.
            ic.setComposingText("", 1)
            candidates = emptyList()
        }
    } else {
        if (speedMeter.lastPressedWord.isNotEmpty()) {
            speedMeter = speedMeter.copy(lastPressedWord = speedMeter.lastPressedWord.dropLast(1))
        }
        deleteGraphemeBackward(ic)
    }
}

internal fun NexKeyInputMethodService.handleBackspaceWord() {
    playFeedback()
    mayStartSentence = true

    if (emojiSearchActive) {
        emojiSearchQuery = ""
        return
    }

    val ic = currentInputConnection ?: return

    if (composingBuffer.isNotEmpty()) {
        composingBuffer = ""
        speedMeter = speedMeter.copy(lastPressedWord = "")
        // FIX (words left behind during hold-delete): finishComposingText() kept the composing
        // word in the editor, so the next repeat tick (or a finger release) left it behind.
        // setComposingText("", 1) removes the whole composing word from the editor in one tick.
        ic.setComposingText("", 1)
        candidates = emptyList()
        return
    } else {
        val before = ic.getTextBeforeCursor(256, 0)?.toString() ?: return
        if (before.isEmpty()) return
        val wordEnd = before.length
        var wordStart = wordEnd
        while (wordStart > 0 && before[wordStart - 1].isWhitespace()) wordStart--
        while (wordStart > 0 && !before[wordStart - 1].isWhitespace()) wordStart--
        val deleteCount = wordEnd - wordStart
        if (deleteCount > 0) {
            ic.deleteSurroundingText(deleteCount, 0)
        }
        speedMeter = speedMeter.copy(lastPressedWord = "")
    }
}

// FIX (selection-delete bug): deleteSurroundingText(1, 0) only deletes ONE char BEFORE the
// cursor and completely ignores an active text selection. If the user selected text
// (e.g. long-press select-all) and presses delete, nothing was removed. A selection must
// be deleted by replacing it with an empty string via commitText("", 1) — commitText
// replaces the current selection (or composing region) in every IME client.
//
// FIX (emoji "?" bug): emojis are 2 UTF-16 code units (surrogate pair). Deleting only 1
// unit leaves a broken half that renders as "?" and needs a second press. deleteGrapheme
// deletes the full grapheme (surrogate pair, ZWJ emoji sequence, variation selectors,
// combining marks) in a single press.
private fun deleteGraphemeBackward(ic: InputConnection) {
    val selectedText = ic.getSelectedText(0)
    if (selectedText != null && selectedText.isNotEmpty()) {
        ic.commitText("", 1)
        return
    }
    val before = ic.getTextBeforeCursor(32, 0)?.toString() ?: return
    if (before.isEmpty()) return
    ic.deleteSurroundingText(graphemeBackwardLength(before), 0)
}

private fun graphemeBackwardLength(text: String): Int {
    var i = text.length
    var consumed = 0
    // 1. Trailing marks / variation selectors (tail of the cluster, e.g. "a" + U+0301 = é)
    while (i > 0) {
        val c = text[i - 1]
        val t = Character.getType(c)
        if (c == '\uFE0F' || c == '\uFE0E' || t == Character.NON_SPACING_MARK.toInt() ||
            t == Character.COMBINING_SPACING_MARK.toInt() || t == Character.ENCLOSING_MARK.toInt()
        ) {
            i--
            consumed++
        } else {
            break
        }
    }
    // 2. Base code point, then keep going only if chained by ZWJ (family/keycap emoji)
    while (i > 0) {
        val c = text[i - 1]
        if (Character.isLowSurrogate(c) && i >= 2 && Character.isHighSurrogate(text[i - 2])) {
            i -= 2
            consumed += 2
        } else {
            i--
            consumed++
        }
        if (i > 0 && text[i - 1] == '\u200D') {
            i--
            consumed++
        } else {
            break
        }
    }
    return consumed
}
