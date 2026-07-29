package com.example.ui

object ArabicLayout {
    val Row1 = listOf("ض", "ص", "ث", "ق", "ف", "غ", "ع", "ه", "خ", "ح", "ج").map { KeyModel(it) }
    val Row2 = listOf("ش", "س", "ي", "ب", "ل", "ا", "ت", "ن", "م", "ك", "ط").map { KeyModel(it) }
    val Row3 = listOf("ئ", "ء", "ؤ", "ر", "لا", "ى", "ة", "و", "ز", "ظ").map { KeyModel(it) }
}
