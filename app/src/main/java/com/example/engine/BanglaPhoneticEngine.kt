package com.example.engine

import java.text.Normalizer

/**
 * Ridmik-class Bangla Phonetic Transliteration Engine.
 * Converts Latin phoneme keystrokes (e.g., "ami", "bangla", "amar", "shonai", "kormo")
 * into byte-level correct Bengali Unicode (NFC normalized).
 *
 * Supports:
 * - Independent Vowels & Dependent Vowel Signs (Kar)
 * - Inherent Vowel (ô) Suppression
 * - Consonant Clusters & Conjuncts (Juktakkhor) via Hasant (্ U+09CD)
 * - Ref (র্), Ya-phola (্য), Ra-phola (্ৰ), Hasant, Chandrabindu (^), Anusvara (ং), Visarga (ঃ), Dari (।)
 * - Bangla Numerals (০-৯)
 */
object BanglaPhoneticEngine {

    private const val HASANT = "\u09CD" // ্
    private const val CHANDRABINDU = "\u0981" // ঁ
    private const val ANUSVARA = "\u0982" // ং
    private const val VISARGA = "\u0983" // ঃ
    private const val DARI = "।"

    // Mapping for consonants to Bengali letters
    private val CONSONANTS = mapOf(
        "k" to "ক", "kh" to "খ", "g" to "গ", "gh" to "ঘ", "ng" to "ঙ", "Ng" to "ঙ",
        "ch" to "ছ", "c" to "চ", "j" to "জ", "jh" to "ঝ", "nj" to "ঞ", "NG" to "ঞ",
        "Th" to "ঠ", "T" to "ট", "Dh" to "ঢ", "D" to "ড", "N" to "ণ",
        "th" to "থ", "t" to "ত", "dh" to "ধ", "d" to "দ", "n" to "ন",
        "ph" to "ফ", "f" to "ফ", "p" to "প", "bh" to "ভ", "v" to "ভ", "b" to "ব", "m" to "ম",
        "z" to "য", "r" to "র", "l" to "ল", "Sh" to "ষ", "S" to "শ", "sh" to "শ", "s" to "স", "h" to "হ",
        "Rh" to "ঢ়", "R" to "ড়", "y" to "য়", "t`" to "ৎ", "t_" to "ৎ"
    )

    // Mapping for independent vowels
    private val VOWELS = mapOf(
        "a" to "অ", "A" to "আ", "aa" to "আ", "i" to "ই", "I" to "ঈ", "ee" to "ঈ",
        "u" to "উ", "U" to "ঊ", "oo" to "ঊ", "r`" to "ঋ", "e" to "এ",
        "OI" to "ঐ", "oi" to "ঐ", "o" to "ও", "OU" to "ঔ", "ou" to "ঔ"
    )

    // Mapping for dependent vowel signs (Kar)
    private val KAR_MAP = mapOf(
        "a" to "", // inherent or explicit a
        "A" to "া", "aa" to "া", "i" to "ি", "I" to "ী", "ee" to "ী",
        "u" to "ু", "U" to "ূ", "oo" to "ূ", "r`" to "ৃ", "e" to "ে",
        "OI" to "ৈ", "oi" to "ৈ", "o" to "ো", "OU" to "ৌ", "ou" to "ৌ"
    )

    // Common Bangla conjunct rules (Juktakkhor)
    private val JUKTAKKHOR_MAP = mapOf(
        "kk" to "ক্ক", "kS" to "ক্ষ", "ksh" to "ক্ষ", "kt" to "ক্ত", "kl" to "ক্ল", "kw" to "ক্ব", "ky" to "ক্য", "kr" to "ক্র",
        "kkh" to "ক্খ",
        "gdh" to "গ্ধ", "gn" to "গ্ন", "gm" to "গ্ম", "gly" to "গ্ল", "gr" to "গ্র", "gy" to "গ্য", "ggh" to "গ্ঘ",
        "ngk" to "ঙ্ক", "ngkh" to "ঙ্খ", "ngg" to "ঙ্গ", "nggh" to "ঙ্ঘ", "ngy" to "ঙ্য",
        "cc" to "চ্চ", "cch" to "চ্ছ", "cw" to "চ্ব", "cy" to "চ্য",
        "jj" to "জ্জ", "jjh" to "জ্ঝ", "jn" to "জ্ঞ", "jw" to "জ্ব", "jy" to "জ্য", "jjw" to "জ্জ্ব",
        "njc" to "ঞ্চ", "njch" to "ঞ্ছ", "njj" to "ঞ্জ", "njjh" to "ঞ্ঝ", "nj" to "ঞ",
        "TT" to "ট্ট", "Ttw" to "ট্ব", "Ty" to "ট্য", "Tr" to "ট্র",
        "DD" to "ড্ড", "Dy" to "ড্য", "Dr" to "ড্র",
        "NT" to "ণ্ট", "NTh" to "ণ্ঠ", "ND" to "ণ্ড", "NDh" to "ণ্ঢ", "NN" to "ণ্ণ", "Nm" to "ণ্ম", "Ny" to "ণ্য",
        "tt" to "ত্ত", "tth" to "ত্থ", "tn" to "ত্ন", "tm" to "ত্ম", "tw" to "ত্ব", "ty" to "ত্য", "tr" to "ত্র",
        "dd" to "দ্দ", "ddh" to "দ্ধ", "dw" to "দ্ব", "dy" to "দ্য", "dr" to "দ্র", "dm" to "দ্ম", "ddr" to "দ্দ্র",
        "dhn" to "ধ্ন", "dhm" to "ধ্ম", "dhw" to "ধ্ব", "dhy" to "ধ্য", "dhr" to "ধ্র",
        "nt" to "ন্ত", "nth" to "ন্থ", "nd" to "ন্দ", "ndh" to "ন্ধ", "nn" to "ন্ন", "nm" to "ন্ম", "ny" to "ন্য", "nw" to "ন্ব", "ntr" to "ন্ত্র", "ndr" to "ন্দ্র",
        "pt" to "প্ত", "pn" to "প্ন", "pp" to "প্প", "pl" to "প্ল", "ps" to "প্স", "py" to "প্য", "pr" to "প্র", "pph" to "ফ্ফ",
        "bd" to "ব্দ", "bdh" to "ব্ধ", "bb" to "ব্ব", "bl" to "ব্ল", "by" to "ব্য", "br" to "ব্র", "bbr" to "ব্ব্র",
        "bhn" to "ভ্ন", "bhy" to "ভ্য", "bhr" to "ভ্র",
        "mn" to "ম্ন", "mp" to "ম্প", "mph" to "ম্ফ", "mb" to "ম্ব", "mbh" to "ম্ভ", "mm" to "ম্ম", "ml" to "ম্ল", "my" to "ম্য", "mr" to "ম্র",
        "st" to "স্ত", "sth" to "স্থ", "sn" to "স্ন", "sp" to "স্প", "sph" to "স্ফ", "sb" to "স্ব", "sm" to "স্ম", "sl" to "স্ল", "sy" to "স্য", "sr" to "স্র",
        "ShT" to "ষ্ট", "ShTh" to "ষ্ঠ", "ShN" to "ষ্ণ", "Shp" to "ষ্প", "Shph" to "স্ফ", "Shm" to "ষ্ম", "Shy" to "ষ্য",
        "shc" to "শ্চ", "shch" to "শ্ছ", "shn" to "শ্ন", "shm" to "শ্ম", "shl" to "শ্ল", "shy" to "শ্য", "shr" to "শ্র",
        "hn" to "হ্ন", "hm" to "হ্ম", "hl" to "হ্ল", "hy" to "হ্য", "hr" to "হৃ", "hN" to "হ্ণ", "hny" to "হ্ন্য"
    )

    private val NUMERALS = mapOf(
        '0' to '০', '1' to '১', '2' to '২', '3' to '৩', '4' to '৪',
        '5' to '৫', '6' to '৬', '7' to '৭', '8' to '৮', '9' to '৯'
    )

    /**
     * Main phonetic transliteration function.
     * Takes Latin input string (e.g. "bangla", "kormo", "amader") and returns Bengali string.
     */
    fun parse(latinInput: String): String {
        if (latinInput.isEmpty()) return ""

        val result = StringBuilder()
        var i = 0
        val len = latinInput.length

        while (i < len) {
            val ch = latinInput[i]

            // Handle punctuation / special cases
            if (ch == '.' && i + 1 < len && latinInput[i + 1] == '.') {
                result.append(DARI)
                i += 2
                continue
            }
            if (ch == '^') {
                result.append(CHANDRABINDU)
                i++
                continue
            }
            if (ch == ':' && i + 1 < len && latinInput[i + 1] == ':') {
                result.append(VISARGA)
                i += 2
                continue
            }
            if (ch in NUMERALS) {
                result.append(NUMERALS[ch])
                i++
                continue
            }

            // Check for Juktakkhor / multi-character consonant cluster matched from current position
            var matched = false
            for (matchLen in 4 downTo 2) {
                if (i + matchLen <= len) {
                    val sub = latinInput.substring(i, i + matchLen)
                    if (JUKTAKKHOR_MAP.containsKey(sub)) {
                        result.append(JUKTAKKHOR_MAP[sub])
                        i += matchLen
                        matched = true
                        break
                    }
                }
            }
            if (matched) continue

            // Check for longest consonant token
            var matchedConsonant: String? = null
            var matchedConsonantLen = 0
            for (matchLen in 3 downTo 1) {
                if (i + matchLen <= len) {
                    val sub = latinInput.substring(i, i + matchLen)
                    if (CONSONANTS.containsKey(sub)) {
                        matchedConsonant = CONSONANTS[sub]
                        matchedConsonantLen = matchLen
                        break
                    }
                }
            }

            if (matchedConsonant != null) {
                result.append(matchedConsonant)
                i += matchedConsonantLen

                // Lookahead to see if followed by vowel or kar or hasant
                if (i < len) {
                    var matchedKar: String? = null
                    var matchedKarLen = 0
                    for (vLen in 2 downTo 1) {
                        if (i + vLen <= len) {
                            val vSub = latinInput.substring(i, i + vLen)
                            if (KAR_MAP.containsKey(vSub)) {
                                matchedKar = KAR_MAP[vSub]
                                matchedKarLen = vLen
                                break
                            }
                        }
                    }

                    if (matchedKar != null) {
                        if (matchedKar.isNotEmpty()) {
                            result.append(matchedKar)
                        }
                        i += matchedKarLen
                    } else if (i < len && (latinInput[i] == 'w' || latinInput[i] == 'y')) {
                        // Special handling for ya-phola or ba-phola
                        if (latinInput[i] == 'y') {
                            result.append(HASANT).append("য")
                            i++
                        } else if (latinInput[i] == 'w') {
                            result.append(HASANT).append("ব")
                            i++
                        }
                    } else if (i < len && CONSONANTS.keys.any { latinInput.substring(i).startsWith(it) }) {
                        // Consonant directly followed by another consonant without a vowel -> insert hasant
                        // Exception: if the next character is space or non-alpha, don't insert hasant
                        result.append(HASANT)
                    }
                }
                continue
            }

            // Check for independent vowels
            var matchedVowel: String? = null
            var matchedVowelLen = 0
            for (matchLen in 2 downTo 1) {
                if (i + matchLen <= len) {
                    val sub = latinInput.substring(i, i + matchLen)
                    if (VOWELS.containsKey(sub)) {
                        matchedVowel = VOWELS[sub]
                        matchedVowelLen = matchLen
                        break
                    }
                }
            }

            if (matchedVowel != null) {
                result.append(matchedVowel)
                i += matchedVowelLen
                continue
            }

            // Fallback: pass character as-is
            result.append(ch)
            i++
        }

        // Apply Unicode NFC normalization to ensure standard representation of conjuncts and kars
        return Normalizer.normalize(result.toString(), Normalizer.Form.NFC)
    }

    /**
     * Converts English numbers in text to Bangla numbers.
     */
    fun convertDigitsToBangla(text: String): String {
        val sb = StringBuilder()
        for (ch in text) {
            if (ch in NUMERALS) {
                sb.append(NUMERALS[ch])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }
}
