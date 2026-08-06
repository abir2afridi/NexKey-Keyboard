package com.example.prediction.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LearnedPhraseDao {
    @Query("SELECT * FROM learned_phrases WHERE key = :key LIMIT 1")
    suspend fun findPhrase(key: String): LearnedPhraseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(phrase: LearnedPhraseEntity)

    @Query(
        "SELECT * FROM learned_phrases WHERE firstWord = :first AND secondWord = :second " +
            "AND thirdWord IS NULL ORDER BY frequency DESC"
    )
    suspend fun getBigrams(first: String, second: String): List<LearnedPhraseEntity>

    @Query(
        "SELECT * FROM learned_phrases WHERE firstWord = :first AND secondWord = :second " +
            "AND thirdWord IS NOT NULL ORDER BY frequency DESC"
    )
    suspend fun getTrigrams(first: String, second: String): List<LearnedPhraseEntity>

    @Query("SELECT * FROM learned_phrases ORDER BY frequency DESC LIMIT :limit")
    suspend fun getTopPhrases(limit: Int): List<LearnedPhraseEntity>

    @Query("DELETE FROM learned_phrases")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM learned_phrases")
    suspend fun count(): Int
}
