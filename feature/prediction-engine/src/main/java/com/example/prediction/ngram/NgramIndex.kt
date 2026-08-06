package com.example.prediction.ngram

/**
 * Bigram/trigram continuation model with interpolated backoff
 * (see /docs/NGRAM_MODEL.md for the exact formula).
 *
 * Learned continuously from committed phrases (LearningEngine) and seeded from the
 * dictionary-builder pipeline when a full corpus is ingested. Pure in-memory map
 * structure — small for personal data, rebuilt on startup.
 */
class NgramIndex {

    private val bigrams = HashMap<String, HashMap<String, Int>>()
    private val trigrams = HashMap<String, HashMap<String, Int>>()

    @Synchronized
    fun learn(first: String, second: String, third: String?) {
        if (third == null) {
            bigrams.getOrPut(first) { HashMap() }.merge(second, 1, Int::plus)
        } else {
            trigrams.getOrPut("$first|$second") { HashMap() }.merge(third, 1, Int::plus)
        }
    }

    @Synchronized
    fun clear() {
        bigrams.clear()
        trigrams.clear()
    }

    /**
     * Interpolated backoff candidates for context [prevWords] (1 or 2 committed words).
     * Returns (nextWord, score) sorted descending; score ∈ [0,1].
     */
    @Synchronized
    fun nextCandidates(
        prevWords: List<String>,
        limit: Int,
        unigramScore: (String) -> Double
    ): List<Pair<String, Double>> {
        if (prevWords.isEmpty()) return emptyList()
        val candidates = HashMap<String, Double>()
        val w1 = prevWords.last()
        val w2 = if (prevWords.size >= 2) prevWords[prevWords.size - 2] else null

        val bigramMap = bigrams[w1]
        if (bigramMap != null) {
            val total = bigramMap.values.sum()
            if (w2 != null) {
                val triMap = trigrams["$w2|$w1"]
                if (triMap != null) {
                    val triTotal = triMap.values.sum()
                    for ((next, count) in triMap) {
                        candidates[next] = 0.6 * count / triTotal
                    }
                }
            }
            for ((next, count) in bigramMap) {
                val lambda2 = if (w2 != null && trigrams["$w2|$w1"]?.containsKey(next) == true) 0.3 else 0.9
                candidates[next] = (candidates[next] ?: 0.0) + lambda2 * count / total
            }
        }

        val result = candidates.mapValues { (word, score) -> score + 0.1 * unigramScore(word) }
            .entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { it.key to it.value }
        return result
    }
}
