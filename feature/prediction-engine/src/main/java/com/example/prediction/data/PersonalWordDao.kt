package com.example.prediction.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PersonalWordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: PersonalWordEntity)

    @Query("SELECT * FROM personal_words WHERE word = :word LIMIT 1")
    suspend fun findWord(word: String): PersonalWordEntity?

    @Query("SELECT * FROM personal_words ORDER BY frequency DESC")
    suspend fun getAllWords(): List<PersonalWordEntity>

    @Query("DELETE FROM personal_words WHERE word = :word")
    suspend fun deleteWord(word: String)

    @Query("DELETE FROM personal_words")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM personal_words")
    suspend fun count(): Int
}
