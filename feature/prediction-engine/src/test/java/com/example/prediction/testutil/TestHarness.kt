package com.example.prediction.testutil

import androidx.room.Room
import com.example.prediction.builtin.BuiltInDictionaryRepository
import com.example.prediction.builtin.DawgBuilder
import com.example.prediction.data.AppDatabase
import com.example.prediction.data.LearnedPhraseDao
import com.example.prediction.data.LearnedWordDao
import com.example.prediction.data.PersonalWordDao
import com.example.prediction.data.RecentWordDao
import com.example.prediction.ngram.NgramIndex
import com.example.prediction.personal.LearningEngine
import com.example.prediction.personal.PersonalTrieIndex
import com.example.prediction.ranking.RankingEngine
import org.robolectric.RuntimeEnvironment

object TestHarness {

    val syntheticEnglishWords = listOf(
        "the" to 5000, "and" to 4000, "good" to 3000, "morning" to 1500, "night" to 1300,
        "luck" to 900, "job" to 800, "receive" to 700, "believe" to 650, "definitely" to 550,
        "environment" to 500, "happy" to 600, "heart" to 300, "great" to 700, "hello" to 900
    )

    fun englishRepository(): BuiltInDictionaryRepository {
        val repo = BuiltInDictionaryRepository()
        repo.loadFromBytes(
            englishBytes = DawgBuilder.build(syntheticEnglishWords),
            banglaBytes = null
        )
        return repo
    }

    fun database(): AppDatabase = Room.inMemoryDatabaseBuilder(
        RuntimeEnvironment.getApplication(),
        AppDatabase::class.java
    ).allowMainThreadQueries().build()

    fun learningEngine(
        db: AppDatabase,
        trie: PersonalTrieIndex,
        learningEnabled: () -> Boolean = { true },
        incognitoEnabled: () -> Boolean = { false }
    ): LearningEngine = LearningEngine(
        learnedWordDao = db.learnedWordDao(),
        learnedPhraseDao = db.learnedPhraseDao(),
        personalWordDao = db.personalWordDao(),
        recentWordDao = db.recentWordDao(),
        personalTrie = trie,
        learningEnabled = learningEnabled,
        incognitoEnabled = incognitoEnabled
    )

    fun ranking(): RankingEngine = RankingEngine()

    fun ngram(): NgramIndex = NgramIndex()

    /** Mirrors DictionaryManager.init: loads Room-learned phrases into the in-memory NgramIndex. */
    suspend fun loadNgramsFromDb(db: AppDatabase, ngram: NgramIndex) {
        for (phrase in db.learnedPhraseDao().getTopPhrases(2000)) {
            ngram.learn(phrase.firstWord, phrase.secondWord, phrase.thirdWord)
        }
    }
}
