package com.example.ui

enum class KeyboardMode {
    ENGLISH,
    BANGLA_PHONETIC,
    BANGLA_JATIYO,
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

    // QWERTY English Rows
    val EnglishRow1 = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p").map { KeyModel(it) }
    val EnglishRow2 = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l").map { KeyModel(it) }
    val EnglishRow3 = listOf("z", "x", "c", "v", "b", "n", "m").map { KeyModel(it) }

    // Bangla Phonetic QWERTY layout (using standard Latin mappings for Ridmik-class transliteration)
    val BanglaPhoneticRow1 = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p").map { 
        KeyModel(label = it, popupCandidates = getBanglaPopups(it)) 
    }
    val BanglaPhoneticRow2 = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l").map { 
        KeyModel(label = it, popupCandidates = getBanglaPopups(it)) 
    }
    val BanglaPhoneticRow3 = listOf("z", "x", "c", "v", "b", "n", "m").map { 
        KeyModel(label = it, popupCandidates = getBanglaPopups(it)) 
    }

    // Bangla Jatiyo (National Layout)
    val BanglaJatiyoRow1 = listOf("ৌ", "ৈ", "া", "ী", "ূ", "ব", "হ", "গ", "ড", "ড়").map { KeyModel(it) }
    val BanglaJatiyoRow2 = listOf("ো", "ে", "ি", "ু", "্", "প", "র", "ক", "ত", "চ").map { KeyModel(it) }
    val BanglaJatiyoRow3 = listOf("ং", "ম", "ন", "ব", "ল", "স", "য", "দ").map { KeyModel(it) }

    // Arabic Layout
    val ArabicRow1 = listOf("ض", "ص", "ث", "ق", "ف", "غ", "ع", "ه", "خ", "ح", "ج").map { KeyModel(it) }
    val ArabicRow2 = listOf("ش", "س", "ي", "ب", "ل", "ا", "ت", "ن", "م", "ك", "ط").map { KeyModel(it) }
    val ArabicRow3 = listOf("ئ", "ء", "ؤ", "ر", "لا", "ى", "ة", "و", "ز", "ظ").map { KeyModel(it) }

    // Symbols & Numbers
    val NumbersRow = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").map { KeyModel(it) }
    val SymbolsRow1 = listOf("@", "#", "$", "%", "&", "-", "+", "(", ")", "/").map { KeyModel(it) }
    val SymbolsRow2 = listOf("*", "\"", "'", ":", ";", "!", "?", "=", "_", "\\").map { KeyModel(it) }
    val SymbolsRow3 = listOf("~", "`", "|", "^", "<", ">", "{", "}", "[", "]").map { KeyModel(it) }

    private fun getBanglaPopups(latinKey: String): List<String> {
        return when (latinKey) {
            "k" -> listOf("ক", "খ")
            "g" -> listOf("গ", "ঘ", "ঙ")
            "c" -> listOf("চ", "ছ")
            "j" -> listOf("জ", "ঝ", "ঞ")
            "t" -> listOf("ত", "থ", "ট", "ঠ")
            "d" -> listOf("দ", "ধ", "ড", "ঢ")
            "n" -> listOf("ন", "ণ")
            "p" -> listOf("প", "ফ")
            "b" -> listOf("ব", "ভ")
            "m" -> listOf("ম")
            "r" -> listOf("র", "ড়", "ঢ়")
            "s" -> listOf("স", "শ", "ষ")
            "a" -> listOf("অ", "আ", "া")
            "i" -> listOf("ই", "ঈ", "ি", "ী")
            "u" -> listOf("উ", "ঊ", "ু", "ূ")
            "e" -> listOf("এ", "ে")
            "o" -> listOf("ও", "ো")
            else -> emptyList()
        }
    }
}
