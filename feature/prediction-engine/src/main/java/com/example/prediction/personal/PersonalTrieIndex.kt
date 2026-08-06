package com.example.prediction.personal

/**
 * In-memory trie over personal/learned words, backed by Room for persistence.
 * Built on IME startup from the Room tables and updated immediately on every commit
 * so the very next keystroke sees a just-learned word. Small (thousands of entries),
 * so a plain hash-map trie is the right structure — far cheaper than a DAWG here.
 */
class PersonalTrieIndex {

    data class Entry(val frequency: Int, val lastUsedAt: Long)

    private class Node {
        val children = HashMap<Char, Node>()
        var entry: Entry? = null
    }

    private val root = Node()

    @Synchronized
    fun addWord(word: String, frequency: Int, lastUsedAt: Long) {
        var current = root
        for (ch in word) current = current.children.getOrPut(ch) { Node() }
        val existing = current.entry
        current.entry = if (existing == null) {
            Entry(frequency, lastUsedAt)
        } else {
            Entry(maxOf(existing.frequency, frequency), maxOf(existing.lastUsedAt, lastUsedAt))
        }
    }

    @Synchronized
    fun increment(word: String, now: Long): Entry? {
        var current = root
        for (ch in word) current = current.children.getOrPut(ch) { Node() }
        val existing = current.entry
        val entry = Entry((existing?.frequency ?: 0) + 1, now)
        current.entry = entry
        return existing
    }

    @Synchronized
    fun get(word: String): Entry? {
        var current = root
        for (ch in word) {
            current = current.children[ch] ?: return null
        }
        return current.entry
    }

    @Synchronized
    fun removeWord(word: String) {
        var current = root
        for (ch in word) {
            current = current.children[ch] ?: return
        }
        current.entry = null
    }

    @Synchronized
    fun clear() {
        root.children.clear()
    }

    @Synchronized
    fun size(): Int {
        var count = 0
        fun walk(node: Node) {
            if (node.entry != null) count++
            for (child in node.children.values) walk(child)
        }
        walk(root)
        return count
    }

    /** All word entries, e.g. for fuzzy-correction search over the small personal set. */
    @Synchronized
    fun allWords(): List<Triple<String, Int, Long>> {
        val out = ArrayList<Triple<String, Int, Long>>()
        val sb = StringBuilder()
        fun walk(node: Node) {
            node.entry?.let { out.add(Triple(sb.toString(), it.frequency, it.lastUsedAt)) }
            for ((ch, child) in node.children) {
                sb.append(ch)
                walk(child)
                sb.deleteCharAt(sb.length - 1)
            }
        }
        walk(root)
        return out
    }

    /** Words matching [prefix], bounded by [budget] nodes. */
    @Synchronized
    fun collectPrefix(prefix: String, budget: Int = 500): List<Pair<String, Entry>> {
        var current = root
        for (ch in prefix) {
            current = current.children[ch] ?: return emptyList()
        }
        val out = ArrayList<Pair<String, Entry>>()
        val sb = StringBuilder(prefix)
        var visited = 0
        fun walk(node: Node) {
            if (visited++ >= budget) return
            node.entry?.let { out.add(sb.toString() to it) }
            for ((ch, child) in node.children) {
                if (visited >= budget) break
                sb.append(ch)
                walk(child)
                sb.deleteCharAt(sb.length - 1)
            }
        }
        walk(current)
        return out
    }
}
