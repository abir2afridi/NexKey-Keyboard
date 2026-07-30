package com.example.ui

// ─────────────────────────────────────────────────────────────────────────────
//  NexKey — বাংলা কীবোর্ড লেআউট ফাইল
//  জাতীয়, জাতীয় শিফট, অভ্র, ফোনেটিক — Gboard/Ridmik-style পেশাদার অ্যারেঞ্জমেন্ট
// ─────────────────────────────────────────────────────────────────────────────

object BanglaLayout {

    // =========================================================================
    //  ★  জাতীয় মোড — Bangladesh National Keyboard (Bijoy-compatible layout)
    //      Row 1 : ব হ গ প ড ক ত চ ট ড় (top row – বাংলা সংখ্যা long-press corner hints)
    //      Row 2 : া ি ু ে ো ্ র ন ম ল
    //      Row 3 : ং ঃ ঁ য জ স শ দ
    // =========================================================================

    val JatiyoRow1 = listOf(
        KeyModel("ব",  popupCandidates = listOf("১", "ভ", "ব্র", "ব্ল", "ব্ব", "ব্ধ")),
        KeyModel("হ",  popupCandidates = listOf("২", "হ্ম", "হ্ন", "হ্ল", "হ্ব", "হ্র")),
        KeyModel("গ",  popupCandidates = listOf("৩", "ঘ", "ঙ", "গ্ন", "গ্ম", "গ্ল", "গ্র", "গ্ধ")),
        KeyModel("প",  popupCandidates = listOf("৪", "ফ", "প্র", "প্ল", "প্ত", "প্ন", "প্স", "প্প")),
        KeyModel("ড",  popupCandidates = listOf("৫", "ঢ", "ড়", "ঢ়", "ড্ড", "ড্র")),
        KeyModel("ক",  popupCandidates = listOf("৬", "খ", "ক্ষ", "ক্র", "ক্ল", "ক্ত", "ক্ক", "ক্ন", "ক্ব", "ক্স")),
        KeyModel("ত",  popupCandidates = listOf("৭", "থ", "ট", "ঠ", "ৎ", "ত্ত", "ত্র", "ত্ব", "ত্থ", "ত্ম")),
        KeyModel("চ",  popupCandidates = listOf("৮", "ছ", "চ্চ", "চ্ছ", "চ্ব")),
        KeyModel("ট",  popupCandidates = listOf("৯", "ঠ", "ট্ট", "ট্র", "ট্ব")),
        KeyModel("ড়", popupCandidates = listOf("০", "ঢ়", "ড়্ড়"))
    )

    val JatiyoRow2 = listOf(
        KeyModel("া",  popupCandidates = listOf("আ", "অ", "ো", "ৌ", "ৈ", "ৗ")),
        KeyModel("ি",  popupCandidates = listOf("ই", "ী", "ঈ")),
        KeyModel("ু",  popupCandidates = listOf("উ", "ূ", "ঊ", "ৃ", "ঋ")),
        KeyModel("ে",  popupCandidates = listOf("এ", "ো", "ৈ", "ৌ")),
        KeyModel("ো", popupCandidates = listOf("ৌ", "এ", "ো")),
        KeyModel("্",  popupCandidates = listOf("্ক", "্ত", "্ন", "্র", "্ব", "্ম", "্য", "্ল", "্প", "্ট", "্থ", "্স")),
        KeyModel("র",  popupCandidates = listOf("ড়", "ঢ়", "র্", "্র", "র্ক", "র্ত")),
        KeyModel("ন",  popupCandidates = listOf("ণ", "ন্ত", "ন্দ", "ন্ন", "ন্ম", "ন্ব", "ন্থ", "ন্ধ", "ন্ট")),
        KeyModel("ম",  popupCandidates = listOf("ম্প", "ম্ব", "ম্ভ", "ম্ম", "ম্ল", "ম্ন", "ম্ফ")),
        KeyModel("ল",  popupCandidates = listOf("ল্ল", "ল্ক", "ল্প", "ল্ট", "ল্ম"))
    )

    val JatiyoRow3 = listOf(
        KeyModel("ং",  popupCandidates = listOf("ঙ", "ঙ্ক", "ঙ্গ", "ঙ্ম")),
        KeyModel("ঃ",  popupCandidates = listOf("ঁ", ":")),
        KeyModel("ঁ",  popupCandidates = listOf("ঁ", "ং", "ঃ")),
        KeyModel("য",  popupCandidates = listOf("য়", "য্য", "্য", "য্ন")),
        KeyModel("জ",  popupCandidates = listOf("ঝ", "ঞ", "জ্জ", "জ্ঝ", "জ্ব", "জ্ঞ")),
        KeyModel("স",  popupCandidates = listOf("শ", "ষ", "স্ত", "স্থ", "স্ন", "স্ব", "স্ম", "স্ক", "স্প")),
        KeyModel("শ",  popupCandidates = listOf("ষ", "শ্চ", "শ্ন", "শ্ম", "শ্ল", "শ্র", "শ্ব")),
        KeyModel("দ",  popupCandidates = listOf("ধ", "দ্দ", "দ্ধ", "দ্ব", "দ্র", "দ্ম", "দ্ভ"))
    )

    // =========================================================================
    //  ★  জাতীয় শিফট মোড — মহাপ্রাণ + স্বরবর্ণ
    // =========================================================================

    val JatiyoShiftRow1 = listOf(
        KeyModel("ভ",  popupCandidates = listOf("১", "ব", "ভ্র", "ভ্ন", "ভ্ব")),
        KeyModel("ঋ",  popupCandidates = listOf("২", "ৃ", "ঋ্ণ")),
        KeyModel("ঘ",  popupCandidates = listOf("৩", "গ", "ঘ্ন", "ঘ্ব")),
        KeyModel("ফ",  popupCandidates = listOf("৪", "প", "ফ্র", "ফ্ল")),
        KeyModel("ঢ",  popupCandidates = listOf("৫", "ড", "ঢ্র", "ঢ্ব")),
        KeyModel("খ",  popupCandidates = listOf("৬", "ক", "খ্য", "খ্র")),
        KeyModel("থ",  popupCandidates = listOf("৭", "ত", "থ্য", "থ্র")),
        KeyModel("ছ",  popupCandidates = listOf("৮", "চ", "ছ্য")),
        KeyModel("ঠ",  popupCandidates = listOf("৯", "ট", "ঠ্য", "ঠ্ব")),
        KeyModel("ঢ়", popupCandidates = listOf("০", "ড়", "ড়্ড়"))
    )

    val JatiyoShiftRow2 = listOf(
        KeyModel("অ",  popupCandidates = listOf("আ", "া", "ো", "ৌ")),
        KeyModel("ঈ",  popupCandidates = listOf("ই", "ী", "ি")),
        KeyModel("ঊ",  popupCandidates = listOf("উ", "ূ", "ু", "ঋ")),
        KeyModel("ঐ",  popupCandidates = listOf("এ", "ৈ", "ে")),
        KeyModel("ঔ",  popupCandidates = listOf("ও", "ৌ", "ো")),
        KeyModel("্",  popupCandidates = listOf("্ক", "্ত", "্ন", "্র", "্ম", "্য", "্ল")),
        KeyModel("ৃ",  popupCandidates = listOf("ঋ", "্ৃ")),
        KeyModel("ণ",  popupCandidates = listOf("ন", "ণ্ণ", "ণ্ট", "ণ্ড")),
        KeyModel("ম",  popupCandidates = listOf("ম্ম", "ম্ন", "ম্ব")),
        KeyModel("ল",  popupCandidates = listOf("ল্ল", "ল্ক", "ল্ম"))
    )

    val JatiyoShiftRow3 = listOf(
        KeyModel("ঙ",  popupCandidates = listOf("ং", "ঙ্ক", "ঙ্গ")),
        KeyModel("ঞ",  popupCandidates = listOf("ঞ্চ", "ঞ্ছ", "ঞ্জ", "ঞ্ঝ")),
        KeyModel("ৎ",  popupCandidates = listOf("ত", "ৎক", "ৎপ")),
        KeyModel("য়", popupCandidates = listOf("য", "য্য")),
        KeyModel("ঝ",  popupCandidates = listOf("জ", "ঝ্ব")),
        KeyModel("ষ",  popupCandidates = listOf("ষ্প", "ষ্ট", "ষ্ণ", "ষ্ঠ", "ষ্ক")),
        KeyModel("শ",  popupCandidates = listOf("শ্চ", "শ্র", "শ্ব", "শ্ন")),
        KeyModel("ধ",  popupCandidates = listOf("দ", "ধ্ন", "ধ্ব", "ধ্য", "ধ্র"))
    )

    // =========================================================================
    //  ★  অভ্র মোড — QWERTY transliteration (Avro Phonetic standard)
    //      বাংলা numbers appear as corner hints on top row
    // =========================================================================

    val AvroRow1 = listOf(
        KeyModel("q", popupCandidates = listOf("১", "ক", "ক্ষ", "ক্ব")),
        KeyModel("w", popupCandidates = listOf("২", "ও", "ওয়", "ওই")),
        KeyModel("e", popupCandidates = listOf("৩", "এ", "ে", "ঈ", "ই")),
        KeyModel("r", popupCandidates = listOf("৪", "র", "ড়", "ঢ়", "ৃ", "ঋ")),
        KeyModel("t", popupCandidates = listOf("৫", "ত", "থ", "ট", "ঠ", "ৎ")),
        KeyModel("y", popupCandidates = listOf("৬", "য়", "য", "য্য")),
        KeyModel("u", popupCandidates = listOf("৭", "উ", "ু", "ঊ", "ূ")),
        KeyModel("i", popupCandidates = listOf("৮", "ই", "ি", "ঈ", "ী")),
        KeyModel("o", popupCandidates = listOf("৯", "ও", "ো", "ঔ", "ৌ")),
        KeyModel("p", popupCandidates = listOf("০", "প", "ফ", "প্র", "প্ল"))
    )

    val AvroRow2 = listOf(
        KeyModel("a", popupCandidates = listOf("অ", "আ", "া", "ঁ", "ৈ")),
        KeyModel("s", popupCandidates = listOf("স", "শ", "ষ", "শ্চ")),
        KeyModel("d", popupCandidates = listOf("দ", "ধ", "ড", "ঢ", "ড়")),
        KeyModel("f", popupCandidates = listOf("ফ", "ফ্র", "ফ্ল")),
        KeyModel("g", popupCandidates = listOf("গ", "ঘ", "ঙ", "গ্র")),
        KeyModel("h", popupCandidates = listOf("হ", "ঃ", "হ্ম", "হ্ন")),
        KeyModel("j", popupCandidates = listOf("জ", "ঝ", "ঞ", "জ্ঞ")),
        KeyModel("k", popupCandidates = listOf("ক", "খ", "ক্ষ", "ক্র")),
        KeyModel("l", popupCandidates = listOf("ল", "ল্ল", "ল্ক"))
    )

    val AvroRow3 = listOf(
        KeyModel("z", popupCandidates = listOf("য", "জ", "ঝ", "জ্ঞ")),
        KeyModel("x", popupCandidates = listOf("ক্স", "ক্ষ", "ক্ষ্ম")),
        KeyModel("c", popupCandidates = listOf("চ", "ছ", "ক", "চ্চ")),
        KeyModel("v", popupCandidates = listOf("ভ", "ব", "ভ্র")),
        KeyModel("b", popupCandidates = listOf("ব", "ভ", "ব্র", "ব্ল")),
        KeyModel("n", popupCandidates = listOf("ন", "ণ", "ং", "ন্ত", "ন্দ")),
        KeyModel("m", popupCandidates = listOf("ম", "ং", "ম্ব", "ম্প"))
    )

    // =========================================================================
    //  ★  ফোনেটিক মোড — বাংলা Phonetic QWERTY (Bangla Phonetic Engine)
    //      ইংরেজি টাইপ করলে বাংলায় রূপান্তর হয়
    // =========================================================================

    val PhoneticRow1 = listOf(
        KeyModel("q", popupCandidates = listOf("১", "ক", "খ", "ক্ষ", "ক্ব")),
        KeyModel("w", popupCandidates = listOf("২", "ও", "ওয়", "ওই")),
        KeyModel("e", popupCandidates = listOf("৩", "এ", "ে", "ঈ", "ই")),
        KeyModel("r", popupCandidates = listOf("৪", "র", "ড়", "ঢ়", "ৃ")),
        KeyModel("t", popupCandidates = listOf("৫", "ত", "থ", "ট", "ঠ", "ৎ")),
        KeyModel("y", popupCandidates = listOf("৬", "য়", "য", "য্য")),
        KeyModel("u", popupCandidates = listOf("৭", "উ", "ঊ", "ু", "ূ")),
        KeyModel("i", popupCandidates = listOf("৮", "ই", "ঈ", "ি", "ী")),
        KeyModel("o", popupCandidates = listOf("৯", "ও", "ো", "ঔ", "ৌ")),
        KeyModel("p", popupCandidates = listOf("০", "প", "ফ", "প্র"))
    )

    val PhoneticRow2 = listOf(
        KeyModel("a", popupCandidates = listOf("অ", "আ", "া", "ঁ")),
        KeyModel("s", popupCandidates = listOf("স", "শ", "ষ", "শ্চ")),
        KeyModel("d", popupCandidates = listOf("দ", "ধ", "ড", "ঢ")),
        KeyModel("f", popupCandidates = listOf("ফ", "ফ্র", "ফ্ল")),
        KeyModel("g", popupCandidates = listOf("গ", "ঘ", "ঙ", "গ্র")),
        KeyModel("h", popupCandidates = listOf("হ", "ঃ", "হ্ম")),
        KeyModel("j", popupCandidates = listOf("জ", "ঝ", "ঞ", "জ্ঞ")),
        KeyModel("k", popupCandidates = listOf("ক", "খ", "ক্ষ")),
        KeyModel("l", popupCandidates = listOf("ল", "ল্ল"))
    )

    val PhoneticRow3 = listOf(
        KeyModel("z", popupCandidates = listOf("য", "জ", "ঝ", "জ্ঞ")),
        KeyModel("x", popupCandidates = listOf("ক্স", "ক্ষ")),
        KeyModel("c", popupCandidates = listOf("চ", "ছ", "চ্চ")),
        KeyModel("v", popupCandidates = listOf("ভ", "ব", "ভ্র")),
        KeyModel("b", popupCandidates = listOf("ব", "ভ", "ব্র")),
        KeyModel("n", popupCandidates = listOf("ন", "ণ", "ং", "ন্ত")),
        KeyModel("m", popupCandidates = listOf("ম", "ম্ব", "ম্প"))
    )
}
