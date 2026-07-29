package com.example.ui

enum class KeyboardMode {
    ENGLISH,
    BANGLA_PHONETIC,
    BANGLA_JATIYO,
    AVRO,
    ARABIC,
    SYMBOLS,
    NUMBERS,
    EMOJI,
    CLIPBOARD
}

enum class ShiftState {
    OFF,
    SHIFT,
    CAPS_LOCK
}

data class KeyModel(
    val label: String,
    val code: String = label,
    val weight: Float = 1.0f,
    val isSpecial: Boolean = false,
    val iconResId: Int? = null,
    val popupCandidates: List<String> = emptyList()
)

object KeyboardLayouts {
    val NumbersRow = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").map { KeyModel(it) }
    val SymbolsRow1 = listOf("@", "#", "$", "%", "&", "-", "+", "(", ")", "/").map { KeyModel(it) }
    val SymbolsRow2 = listOf("*", "\"", "'", ":", ";", "!", "?", "=", "_", "\\").map { KeyModel(it) }
    val SymbolsRow3 = listOf("~", "`", "|", "^", "<", ">", "{", "}", "[", "]").map { KeyModel(it) }
}
