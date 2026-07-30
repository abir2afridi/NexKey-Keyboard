package com.example.ui

object EnglishLayout {
    val Row1 = listOf(
        // q → 1, !, @, `, ~
        KeyModel("q", popupCandidates = listOf("1", "!", "@", "`", "~")),
        // w → 2, #, $
        KeyModel("w", popupCandidates = listOf("2", "#", "\$")),
        // e → 3, €, £, ¥, é, è, ê, ë, ē
        KeyModel("e", popupCandidates = listOf("3", "€", "£", "¥", "é", "è", "ê", "ë", "ē")),
        // r → 4, %, ®
        KeyModel("r", popupCandidates = listOf("4", "%", "®")),
        // t → 5, +, ©, ™
        KeyModel("t", popupCandidates = listOf("5", "+", "©", "™")),
        // y → 6, ^, ¥, ý
        KeyModel("y", popupCandidates = listOf("6", "^", "¥", "ý")),
        // u → 7, &, ú, û, ü, ù, ū
        KeyModel("u", popupCandidates = listOf("7", "&", "ú", "û", "ü", "ù", "ū")),
        // i → 8, *, í, î, ï, ì, ī
        KeyModel("i", popupCandidates = listOf("8", "*", "í", "î", "ï", "ì", "ī")),
        // o → 9, (, ó, ô, õ, ö, ò, ø, œ
        KeyModel("o", popupCandidates = listOf("9", "(", "ó", "ô", "õ", "ö", "ò", "ø", "œ")),
        // p → 0, ), π, þ
        KeyModel("p", popupCandidates = listOf("0", ")", "π", "þ"))
    )

    val Row2 = listOf(
        // a → @, à, á, â, ã, ä, å, æ, ā
        KeyModel("a", popupCandidates = listOf("@", "à", "á", "â", "ã", "ä", "å", "æ", "ā")),
        // s → $, ß, ś, š
        KeyModel("s", popupCandidates = listOf("\$", "ß", "ś", "š")),
        // d → /, ð, ď
        KeyModel("d", popupCandidates = listOf("/", "ð", "ď")),
        // f → \, £, ƒ
        KeyModel("f", popupCandidates = listOf("\\", "£", "ƒ")),
        // g → =, ğ
        KeyModel("g", popupCandidates = listOf("=", "ğ")),
        // h → #
        KeyModel("h", popupCandidates = listOf("#")),
        // j → ;
        KeyModel("j", popupCandidates = listOf(";")),
        // k → :
        KeyModel("k", popupCandidates = listOf(":")),
        // l → !, ł
        KeyModel("l", popupCandidates = listOf("!", "ł"))
    )

    val Row3 = listOf(
        // z → ?, ž, ź, ż
        KeyModel("z", popupCandidates = listOf("?", "ž", "ź", "ż")),
        // x → "
        KeyModel("x", popupCandidates = listOf("\"")),
        // c → ', ç, ć, č
        KeyModel("c", popupCandidates = listOf("'", "ç", "ć", "č")),
        // v → _, •
        KeyModel("v", popupCandidates = listOf("_", "•")),
        // b → •, ·
        KeyModel("b", popupCandidates = listOf("·", "•")),
        // n → -, ñ, ń
        KeyModel("n", popupCandidates = listOf("-", "ñ", "ń")),
        // m → ., μ
        KeyModel("m", popupCandidates = listOf(".", "μ"))
    )
}
