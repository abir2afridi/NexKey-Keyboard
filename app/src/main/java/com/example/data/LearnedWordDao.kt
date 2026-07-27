package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LearnedWordDao {
    @Query("SELECT * FROM learned_words WHERE word LIKE :prefix || '%' AND isBangla = :isBangla ORDER BY frequency DESC LIMIT :limit")
    suspend fun getPredictions(prefix: String, isBangla: Boolean, limit: Int): List<LearnedWordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(word: LearnedWordEntity)

    @Query("SELECT * FROM learned_words WHERE word = :word LIMIT 1")
    suspend fun findWord(word: String): LearnedWordEntity?

    @Query("SELECT * FROM learned_words ORDER BY frequency DESC")
    suspend fun getAllWords(): List<LearnedWordEntity>

    @Query("DELETE FROM learned_words WHERE word = :word")
    suspend fun deleteWord(word: String)

    @Query("DELETE FROM learned_words")
    suspend fun clearAll()
}
