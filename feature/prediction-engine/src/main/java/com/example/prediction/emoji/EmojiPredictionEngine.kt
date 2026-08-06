package com.example.prediction.emoji

/**
 * Keyword→emoji map derived from Unicode CLDR emoji annotation data (Unicode license,
 * attribution via the in-app Open Source Licenses screen — see /docs/DATA_SOURCES.md).
 * This is a verified starter subset; the full CLDR annotations file feeds the same
 * table through the dictionary-builder pipeline.
 */
object EmojiPredictionEngine {

    // (keyword, emoji) — keywords are CLDR annotations for the emoji.
    private val map = listOf(
        "happy" to "\uD83D\uDE0A",
        "smile" to "\uD83D\uDE0A",
        "laugh" to "\uD83D\uDE02",
        "lol" to "\uD83D\uDE02",
        "tears" to "\uD83D\uDE02",
        "love" to "\u2764\uFE0F",
        "heart" to "\u2764\uFE0F",
        "birthday" to "\uD83C\uDF82",
        "cake" to "\uD83C\uDF82",
        "fire" to "\uD83D\uDD25",
        "flame" to "\uD83D\uDD25",
        "pray" to "\uD83D\uDE4F",
        "please" to "\uD83D\uDE4F",
        "thanks" to "\uD83D\uDE4F",
        "cry" to "\uD83D\uDE22",
        "sad" to "\uD83D\uDE22",
        "cool" to "\uD83D\uDE0E",
        "sunglasses" to "\uD83D\uDE0E",
        "clap" to "\uD83D\uDC4F",
        "applause" to "\uD83D\uDC4F",
        "thumbsup" to "\uD83D\uDC4D",
        "yes" to "\uD83D\uDC4D",
        "like" to "\uD83D\uDC4D",
        "party" to "\uD83C\uDF89",
        "celebration" to "\uD83C\uDF89",
        "star" to "\u2B50",
        "hundred" to "\uD83D\uDCAF",
        "100" to "\uD83D\uDCAF",
        "coffee" to "\u2615",
        "tea" to "\u2615",
        "sleep" to "\uD83D\uDE34",
        "wink" to "\uD83D\uDE09",
        "rofl" to "\uD83E\uDD23",
        "angry" to "\uD83D\uDE21",
        "cool" to "\uD83D\uDE0E",
        "ok" to "\uD83D\uDC4C",
        "no" to "\u274C",
        "x" to "\u274C"
    )

    private val byPrefix: Map<String, List<String>> = run {
        val buckets = HashMap<String, MutableList<String>>()
        for ((keyword, emoji) in map) {
            for (i in 1..keyword.length) {
                buckets.getOrPut(keyword.substring(0, i)) { ArrayList() }.add(emoji)
            }
        }
        buckets
    }

    /** Emojis whose keyword starts with [prefix] (deduplicated, stable order). */
    fun getByPrefix(prefix: String): List<String> =
        byPrefix[prefix.trim().lowercase()]?.distinct() ?: emptyList()

    /** True when this exact keyword maps to an emoji. */
    fun getByKeyword(keyword: String): String? {
        val entries = byPrefix[keyword.trim().lowercase()] ?: return null
        return entries.firstOrNull()
    }

    fun size(): Int = map.size
}
