package com.example.engine

import java.util.Locale

/**
 * High-performance Dictionary Trie and Prediction Engine.
 * Supports prefix search, frequency ranking, personal learned words,
 * and autocorrect suggestions for both English and Bangla.
 */
class PredictionEngine {

    private class TrieNode {
        val children = HashMap<Char, TrieNode>()
        var isWord = false
        var frequency = 0
    }

    private val englishRoot = TrieNode()
    private val banglaRoot = TrieNode()
    private val personalWords = HashSet<String>()

    init {
        // Seed core English vocabulary
        val englishSeed = listOf(
            "the" to 100, "be" to 95, "to" to 90, "of" to 85, "and" to 80,
            "a" to 78, "in" to 75, "that" to 70, "have" to 68, "I" to 65,
            "it" to 62, "for" to 60, "not" to 58, "on" to 55, "with" to 52,
            "he" to 50, "as" to 48, "you" to 45, "do" to 42, "at" to 40,
            "this" to 38, "but" to 35, "his" to 32, "by" to 30, "from" to 28, "they" to 25,
            "we" to 22, "say" to 20, "her" to 18, "she" to 15, "or" to 14,
            "an" to 12, "will" to 10, "my" to 8, "one" to 7, "all" to 6,
            "would" to 5, "there" to 4, "their" to 3, "what" to 2, "keyboard" to 99,
            "application" to 95, "android" to 92, "language" to 88, "phonetic" to 85,
            "message" to 80, "welcome" to 75, "project" to 70, "setting" to 65
        )
        englishSeed.forEach { (word, freq) -> insertWord(englishRoot, word, freq) }

        // Seed core Bangla vocabulary
        val banglaSeed = listOf(
            "আমি" to 100, "বাংলা" to 98, "তুমি" to 95, "ধন্যবাদ" to 90, "কেমন" to 88,
            "আছো" to 85, "ভালো" to 82, "আমাদের" to 80, "দেশ" to 78, "সোনার" to 75,
            "বাংলাদেশ" to 72, "কীবোর্ড" to 70, "সুন্দর" to 68, "আমার" to 65, "কথা" to 60,
            "কাজ" to 58, "সময়" to 55, "লাইফ" to 50, "ফোন" to 48, "অ্যাপ" to 45
        )
        banglaSeed.forEach { (word, freq) -> insertWord(banglaRoot, word, freq) }
    }

    private fun insertWord(root: TrieNode, word: String, frequency: Int) {
        var current = root
        for (ch in word) {
            current = current.children.getOrPut(ch) { TrieNode() }
        }
        current.isWord = true
        current.frequency = maxOf(current.frequency, frequency)
    }

    /**
     * Learn a user-typed word into the personal dictionary.
     */
    fun learnWord(word: String, isBangla: Boolean = false) {
        val trimmed = word.trim()
        if (trimmed.length < 2) return
        personalWords.add(trimmed)
        val root = if (isBangla) banglaRoot else englishRoot
        insertWord(root, trimmed, 150) // High frequency for user-learned words
    }

    /**
     * Get candidate predictions for a given prefix.
     */
    fun getPredictions(prefix: String, isBangla: Boolean = false, limit: Int = 4): List<String> {
        val query = prefix.trim()
        if (query.isEmpty()) return emptyList()

        val root = if (isBangla) banglaRoot else englishRoot
        val results = mutableListOf<Pair<String, Int>>()

        // Find prefix node
        var current: TrieNode? = root
        for (ch in query) {
            current = current?.children?.get(ch)
            if (current == null) break
        }

        if (current != null) {
            collectWords(current, StringBuilder(query), results)
        }

        // Also check Bangla Phonetic candidate if input is Latin and Bangla mode is on
        val list = results.sortedByDescending { it.second }
            .map { it.first }
            .distinct()
            .take(limit)
            .toMutableList()

        if (!isBangla && query.all { it in 'a'..'z' || it in 'A'..'Z' }) {
            val banglaTransliterated = BanglaPhoneticEngine.parse(query)
            if (banglaTransliterated.isNotEmpty() && banglaTransliterated != query) {
                if (!list.contains(banglaTransliterated)) {
                    list.add(0, banglaTransliterated)
                }
            }
        }

        return list.take(limit)
    }

    private fun collectWords(node: TrieNode, prefix: StringBuilder, results: MutableList<Pair<String, Int>>) {
        if (node.isWord) {
            results.add(prefix.toString() to node.frequency)
        }
        if (results.size > 50) return // Safety bound
        for ((ch, childNode) in node.children) {
            prefix.append(ch)
            collectWords(childNode, prefix, results)
            prefix.deleteCharAt(prefix.length - 1)
        }
    }
}
