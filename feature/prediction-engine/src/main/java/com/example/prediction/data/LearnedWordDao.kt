package com.example.prediction.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LearnedWordDao {
    @Query("SELECT * FROM learned_words WHERE word = :word LIMIT 1")
    suspend fun findWord(word: String): LearnedWordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(word: LearnedWordEntity)

    @Query("SELECT * FROM learned_words ORDER BY frequency DESC LIMIT :limit")
    suspend fun getTopWords(limit: Int): List<LearnedWordEntity>

    @Query("SELECT * FROM learned_words WHERE isBangla = :isBangla ORDER BY frequency DESC LIMIT :limit")
    suspend fun getTopWordsByLanguage(isBangla: Boolean, limit: Int): List<LearnedWordEntity>

    @Query("SELECT * FROM learned_words ORDER BY frequency DESC")
    suspend fun getAllWords(): List<LearnedWordEntity>

    @Query("DELETE FROM learned_words WHERE word = :word")
    suspend fun deleteWord(word: String)

    @Query("DELETE FROM learned_words")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM learned_words")
    suspend fun count(): Int
}
