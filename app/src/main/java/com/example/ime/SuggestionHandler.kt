package com.example.ime

import kotlinx.coroutines.launch

internal fun NexKeyInputMethodService.updateCandidates(query: String) {
    if (isPasswordField || isSensitiveField) {
        candidates = emptyList()
        return
    }
    val predictions = predictionEngine.getPredictions(
        prefix = query,
        isBangla = isBanglaMode(currentMode),
        showTypedWordFirst = showTypedWordFirstEnabled
    )
    candidates = predictions
}

internal fun NexKeyInputMethodService.commitSuggestion(word: String) {
    val ic = currentInputConnection ?: return
    ic.beginBatchEdit()
    ic.commitText("$word ", 1)
    ic.endBatchEdit()
    if (!isSensitiveField && personalizedSuggestionsEnabled) {
        predictionEngine.learnWord(word, isBangla = isBanglaMode(currentMode))
        predictionEngine.setLastTypedWord(word)
        if (nextWordSuggestionsEnabled) {
            candidates = predictionEngine.getNextWordPredictions(isBanglaMode(currentMode))
        }
    }
    composingBuffer = ""
}

internal fun NexKeyInputMethodService.commitComposingBuffer() {
    val ic = currentInputConnection ?: return
    if (composingBuffer.isNotEmpty()) {
        val word = parseComposing(currentMode, composingBuffer)
        ic.beginBatchEdit()
        ic.commitText(word, 1)
        ic.endBatchEdit()
        if (!isSensitiveField && !isIncognito) {
            predictionEngine.learnWord(word, isBangla = isBanglaMode(currentMode))
            scope.launch { userPreferences.incrementStats(words = 1, chars = word.length) }
        }
        composingBuffer = ""
        candidates = emptyList()
    }
}
