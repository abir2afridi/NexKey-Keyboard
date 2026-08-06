package com.example.prediction.builtin

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * Compiles a word list into a minimized DFA (DAWG) and serializes it to a binary asset.
 *
 * Pipeline: trie -> bottom-up minimization (hash-consing on canonical signatures) ->
 * serialization. The minimized DFA shares suffixes across words, so 50k words occupy a
 * fraction of the memory a raw trie would need while keeping O(word length) lookup.
 */
object DawgBuilder {

    private const val MAGIC = "NEXDAWG1"
    private const val FLAG_IS_WORD: Byte = 1

    private class Node {
        val children = HashMap<Char, Node>()
        var isWord = false
        var frequency = 0
    }

    /**
     * @param words (word, frequency) pairs. Frequencies are preserved for ranking.
     */
    fun build(words: List<Pair<String, Int>>): ByteArray {
        val root = Node()
        for ((word, frequency) in words) {
            var current = root
            for (ch in word) current = current.children.getOrPut(ch) { Node() }
            current.isWord = true
            current.frequency = maxOf(current.frequency, frequency)
        }

        val minimizedIds = HashMap<Node, Int>()
        val signatures = HashMap<String, Int>()
        var nextId = 0

        // Postorder minimization with hash-consing on canonical signatures. The root is
        // NEVER deduped against other states (a shared root would point into the middle of
        // the graph); it always receives a fresh id, which we serialize explicitly.
        fun assign(node: Node): Int {
            minimizedIds[node]?.let { return it }
            val key = StringBuilder()
            key.append(if (node.isWord) 'W' else '.')
            key.append(node.frequency)
            key.append('|')
            for ((ch, child) in node.children.entries.sortedBy { it.key }) {
                key.append(ch).append(':').append(assign(child)).append(';')
            }
            val sig = key.toString()
            val existing = signatures[sig]
            return if (existing != null) {
                minimizedIds[node] = existing
                existing
            } else {
                signatures[sig] = nextId
                minimizedIds[node] = nextId
                nextId++
            }
        }

        for ((_, child) in root.children.entries.sortedBy { it.key }) assign(child)
        val rootId = nextId
        minimizedIds[root] = rootId
        nextId++

        val idToNode = HashMap<Int, Node>()
        for ((node, id) in minimizedIds) idToNode[id] = node
        val stateCount = nextId

        val out = ByteArrayOutputStream()
        DataOutputStream(out).use { dos ->
            dos.writeBytes(MAGIC)
            dos.writeInt(words.size)
            dos.writeInt(stateCount)
            dos.writeInt(rootId)
            for (id in 0 until stateCount) {
                val node = idToNode[id]!!
                val edges = node.children.entries.sortedBy { it.key }
                val flags: Byte = if (node.isWord) FLAG_IS_WORD else 0
                dos.writeByte(flags.toInt())
                if (node.isWord) dos.writeInt(node.frequency)
                dos.writeByte(edges.size)
                for ((ch, child) in edges) {
                    dos.writeChar(ch.code)
                    dos.writeInt(minimizedIds[child]!!)
                }
            }
        }
        return out.toByteArray()
    }

    /**
     * Parses raw "word count" lines (e.g. hermitdave/FrequencyWords format) into
     * (word, frequency) pairs, keeping only letter-only tokens for the target language.
     */
    fun parseWordList(lines: List<String>, isBangla: Boolean): List<Pair<String, Int>> {
        val seen = HashMap<String, Int>()
        for (line in lines) {
            val parts = line.trim().split(Regex("\\s+"))
            if (parts.size < 2) continue
            val word = parts[0].trim().lowercase()
            val count = parts[1].toIntOrNull() ?: continue
            if (!isValidWord(word, isBangla)) continue
            if (word.length > 40) continue
            seen[word] = maxOf(seen[word] ?: 0, count)
        }
        return seen.entries.sortedBy { it.key }.map { it.key to it.value }
    }

    private fun isValidWord(word: String, isBangla: Boolean): Boolean {
        if (word.isEmpty()) return false
        for (ch in word) {
            if (isBangla) {
                if (ch.code !in 0x0980..0x09FF) return false
            } else {
                if (ch !in 'a'..'z') return false
            }
        }
        return true
    }
}
