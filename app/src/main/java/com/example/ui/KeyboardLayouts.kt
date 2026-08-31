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
    val NumbersRow = listOf(
        KeyModel("1", popupCandidates = listOf("১", "¹", "½")),
        KeyModel("2", popupCandidates = listOf("২", "²", "⅓")),
        KeyModel("3", popupCandidates = listOf("৩", "³", "¼")),
        KeyModel("4", popupCandidates = listOf("৪", "⁴", "¾")),
        KeyModel("5", popupCandidates = listOf("৫", "⅝")),
        KeyModel("6", popupCandidates = listOf("৬", "⅞")),
        KeyModel("7", popupCandidates = listOf("৭")),
        KeyModel("8", popupCandidates = listOf("৮")),
        KeyModel("9", popupCandidates = listOf("৯")),
        KeyModel("0", popupCandidates = listOf("০", "∅", "°"))
    )

    val BanglaNumbersRow = listOf(
        KeyModel("১", popupCandidates = listOf("1")),
        KeyModel("২", popupCandidates = listOf("2")),
        KeyModel("৩", popupCandidates = listOf("3")),
        KeyModel("৪", popupCandidates = listOf("4")),
        KeyModel("৫", popupCandidates = listOf("5")),
        KeyModel("৬", popupCandidates = listOf("6")),
        KeyModel("৭", popupCandidates = listOf("7")),
        KeyModel("৮", popupCandidates = listOf("8")),
        KeyModel("৯", popupCandidates = listOf("9")),
        KeyModel("০", popupCandidates = listOf("0"))
    )

    val SymbolsRow1 = listOf(
        KeyModel("@", popupCandidates = listOf("å")),
        KeyModel("#", popupCandidates = listOf("№")),
        KeyModel("$", popupCandidates = listOf("৳", "€", "£", "¥", "₹")),
        KeyModel("%", popupCandidates = listOf("‰")),
        KeyModel("&", popupCandidates = listOf("§")),
        KeyModel("-", popupCandidates = listOf("–", "—", "_")),
        KeyModel("+", popupCandidates = listOf("±")),
        KeyModel("(", popupCandidates = listOf("[", "{", "<")),
        KeyModel(")", popupCandidates = listOf("]", "}", ">")),
        KeyModel("/", popupCandidates = listOf("\\", "÷"))
    )

    val SymbolsRow2 = listOf(
        KeyModel("*", popupCandidates = listOf("†", "‡", "★")),
        KeyModel("\"", popupCandidates = listOf("“", "”", "„")),
        KeyModel("'", popupCandidates = listOf("‘", "’", "‚")),
        KeyModel(":", popupCandidates = listOf("::")),
        KeyModel(";", popupCandidates = listOf(";:")),
        KeyModel("!", popupCandidates = listOf("¡")),
        KeyModel("?", popupCandidates = listOf("¿")),
        KeyModel("=", popupCandidates = listOf("≠", "≈", "≡")),
        KeyModel("_", popupCandidates = listOf("–", "—")),
        KeyModel("\\", popupCandidates = listOf("/"))
    )

    val SymbolsRow3 = listOf(
        KeyModel("~", popupCandidates = listOf("≈")),
        KeyModel("`", popupCandidates = listOf("′")),
        KeyModel("|", popupCandidates = listOf("¦")),
        KeyModel("^", popupCandidates = listOf("↑")),
        KeyModel("<", popupCandidates = listOf("≤", "«")),
        KeyModel(">", popupCandidates = listOf("≥", "»")),
        KeyModel("{", popupCandidates = listOf("(")),
        KeyModel("}", popupCandidates = listOf(")")),
        KeyModel("[", popupCandidates = listOf("(")),
        KeyModel("]", popupCandidates = listOf(")"))
    )

    val SymbolsRow4 = listOf(
        KeyModel("€", popupCandidates = listOf("₠")),
        KeyModel("£", popupCandidates = listOf("₺")),
        KeyModel("¥", popupCandidates = listOf("元", "円")),
        KeyModel("₹", popupCandidates = listOf("৳")),
        KeyModel("৳", popupCandidates = listOf("$")),
        KeyModel("÷", popupCandidates = listOf("⁄")),
        KeyModel("≠", popupCandidates = listOf("≉")),
        KeyModel("≈", popupCandidates = listOf("≃")),
        KeyModel("★", popupCandidates = listOf("☆", "✦")),
        KeyModel("\\", popupCandidates = listOf("|"))
    )

    val BanglaSymbolsRow1 = listOf(
        KeyModel("।", popupCandidates = listOf(".", "॥")),
        KeyModel("৳", popupCandidates = listOf("$", "€", "£", "¥")),
        KeyModel("অ", popupCandidates = listOf("আ", "া")),
        KeyModel("ই", popupCandidates = listOf("ঈ", "ি", "ী")),
        KeyModel("উ", popupCandidates = listOf("ঊ", "ু", "ূ")),
        KeyModel("এ", popupCandidates = listOf("ঐ", "ে", "ৈ")),
        KeyModel("ও", popupCandidates = listOf("ঔ", "ো", "ৌ")),
        KeyModel("ঋ", popupCandidates = listOf("ৃ")),
        KeyModel("্", popupCandidates = listOf("্")),
        KeyModel("ঁ", popupCandidates = listOf("ং", "ঃ"))
    )
}
