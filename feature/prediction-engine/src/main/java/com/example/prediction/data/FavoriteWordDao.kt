package com.example.prediction.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteWordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteWordEntity)

    @Query("SELECT * FROM favorite_words ORDER BY addedAt DESC")
    suspend fun getAll(): List<FavoriteWordEntity>

    @Query("DELETE FROM favorite_words WHERE word = :word")
    suspend fun delete(word: String)

    @Query("DELETE FROM favorite_words")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM favorite_words")
    suspend fun count(): Int
}
