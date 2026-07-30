package com.example.ui

object BanglaLayout {

    // ─────────────────────────────────────────────────────────────────
    //  PHONETIC MODE  (Latin keys — triggers BanglaPhoneticEngine)
    //  Same QWERTY layout, long-press shows Bangla equivalents as hint
    // ─────────────────────────────────────────────────────────────────

    val PhoneticRow1 = listOf(
        KeyModel("q",  popupCandidates = listOf("ক", "খ", "ক্ষ")),
        KeyModel("w",  popupCandidates = listOf("ও", "ওয়")),
        KeyModel("e",  popupCandidates = listOf("এ", "ে", "ঈ", "ই")),
        KeyModel("r",  popupCandidates = listOf("র", "ড়", "ঢ়", "ৃ")),
        KeyModel("t",  popupCandidates = listOf("ত", "থ", "ট", "ঠ", "ৎ")),
        KeyModel("y",  popupCandidates = listOf("য়", "য", "য্য")),
        KeyModel("u",  popupCandidates = listOf("উ", "ঊ", "ু", "ূ")),
        KeyModel("i",  popupCandidates = listOf("ই", "ঈ", "ি", "ী")),
        KeyModel("o",  popupCandidates = listOf("ও", "ো", "ঔ", "ৌ")),
        KeyModel("p",  popupCandidates = listOf("প", "ফ", "প্র"))
    )

    val PhoneticRow2 = listOf(
        KeyModel("a",  popupCandidates = listOf("অ", "আ", "া", "ঁ")),
        KeyModel("s",  popupCandidates = listOf("স", "শ", "ষ")),
        KeyModel("d",  popupCandidates = listOf("দ", "ধ", "ড", "ঢ")),
        KeyModel("f",  popupCandidates = listOf("ফ", "ফ্র")),
        KeyModel("g",  popupCandidates = listOf("গ", "ঘ", "ঙ")),
        KeyModel("h",  popupCandidates = listOf("হ", "ঃ")),
        KeyModel("j",  popupCandidates = listOf("জ", "ঝ", "ঞ")),
        KeyModel("k",  popupCandidates = listOf("ক", "খ")),
        KeyModel("l",  popupCandidates = listOf("ল", "ল্ল"))
    )

    val PhoneticRow3 = listOf(
        KeyModel("z",  popupCandidates = listOf("য", "জ", "ঝ")),
        KeyModel("x",  popupCandidates = listOf("ক্স", "ক্ষ")),
        KeyModel("c",  popupCandidates = listOf("চ", "ছ")),
        KeyModel("v",  popupCandidates = listOf("ভ", "ব")),
        KeyModel("b",  popupCandidates = listOf("ব", "ভ", "ব্র")),
        KeyModel("n",  popupCandidates = listOf("ন", "ণ")),
        KeyModel("m",  popupCandidates = listOf("ম", "ম্ব", "ম্প"))
    )

    // ─────────────────────────────────────────────────────────────────
    //  জাতীয় MODE — Normal Layout
    // ─────────────────────────────────────────────────────────────────

    val JatiyoRow1 = listOf(
        KeyModel("ব",   popupCandidates = listOf("ভ", "ব্র", "ব্ল", "ব্ব")),
        KeyModel("হ",   popupCandidates = listOf("হ্ম", "হ্ন", "হ্ল", "হ্ব")),
        KeyModel("গ",   popupCandidates = listOf("ঘ", "ঙ", "গ্র", "গ্ন", "গ্ম")),
        KeyModel("প",   popupCandidates = listOf("ফ", "প্র", "প্ল", "প্ত", "প্ন", "প্স")),
        KeyModel("ড",   popupCandidates = listOf("ঢ", "ড়", "ঢ়", "ড্ড", "ড্র")),
        KeyModel("ক",   popupCandidates = listOf("খ", "ক্ষ", "ক্র", "ক্ল", "ক্ত", "ক্ক")),
        KeyModel("ত",   popupCandidates = listOf("থ", "ট", "ঠ", "ৎ", "ত্ত", "ত্র", "ত্ব")),
        KeyModel("চ",   popupCandidates = listOf("ছ", "চ্চ", "চ্ছ", "চ্ব")),
        KeyModel("ট",   popupCandidates = listOf("ঠ", "ট্ট", "ট্র")),
        KeyModel("ড়",  popupCandidates = listOf("ঢ়", "ড়্ড়"))
    )

    val JatiyoRow2 = listOf(
        KeyModel("া",   popupCandidates = listOf("আ", "অ", "ো", "ৌ", "ৈ")),
        KeyModel("ি",   popupCandidates = listOf("ই", "ী", "ঈ")),
        KeyModel("ু",   popupCandidates = listOf("উ", "ূ", "ঊ", "ৃ")),
        KeyModel("ে",   popupCandidates = listOf("এ", "ো", "ৈ")),
        KeyModel("ো",  popupCandidates = listOf("ো", "ৌ", "এ")),
        KeyModel("্",   popupCandidates = listOf("্ক", "্ত", "্ন", "্র", "্ব", "্ম", "্য", "্ল")),
        KeyModel("র",   popupCandidates = listOf("ড়", "ঢ়", "র্", "্র")),
        KeyModel("ন",   popupCandidates = listOf("ণ", "ন্ত", "ন্দ", "ন্ন", "ন্ম", "ন্ব")),
        KeyModel("ম",   popupCandidates = listOf("ম্প", "ম্ব", "ম্ভ", "ম্ম", "ম্ল", "ম্ন")),
        KeyModel("ল",   popupCandidates = listOf("ল্ল", "ল্ক", "ল্প", "ল্ট"))
    )

    val JatiyoRow3 = listOf(
        KeyModel("ং",   popupCandidates = listOf("ঙ", "ঙ্ক", "ঙ্গ")),
        KeyModel("ঃ",   popupCandidates = listOf("ঁ", "ঃ")),
        KeyModel("ঁ",   popupCandidates = listOf("ঁ", "ং", "ঃ")),
        KeyModel("য",   popupCandidates = listOf("য়", "য্য", "্য")),
        KeyModel("জ",   popupCandidates = listOf("ঝ", "ঞ", "জ্জ", "জ্ঝ", "জ্ব")),
        KeyModel("স",   popupCandidates = listOf("শ", "ষ", "স্ত", "স্থ", "স্ন", "স্ব", "স্ম")),
        KeyModel("শ",   popupCandidates = listOf("ষ", "শ্চ", "শ্ন", "শ্ম", "শ্ল", "শ্র")),
        KeyModel("দ",   popupCandidates = listOf("ধ", "দ্দ", "দ্ধ", "দ্ব", "দ্র", "দ্ম"))
    )

    // ─────────────────────────────────────────────────────────────────
    //  জাতীয় MODE — Shifted Layout (মহাপ্রাণ বর্ণ ও স্বরবর্ণ)
    // ─────────────────────────────────────────────────────────────────

    val JatiyoShiftRow1 = listOf(
        KeyModel("ভ",   popupCandidates = listOf("ব", "ভ্র", "ভ্ন")),
        KeyModel("ঋ",   popupCandidates = listOf("ৃ")),
        KeyModel("ঘ",   popupCandidates = listOf("গ", "ঘ্ন")),
        KeyModel("ফ",   popupCandidates = listOf("প", "ফ্র")),
        KeyModel("ঢ",   popupCandidates = listOf("ড", "ঢ্র")),
        KeyModel("খ",   popupCandidates = listOf("ক", "খ্য")),
        KeyModel("থ",   popupCandidates = listOf("ত", "থ্য")),
        KeyModel("ছ",   popupCandidates = listOf("চ")),
        KeyModel("ঠ",   popupCandidates = listOf("ট", "ঠ্য")),
        KeyModel("ঢ়",  popupCandidates = listOf("ড়"))
    )

    val JatiyoShiftRow2 = listOf(
        KeyModel("অ",   popupCandidates = listOf("আ", "া")),
        KeyModel("ঈ",   popupCandidates = listOf("ই", "ী")),
        KeyModel("ঊ",   popupCandidates = listOf("উ", "ূ")),
        KeyModel("ঐ",   popupCandidates = listOf("এ", "ৈ")),
        KeyModel("ঔ",   popupCandidates = listOf("ও", "ৌ")),
        KeyModel("্",   popupCandidates = listOf("্ক", "্ত", "্ন", "্র")),
        KeyModel("ৃ",   popupCandidates = listOf("ঋ")),
        KeyModel("ণ",   popupCandidates = listOf("ন", "ণ্ণ")),
        KeyModel("ম",   popupCandidates = listOf("ম্ম", "ম্ন")),
        KeyModel("ল",   popupCandidates = listOf("ল্ল"))
    )

    val JatiyoShiftRow3 = listOf(
        KeyModel("ঙ",   popupCandidates = listOf("ং", "ঙ্ক")),
        KeyModel("ঞ",   popupCandidates = listOf("ঞ্চ", "ঞ্ছ", "ঞ্জ")),
        KeyModel("ৎ",   popupCandidates = listOf("ত")),
        KeyModel("য়",  popupCandidates = listOf("য")),
        KeyModel("ঝ",   popupCandidates = listOf("জ")),
        KeyModel("ষ",   popupCandidates = listOf("ষ্প", "ষ্ট")),
        KeyModel("শ",   popupCandidates = listOf("শ্চ", "শ্র")),
        KeyModel("ধ",   popupCandidates = listOf("দ", "ধ্ন", "ধ্ব"))
    )

    // ─────────────────────────────────────────────────────────────────
    //  AVRO MODE — same QWERTY keys but uses AvroPhoneticEngine
    // ─────────────────────────────────────────────────────────────────

    val AvroRow1 = listOf(
        KeyModel("q",  popupCandidates = listOf("ক", "ক্ষ")),
        KeyModel("w",  popupCandidates = listOf("ও", "ওয়")),
        KeyModel("e",  popupCandidates = listOf("এ", "ে", "ঈ")),
        KeyModel("r",  popupCandidates = listOf("র", "ড়", "ৃ")),
        KeyModel("t",  popupCandidates = listOf("ত", "থ", "ট", "ঠ")),
        KeyModel("y",  popupCandidates = listOf("য়", "য")),
        KeyModel("u",  popupCandidates = listOf("উ", "ু", "ঊ")),
        KeyModel("i",  popupCandidates = listOf("ই", "ি", "ঈ")),
        KeyModel("o",  popupCandidates = listOf("ও", "ো", "ঔ")),
        KeyModel("p",  popupCandidates = listOf("প", "ফ"))
    )

    val AvroRow2 = listOf(
        KeyModel("a",  popupCandidates = listOf("অ", "আ", "া")),
        KeyModel("s",  popupCandidates = listOf("স", "শ", "ষ")),
        KeyModel("d",  popupCandidates = listOf("দ", "ধ", "ড")),
        KeyModel("f",  popupCandidates = listOf("ফ")),
        KeyModel("g",  popupCandidates = listOf("গ", "ঘ", "ঙ")),
        KeyModel("h",  popupCandidates = listOf("হ", "ঃ")),
        KeyModel("j",  popupCandidates = listOf("জ", "ঝ")),
        KeyModel("k",  popupCandidates = listOf("ক", "খ")),
        KeyModel("l",  popupCandidates = listOf("ল"))
    )

    val AvroRow3 = listOf(
        KeyModel("z",  popupCandidates = listOf("য", "জ")),
        KeyModel("x",  popupCandidates = listOf("ক্স")),
        KeyModel("c",  popupCandidates = listOf("ক", "চ")),
        KeyModel("v",  popupCandidates = listOf("ভ")),
        KeyModel("b",  popupCandidates = listOf("ব", "ভ")),
        KeyModel("n",  popupCandidates = listOf("ন", "ণ", "ং")),
        KeyModel("m",  popupCandidates = listOf("ম", "ং"))
    )
}
