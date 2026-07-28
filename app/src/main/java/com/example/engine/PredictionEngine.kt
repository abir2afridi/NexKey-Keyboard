package com.example.engine

import android.content.Context
import com.example.data.AppDatabase
import com.example.data.LearnedWordEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PredictionEngine {

    private class TrieNode {
        val children = HashMap<Char, TrieNode>()
        var isWord = false
        var frequency = 0
    }

    private val englishRoot = TrieNode()
    private val banglaRoot = TrieNode()
    private val personalWords = HashSet<String>()
    private var lastTypedWord: String = ""

    private var database: AppDatabase? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var incognito: Boolean = false

    fun init(context: Context) {
        database = AppDatabase.getInstance(context)
        scope.launch {
            val savedWords = database?.learnedWordDao()?.getPredictions("", isBangla = false, limit = 200) ?: emptyList()
            savedWords.forEach { insertWord(englishRoot, it.word, it.frequency) }
            val savedBanglaWords = database?.learnedWordDao()?.getPredictions("", isBangla = true, limit = 200) ?: emptyList()
            savedBanglaWords.forEach { insertWord(banglaRoot, it.word, it.frequency) }
        }
    }

    fun setIncognito(enabled: Boolean) {
        incognito = enabled
    }

    fun isIncognito(): Boolean = incognito

    init {
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

    fun learnWord(word: String, isBangla: Boolean = false) {
        if (incognito) return
        val trimmed = word.trim()
        if (trimmed.length < 2) return
        personalWords.add(trimmed)
        val root = if (isBangla) banglaRoot else englishRoot
        insertWord(root, trimmed, 150)

        scope.launch {
            val existing = database?.learnedWordDao()?.findWord(trimmed)
            if (existing != null) {
                database?.learnedWordDao()?.insertOrUpdate(
                    existing.copy(frequency = existing.frequency + 1, lastUsedAt = System.currentTimeMillis())
                )
            } else {
                database?.learnedWordDao()?.insertOrUpdate(
                    LearnedWordEntity(word = trimmed, isBangla = isBangla, frequency = 150)
                )
            }
        }
    }

    fun clearAllLearned() {
        personalWords.clear()
        scope.launch {
            database?.learnedWordDao()?.clearAll()
        }
    }

    fun setLastTypedWord(word: String) {
        lastTypedWord = word
    }

    fun getNextWordPredictions(isBangla: Boolean = false): List<String> {
        if (lastTypedWord.isBlank()) return emptyList()
        return getPredictions("", isBangla, limit = 3, showTypedWordFirst = false)
    }

    fun getCorrection(word: String, isBangla: Boolean = false): String? {
        val root = if (isBangla) banglaRoot else englishRoot
        var current: TrieNode? = root
        for (ch in word) {
            current = current?.children?.get(ch)
            if (current == null) return null
        }
        if (current?.isWord == true && current.frequency > 0) return null
        val predictions = getPredictions(word, isBangla, limit = 1, showTypedWordFirst = false)
        return if (predictions.isNotEmpty() && predictions[0] != word) predictions[0] else null
    }

    fun getPredictions(prefix: String, isBangla: Boolean = false, limit: Int = 4, showTypedWordFirst: Boolean = false): List<String> {
        val query = prefix.trim()
        if (query.isEmpty()) return emptyList()

        val root = if (isBangla) banglaRoot else englishRoot
        val results = mutableListOf<Pair<String, Int>>()

        var current: TrieNode? = root
        for (ch in query) {
            current = current?.children?.get(ch)
            if (current == null) break
        }

        if (current != null) {
            collectWords(current, StringBuilder(query), results)
        }

        val list = results.sortedByDescending { it.second }
            .map { it.first }
            .distinct()
            .take(limit)
            .toMutableList()

        if (showTypedWordFirst && !list.contains(query)) {
            list.add(0, query)
        }

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
        if (results.size > 50) return
        for ((ch, childNode) in node.children) {
            prefix.append(ch)
            collectWords(childNode, prefix, results)
            prefix.deleteCharAt(prefix.length - 1)
        }
    }
}
