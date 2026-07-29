package com.example.ui

object EnglishLayout {
    val Row1 = listOf(
        KeyModel("q", popupCandidates = listOf("1")),
        KeyModel("w", popupCandidates = listOf("2")),
        KeyModel("e", popupCandidates = listOf("3", "é", "è", "ê", "ë", "ē")),
        KeyModel("r", popupCandidates = listOf("4")),
        KeyModel("t", popupCandidates = listOf("5")),
        KeyModel("y", popupCandidates = listOf("6")),
        KeyModel("u", popupCandidates = listOf("7", "ú", "û", "ü", "ù")),
        KeyModel("i", popupCandidates = listOf("8", "í", "î", "ï", "ì")),
        KeyModel("o", popupCandidates = listOf("9", "ó", "ô", "õ", "ö", "ò")),
        KeyModel("p", popupCandidates = listOf("0"))
    )
    val Row2 = listOf(
        KeyModel("a", popupCandidates = listOf("à", "á", "â", "ã", "ä", "å", "æ")),
        KeyModel("s", popupCandidates = listOf("ß", "ś", "š")),
        KeyModel("d", popupCandidates = listOf("ð", "ď")),
        KeyModel("f"),
        KeyModel("g"),
        KeyModel("h"),
        KeyModel("j"),
        KeyModel("k"),
        KeyModel("l", popupCandidates = listOf("ł"))
    )
    val Row3 = listOf(
        KeyModel("z", popupCandidates = listOf("ž", "ź", "ż")),
        KeyModel("x"),
        KeyModel("c", popupCandidates = listOf("ç", "ć", "č")),
        KeyModel("v"),
        KeyModel("b"),
        KeyModel("n", popupCandidates = listOf("ñ", "ń")),
        KeyModel("m")
    )
}
