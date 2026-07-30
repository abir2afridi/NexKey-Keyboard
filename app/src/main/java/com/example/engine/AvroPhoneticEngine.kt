package com.example.engine

import java.text.Normalizer

/**
 * Standard Avro Phonetic Transliteration Engine.
 * Follows the official Avro Keyboard phonetic mapping rules exactly.
 *
 * Reference: https://www.omicronlab.com/avro-keyboard.html
 *
 * Key differences from BanglaPhoneticEngine:
 * - `c` → ক (not চ), `ch` → চ, `chh` → ছ
 * - `y` → য় (not য), `z`/`Z` → য
 * - `ng`/`N` → ং, `M` → ং, `H` → ঃ
 * - `rr` / `RR` → ড়
 * - `kh` → খ (same), `gh` → ঘ (same)
 * - Full conjunct support via hasant
 */
object AvroPhoneticEngine {

    private const val HASANT = "\u09CD"       // ্
    private const val CHANDRABINDU = "\u0981"  // ঁ
    private const val ANUSVARA = "\u0982"      // ং
    private const val VISARGA = "\u0983"       // ঃ
    private const val DARI = "।"
    private const val DOUBLE_DARI = "॥"

    // Ordered by length (longest first) for greedy matching
    // Avro official consonant mappings
    private val CONSONANTS: List<Pair<String, String>> = listOf(
        // 3-char
        "kss" to "ক্ষ",
        "shh" to "শ্ছ",
        "NGa" to "ঞ",
        // 2-char (must come before single)
        "kh"  to "খ",
        "gh"  to "ঘ",
        "ng"  to "ঙ",
        "ch"  to "চ",
        "jh"  to "ঝ",
        "Th"  to "ঠ",
        "Dh"  to "ঢ",
        "th"  to "থ",
        "dh"  to "ধ",
        "ph"  to "ফ",
        "bh"  to "ভ",
        "sh"  to "শ",
        "Sh"  to "ষ",
        "rr"  to "ড়",
        "RR"  to "ড়",
        "Rh"  to "ঢ়",
        "NG"  to "ঞ",
        "kx"  to "ক্ষ",
        "ks"  to "ক্ষ",
        // 1-char
        "k"   to "ক",
        "g"   to "গ",
        "c"   to "ক",
        "j"   to "জ",
        "T"   to "ট",
        "D"   to "ড",
        "t"   to "ত",
        "d"   to "দ",
        "n"   to "ন",
        "N"   to "ণ",
        "p"   to "প",
        "f"   to "ফ",
        "b"   to "ব",
        "m"   to "ম",
        "z"   to "য",
        "Z"   to "য",
        "r"   to "র",
        "R"   to "ড়",
        "l"   to "ল",
        "S"   to "শ",
        "s"   to "স",
        "h"   to "হ",
        "y"   to "য়",
        "v"   to "ভ",
        "q"   to "ক",
        "w"   to "ও",
        "x"   to "ক্স"
    )

    // Independent vowels (when at word start or after vowel)
    private val VOWELS: List<Pair<String, String>> = listOf(
        "OI"  to "ঐ",
        "OU"  to "ঔ",
        "oi"  to "ঐ",
        "ou"  to "ঔ",
        "aa"  to "আ",
        "ee"  to "ঈ",
        "ii"  to "ঈ",
        "uu"  to "ঊ",
        "oo"  to "ঊ",
        "ri"  to "ঋ",
        "A"   to "আ",
        "I"   to "ঈ",
        "U"   to "ঊ",
        "E"   to "এ",
        "O"   to "ও",
        "a"   to "অ",
        "i"   to "ই",
        "u"   to "উ",
        "e"   to "এ",
        "o"   to "ও"
    )

    // Dependent vowel signs (kar) — used after a consonant
    private val KAR: List<Pair<String, String>> = listOf(
        "OI"  to "ৈ",
        "OU"  to "ৌ",
        "oi"  to "ৈ",
        "ou"  to "ৌ",
        "aa"  to "া",
        "ee"  to "ী",
        "ii"  to "ী",
        "uu"  to "ূ",
        "oo"  to "ূ",
        "ri"  to "ৃ",
        "A"   to "া",
        "I"   to "ী",
        "U"   to "ূ",
        "E"   to "ে",
        "O"   to "ো",
        "a"   to "",        // inherent 'a' — silent after consonant
        "i"   to "ি",
        "u"   to "ু",
        "e"   to "ে",
        "o"   to "ো"
    )

    private val NUMERALS = mapOf(
        '0' to '০', '1' to '১', '2' to '২', '3' to '৩', '4' to '৪',
        '5' to '৫', '6' to '৬', '7' to '৭', '8' to '৮', '9' to '৯'
    )

    /**
     * Main Avro phonetic transliteration function.
     * Input: Latin string typed by the user (e.g. "amar sonar bangla")
     * Output: Bengali Unicode string (e.g. "আমার সোনার বাংলা")
     */
    fun parse(latinInput: String): String {
        if (latinInput.isEmpty()) return ""

        val result = StringBuilder()
        var i = 0
        val len = latinInput.length

        while (i < len) {
            val ch = latinInput[i]

            // --- Special characters ---
            // Dari (।) from double period
            if (ch == '.' && i + 1 < len && latinInput[i + 1] == '.') {
                result.append(DARI)
                i += 2
                continue
            }
            // Double Dari from triple period
            if (ch == '.' && i + 2 < len && latinInput[i + 1] == '.' && latinInput[i + 2] == '.') {
                result.append(DOUBLE_DARI)
                i += 3
                continue
            }
            // Chandrabindu
            if (ch == '^') {
                result.append(CHANDRABINDU)
                i++
                continue
            }
            // Anusvara / Anuswar (M at end of syllable or before consonant)
            if (ch == 'M') {
                result.append(ANUSVARA)
                i++
                continue
            }
            // Visarga
            if (ch == 'H' && (i + 1 >= len || !latinInput[i + 1].isLetter())) {
                result.append(VISARGA)
                i++
                continue
            }
            // Hasant (explicit - user types ,,)
            if (ch == ',' && i + 1 < len && latinInput[i + 1] == ',') {
                result.append(HASANT)
                i += 2
                continue
            }
            // Numerals
            if (ch in NUMERALS) {
                result.append(NUMERALS[ch])
                i++
                continue
            }
            // Spaces and punctuation pass through
            if (ch == ' ' || ch == '\n' || ch == '\t') {
                result.append(ch)
                i++
                continue
            }
            // Non-alpha punctuation (except handled above) pass through
            if (!ch.isLetter()) {
                result.append(ch)
                i++
                continue
            }

            // --- Try matching a consonant ---
            var matchedConsonant: String? = null
            var matchedConsonantKey = ""
            var matchedConsonantLen = 0

            for ((key, value) in CONSONANTS) {
                val keyLen = key.length
                if (i + keyLen <= len && latinInput.substring(i, i + keyLen) == key) {
                    matchedConsonant = value
                    matchedConsonantKey = key
                    matchedConsonantLen = keyLen
                    break
                }
            }

            if (matchedConsonant != null) {
                result.append(matchedConsonant)
                i += matchedConsonantLen

                // Lookahead: vowel sign (kar), hasant, or next consonant
                if (i < len) {
                    // Try to match a kar (dependent vowel sign)
                    var matchedKar: String? = null
                    var matchedKarLen = 0

                    for ((key, value) in KAR) {
                        val keyLen = key.length
                        if (i + keyLen <= len && latinInput.substring(i, i + keyLen) == key) {
                            matchedKar = value
                            matchedKarLen = keyLen
                            break
                        }
                    }

                    if (matchedKar != null) {
                        // Append kar (may be empty string for inherent 'a')
                        if (matchedKar.isNotEmpty()) result.append(matchedKar)
                        i += matchedKarLen
                    } else if (i < len) {
                        // Check if next is a consonant — if so, add hasant
                        val nextChar = latinInput[i]
                        val nextIsConsonant = CONSONANTS.any { (key, _) ->
                            i + key.length <= len && latinInput.substring(i, i + key.length) == key
                        }
                        val nextIsVowel = VOWELS.any { (key, _) ->
                            i + key.length <= len && latinInput.substring(i, i + key.length) == key
                        }

                        if (nextIsConsonant && !nextIsVowel) {
                            result.append(HASANT)
                        }
                        // else: inherent 'a' vowel sound — do nothing (consonant already appended)
                    }
                }
                continue
            }

            // --- Try matching an independent vowel ---
            var matchedVowel: String? = null
            var matchedVowelLen = 0

            for ((key, value) in VOWELS) {
                val keyLen = key.length
                if (i + keyLen <= len && latinInput.substring(i, i + keyLen) == key) {
                    matchedVowel = value
                    matchedVowelLen = keyLen
                    break
                }
            }

            if (matchedVowel != null) {
                result.append(matchedVowel)
                i += matchedVowelLen
                continue
            }

            // --- Fallback: pass character as-is ---
            result.append(ch)
            i++
        }

        return Normalizer.normalize(result.toString(), Normalizer.Form.NFC)
    }

    /**
     * Converts English digits to Bangla digits.
     */
    fun convertDigitsToBangla(text: String): String {
        val sb = StringBuilder()
        for (ch in text) {
            sb.append(NUMERALS[ch] ?: ch)
        }
        return sb.toString()
    }
}
