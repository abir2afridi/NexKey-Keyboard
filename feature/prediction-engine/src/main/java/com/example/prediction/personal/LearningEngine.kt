package com.example.prediction.personal

import com.example.prediction.data.LearnedPhraseDao
import com.example.prediction.data.LearnedPhraseEntity
import com.example.prediction.data.LearnedWordDao
import com.example.prediction.data.LearnedWordEntity
import com.example.prediction.data.PersonalWordDao
import com.example.prediction.data.RecentWordDao
import com.example.prediction.data.RecentWordEntity

/**
 * Writes personal learning data as the user types. Every gate (incognito, sensitive field,
 * personal-learning toggle) is checked here in ONE place so no learning write can leak
 * through. The in-memory trie is updated synchronously; Room writes happen in the same
 * suspend call. Learning threshold is a RANKING concern (see SuggestionEngine) — a word is
 * always stored, but only confidently surfaced once its frequency reaches the threshold.
 */
class LearningEngine(
    private val learnedWordDao: LearnedWordDao,
    private val learnedPhraseDao: LearnedPhraseDao,
    private val personalWordDao: PersonalWordDao,
    private val recentWordDao: RecentWordDao,
    private val personalTrie: PersonalTrieIndex,
    private val learningEnabled: () -> Boolean,
    private val incognitoEnabled: () -> Boolean
) {

    suspend fun loadExistingIntoMemory() {
        for (word in learnedWordDao.getAllWords()) {
            personalTrie.addWord(word.word, word.frequency, word.lastUsedAt)
        }
        for (word in personalWordDao.getAllWords()) {
            personalTrie.addWord(word.word, word.frequency, word.createdAt)
        }
    }

    /**
     * @param previousWords committed words before this one (for phrase/n-gram learning).
     */
    suspend fun onCommit(
        token: String,
        isBangla: Boolean,
        languageTag: String,
        previousWords: List<String>,
        now: Long,
        sensitiveField: Boolean
    ) {
        if (sensitiveField) return
        if (!learningEnabled()) return
        if (incognitoEnabled()) return
        val word = token.trim().lowercase()
        if (word.length < 2) return

        val existing = personalTrie.increment(word, now)
        val frequency = (existing?.frequency ?: 0) + 1
        learnedWordDao.insertOrUpdate(
            LearnedWordEntity(word = word, isBangla = isBangla, languageTag = languageTag, frequency = frequency, lastUsedAt = now)
        )

        if (previousWords.isNotEmpty()) {
            val first = previousWords.last()
            learnedPhraseDao.insertOrUpdate(
                LearnedPhraseEntity(
                    key = "$first|$word",
                    firstWord = first,
                    secondWord = word,
                    isBangla = isBangla,
                    frequency = 1,
                    lastUsedAt = now
                )
            )
        }
        if (previousWords.size >= 2) {
            val w1 = previousWords[previousWords.size - 2]
            val w2 = previousWords[previousWords.size - 1]
            val key = "$w1|$w2|$word"
            val existingTri = learnedPhraseDao.findPhrase(key)
            learnedPhraseDao.insertOrUpdate(
                existingTri?.copy(frequency = existingTri.frequency + 1, lastUsedAt = now)
                    ?: LearnedPhraseEntity(
                        key = key,
                        firstWord = w1,
                        secondWord = w2,
                        thirdWord = word,
                        isBangla = isBangla,
                        frequency = 1,
                        lastUsedAt = now
                    )
            )
        }

        recentWordDao.insert(RecentWordEntity(word = word, isBangla = isBangla, usedAt = now))
        recentWordDao.trimTo(200)
    }
}
