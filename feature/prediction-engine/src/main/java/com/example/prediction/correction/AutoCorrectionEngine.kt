package com.example.prediction.correction

import com.example.prediction.builtin.BuiltInDictionaryRepository
import com.example.prediction.engine.CandidateSource
import com.example.prediction.engine.CorrectionResult
import com.example.prediction.personal.PersonalTrieIndex

/**
 * Typo correction via weighted Damerau-Levenshtein edit distance (insertion, deletion,
 * substitution with QWERTY key-distance cost, and adjacent transposition), bounded to
 * edit distance <= 2 and searched directly against DAWG paths — never a full dictionary
 * scan. "recive"→"receive" etc. fall out of the algorithm; no misspelling pairs are
 * hardcoded (sources: Birkbeck-class spelling-error corpora via the dictionary pipeline).
 */
open class AutoCorrectionEngine(
    private val builtin: BuiltInDictionaryRepository,
    private val personalTrie: PersonalTrieIndex,
    private val threshold: () -> Int
) {

    private val maxDistance = 2.0
    private val minFreqForDistanceTwo = 200
    private var visitedBudget = 20000

    fun setVisitedBudget(budget: Int) {
        visitedBudget = budget
    }

    /**
     * @param correctionEnabled the single live gate (autocorrect toggle).
     */
    open fun correct(token: String, isBangla: Boolean, now: Long, correctionEnabled: Boolean): CorrectionResult? {
        if (!correctionEnabled) return null
        val word = token.trim().lowercase()
        if (word.length < 3 || word.length > 30) return null
        if (builtin.contains(word, isBangla)) return null
        if (personalTrie.get(word)?.frequency?.let { it >= threshold() } == true) return null

        val candidates = ArrayList<Triple<String, Double, Int>>() // word, distance, freq
        var visited = 0

        val index = builtin.index(isBangla) ?: return null
        val typed = word
        val len = typed.length

        val initCosts = IntArray(len + 1) { it } // path = "" -> distance to typed[0..j) = j
        val prefix = StringBuilder()

        fun searchDawg(state: Int, costs: DoubleArray, prevPrevCosts: DoubleArray, prevPathChar: Char, depth: Int) {
            if (visited++ > visitedBudget) return
            val dAtNode = costs[len]
            if (index.isWordAt(state) && dAtNode <= maxDistance) {
                candidates.add(Triple(prefix.toString(), dAtNode, index.freqAt(state)))
            }
            val edgeCount = index.edgeCount(state)
            var i = 0
            while (i < edgeCount && visited <= visitedBudget) {
                val (ch, target) = index.edgeAt(state, i)
                val newCosts = transition(costs, prevPrevCosts, typed, ch, prevPathChar, len)
                if (minOfArray(newCosts, len) <= maxDistance) {
                    prefix.append(ch)
                    searchDawg(target, newCosts, costs, ch, depth + 1)
                    prefix.deleteCharAt(prefix.length - 1)
                }
                i++
            }
        }

        searchDawg(
            index.rootState,
            DoubleArray(len + 1) { it.toDouble() },
            DoubleArray(len + 1),
            '\u0000',
            0
        )

        for ((personalWord, frequency, _) in personalTrie.allWords()) {
            if (frequency < threshold()) continue
            val dist = weightedOsa(word, personalWord)
            if (dist <= maxDistance) candidates.add(Triple(personalWord, dist, 500 + frequency))
        }

        val best = candidates
            .asSequence()
            .filter { it.second <= maxDistance }
            .filter { it.first != word }
            .filter { it.second < 2.0 || it.third >= minFreqForDistanceTwo }
            .minWithOrNull(compareBy({ it.second }, { -it.third }))
            ?: return null

        return CorrectionResult(
            original = word,
            correction = best.first,
            distance = best.second,
            source = if (best.third > 1000) CandidateSource.PERSONAL else CandidateSource.BUILTIN
        )
    }

    private fun transition(
        costs: DoubleArray,
        prevPrevCosts: DoubleArray,
        typed: String,
        ch: Char,
        prevPathChar: Char,
        len: Int
    ): DoubleArray {
        val newCosts = DoubleArray(len + 1)
        newCosts[0] = costs[0] + 1.0
        for (j in 1..len) {
            var best = minOf(
                costs[j] + 1.0,
                newCosts[j - 1] + 1.0,
                costs[j - 1] + KeyboardProximityMap.substitutionCost(ch, typed[j - 1])
            )
            // OSA transposition: the path's trailing (x,y) aligns with typed (y,x).
            // The fallback must be the row from TWO steps back (before x was appended),
            // i.e. matrix[i-2][j-2] + 1 in full-matrix terms.
            if (j >= 2 && ch == typed[j - 2] && typed[j - 1] == prevPathChar) {
                best = minOf(best, prevPrevCosts[j - 2] + 1.0)
            }
            newCosts[j] = best
        }
        return newCosts
    }

    private fun minOfArray(a: DoubleArray, len: Int): Double {
        var min = a[0]
        for (i in 1..len) if (a[i] < min) min = a[i]
        return min
    }

    /** Plain weighted OSA between two complete strings (for the small personal set). */
    fun weightedOsa(a: String, b: String): Double {
        val n = a.length
        val m = b.length
        var prev = DoubleArray(m + 1) { it.toDouble() }
        var prev2 = DoubleArray(m + 1)
        for (i in 1..n) {
            val cur = DoubleArray(m + 1)
            cur[0] = i.toDouble()
            for (j in 1..m) {
                var best = minOf(
                    prev[j] + 1.0,
                    cur[j - 1] + 1.0,
                    prev[j - 1] + KeyboardProximityMap.substitutionCost(a[i - 1], b[j - 1])
                )
                if (i >= 2 && j >= 2 && a[i - 1] == b[j - 2] && a[i - 2] == b[j - 1]) {
                    best = minOf(best, prev2[j - 2] + 1.0)
                }
                cur[j] = best
            }
            prev2 = prev
            prev = cur
        }
        return prev[m]
    }
}
