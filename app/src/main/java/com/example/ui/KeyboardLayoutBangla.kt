package com.example.ui

object BanglaLayout {
    val PhoneticRow1 = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p").map {
        KeyModel(label = it, popupCandidates = getBanglaPopups(it))
    }
    val PhoneticRow2 = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l").map {
        KeyModel(label = it, popupCandidates = getBanglaPopups(it))
    }
    val PhoneticRow3 = listOf("z", "x", "c", "v", "b", "n", "m").map {
        KeyModel(label = it, popupCandidates = getBanglaPopups(it))
    }

    val JatiyoRow1 = listOf("ৌ", "ৈ", "া", "ী", "ূ", "ব", "হ", "গ", "ড", "ড়").map { KeyModel(it) }
    val JatiyoRow2 = listOf("ো", "ে", "ি", "ু", "্", "প", "র", "ক", "ত", "চ").map { KeyModel(it) }
    val JatiyoRow3 = listOf("ং", "ম", "ন", "ব", "ল", "স", "য", "দ").map { KeyModel(it) }

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
