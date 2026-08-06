package com.example.prediction.banglish

/**
 * Lightweight per-token script/intent classifier (character-set heuristic + known-token
 * lookup, not an ML model). Classifies each committed token so it routes to the correct
 * personal sub-dictionary partition.
 */
class ScriptIntentDetector {

    enum class Intent(val tag: String) { ENGLISH("en"), BANGLA("bn"), BANGLISH("bn-Latn"), OTHER("xx") }

    private val banglishTokens = setOf(
        "vhalo", "vhal", "kemon", "ase", "asche", "korbo", "korteci", "korsi", "korte",
        "jabo", "jai", "jaite", "ami", "tumi", "apni", "nah", "lagtase", "lagbe", "ache",
        "chai", "chay", "ki", "na", "tai", "oi", "ei", "thik", "parbo", "hobe", "hoy",
        "mane", "kore", "koro", "hoise", "naki", "ektu", "kothay", "kobe", "koto",
        "jonno", "jodi", "tobe", "amader", "tomar", "apnar", "mone", "bhalo", "bole",
        "bolar", "dekhbo", "dekhte", "sunte", "bolbo", "bolchi", "jete", "aite", "khai",
        "khaw", "dite", "dibo", "niye", "diye", "theke", "sathe", "pore", "porjonto"
    )

    fun classify(token: String): Intent {
        if (token.isEmpty()) return Intent.OTHER
        var banglaChars = 0
        var latinChars = 0
        for (ch in token) {
            when {
                ch.code in 0x0980..0x09FF -> banglaChars++
                ch in 'a'..'z' || ch in 'A'..'Z' -> latinChars++
            }
        }
        return when {
            banglaChars > 0 && banglaChars >= token.length / 2 -> Intent.BANGLA
            latinChars == token.length -> {
                if (token.lowercase() in banglishTokens) Intent.BANGLISH else Intent.ENGLISH
            }
            else -> Intent.OTHER
        }
    }
}
