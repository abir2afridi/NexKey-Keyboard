package com.example.prediction.builtin

import java.io.ByteArrayInputStream
import java.io.DataInputStream

/**
 * Read-only, memory-resident DAWG (minimized DFA) loaded from a binary asset.
 * States' edges are stored in flat parallel arrays; lookup is O(word length) with
 * binary search per state. Suffix sharing keeps memory far below a raw trie.
 */
class DawgIndex {

    private var edgeChar: CharArray = CharArray(0)
    private var edgeTarget: IntArray = IntArray(0)
    private var edgeStart: IntArray = IntArray(1) { 0 }
    private var wordFlag: BooleanArray = BooleanArray(0)
    private var wordFreq: IntArray = IntArray(0)

    var stateCount: Int = 0
        private set
    var wordCount: Int = 0
        private set
    var rootState: Int = 0
        private set

    fun load(data: ByteArray) {
        val input = DataInputStream(ByteArrayInputStream(data))
        val magic = ByteArray(8)
        input.readFully(magic)
        require(String(magic, Charsets.US_ASCII) == "NEXDAWG1") { "Not a NexKey DAWG asset" }
        wordCount = input.readInt()
        stateCount = input.readInt()
        rootState = input.readInt()

        val chars = StringBuilder()
        val targets = ArrayList<Int>(stateCount * 4)
        val starts = IntArray(stateCount + 1)
        val wordFlags = BooleanArray(stateCount)
        val freqs = IntArray(stateCount)
        var edgeCount = 0
        for (id in 0 until stateCount) {
            starts[id] = edgeCount
            val flags = input.readByte()
            wordFlags[id] = (flags.toInt() and 0x01) != 0
            if (wordFlags[id]) freqs[id] = input.readInt()
            val n = input.readByte().toInt() and 0xFF
            repeat(n) {
                val ch = input.readChar()
                val target = input.readInt()
                chars.append(ch)
                targets.add(target)
                edgeCount++
            }
        }
        starts[stateCount] = edgeCount

        edgeChar = chars.toString().toCharArray()
        edgeTarget = targets.toIntArray()
        edgeStart = starts
        wordFlag = wordFlags
        wordFreq = freqs
    }

    fun frequency(word: String): Int {
        var state = rootState
        for (ch in word) {
            state = child(state, ch)
            if (state < 0) return 0
        }
        return if (wordFlag[state]) wordFreq[state] else 0
    }

    fun contains(word: String): Boolean = frequency(word) > 0

    fun child(state: Int, ch: Char): Int {
        val from = edgeStart[state]
        val to = edgeStart[state + 1]
        var lo = from
        var hi = to - 1
        while (lo <= hi) {
            val mid = (lo + hi) ushr 1
            val c = edgeChar[mid]
            when {
                c < ch -> lo = mid + 1
                c > ch -> hi = mid - 1
                else -> return edgeTarget[mid]
            }
        }
        return -1
    }

    fun edgeCount(state: Int): Int = edgeStart[state + 1] - edgeStart[state]

    fun edgeAt(state: Int, index: Int): Pair<Char, Int> =
        edgeChar[edgeStart[state] + index] to edgeTarget[edgeStart[state] + index]

    fun isWordAt(state: Int): Boolean = wordFlag[state]

    fun freqAt(state: Int): Int = wordFreq[state]

    /**
     * Collects up to [limit] words with the highest frequency reachable from [state],
     * visiting at most [visitedBudget] nodes so short prefixes cannot stall typing.
     */
    fun collectTop(state: Int, prefix: String, limit: Int, visitedBudget: Int = 2000): List<Pair<String, Int>> {
        val results = ArrayList<Pair<String, Int>>()
        if (limit <= 0) return results
        val heap = java.util.PriorityQueue<Pair<String, Int>>(compareBy { it.second })
        val sb = StringBuilder(prefix)
        var visited = 0

        fun walk(s: Int) {
            if (visited++ >= visitedBudget) return
            if (isWordAt(s)) {
                heap.add(sb.toString() to freqAt(s))
                if (heap.size > limit) heap.poll()
            }
            val n = edgeCount(s)
            var i = 0
            while (i < n && visited < visitedBudget) {
                val (ch, target) = edgeAt(s, i)
                sb.append(ch)
                walk(target)
                sb.deleteCharAt(sb.length - 1)
                i++
            }
        }

        walk(state)
        val out = heap.sortedByDescending { it.second }
        return out
    }

    /** Top-freq words from the root (unigram fallback for n-gram backoff). */
    fun topWords(limit: Int): List<Pair<String, Int>> =
        collectTop(rootState, "", limit, visitedBudget = 4000)
}
