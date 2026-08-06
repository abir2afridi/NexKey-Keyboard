package com.example.prediction.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecentWordDao {
    @Insert
    suspend fun insert(entry: RecentWordEntity)

    @Query("DELETE FROM recent_words WHERE id NOT IN (SELECT id FROM recent_words ORDER BY usedAt DESC LIMIT :keep)")
    suspend fun trimTo(keep: Int)

    @Query("SELECT * FROM recent_words ORDER BY usedAt DESC LIMIT :limit")
    suspend fun getRecent(limit: Int): List<RecentWordEntity>

    @Query("DELETE FROM recent_words")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM recent_words")
    suspend fun count(): Int
}
