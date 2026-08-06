package com.example.ime

import kotlinx.coroutines.launch

internal fun NexKeyInputMethodService.rememberCommittedWord(word: String) {
    if (word.isBlank()) return
    recentCommittedWords.addLast(word)
    while (recentCommittedWords.size > 3) recentCommittedWords.removeFirst()
}

internal fun NexKeyInputMethodService.updateCandidates(query: String) {
    if (isPasswordField || isSensitiveField) {
        candidates = emptyList()
        return
    }
    val predictions = predictionEngine.getSuggestions(
        prefix = query,
        isBangla = isBanglaMode(currentMode),
        previousWords = recentCommittedWords.toList(),
        showTypedWordFirst = showTypedWordFirstEnabled,
        limit = 4,
        now = System.currentTimeMillis()
    )
    candidates = predictions.map { it.word }
}

internal fun NexKeyInputMethodService.commitSuggestion(word: String) {
    val ic = currentInputConnection ?: return
    // FIX (cursor-jump bug): always commit via ic.commitText — this removes the composing
    // region and places the cursor right after the committed text. Never replace this with
    // setComposingText, and always reset composingBuffer after committing (see
    // onUpdateSelection() for the mid-word cursor-move case).
    ic.beginBatchEdit()
    ic.commitText("$word ", 1)
    ic.endBatchEdit()
    countMeteredWord()
    if (!isSensitiveField) {
        predictionEngine.onWordCommitted(
            word, isBanglaMode(currentMode), recentCommittedWords.toList(), System.currentTimeMillis()
        )
        rememberCommittedWord(word)
        if (nextWordSuggestionsEnabled) {
            candidates = predictionEngine.getNextWordPredictions(
                recentCommittedWords.toList(), isBanglaMode(currentMode), 3, System.currentTimeMillis()
            ).map { it.word }
        }
    }
    composingBuffer = ""
}

internal fun NexKeyInputMethodService.commitComposingBuffer() {
    val ic = currentInputConnection ?: return
    // FIX (cursor-jump bug): commitComposingBuffer may be called when the cursor is NOT at
    // the end of the composing region (e.g. user moved it mid-word). commitText() commits
    // the composed word in place and leaves the cursor where it is — the next keystroke then
    // inserts at the cursor instead of jumping to the end of the word.
    if (composingBuffer.isNotEmpty()) {
        val word = parseComposing(currentMode, composingBuffer)
        countMeteredWord()
        ic.beginBatchEdit()
        ic.commitText(word, 1)
        ic.endBatchEdit()
        if (!isSensitiveField && !isIncognito) {
            predictionEngine.onWordCommitted(
                word, isBanglaMode(currentMode), recentCommittedWords.toList(), System.currentTimeMillis()
            )
            rememberCommittedWord(word)
            scope.launch { userPreferences.incrementStats(words = 1, chars = word.length) }
        }
        composingBuffer = ""
        candidates = emptyList()
    }
}
