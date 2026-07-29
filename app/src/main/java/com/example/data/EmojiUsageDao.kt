package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EmojiUsageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(emoji: EmojiUsageEntity)

    @Query("UPDATE emoji_usage SET frequency = frequency + 1, lastUsedAt = :now WHERE emoji = :emoji")
    suspend fun increment(emoji: String, now: Long = System.currentTimeMillis())

    @Query("SELECT * FROM emoji_usage ORDER BY frequency DESC LIMIT :limit")
    suspend fun getTopEmojis(limit: Int = 20): List<EmojiUsageEntity>

    @Query("SELECT COUNT(*) FROM emoji_usage")
    suspend fun getEmojiCount(): Int
}
