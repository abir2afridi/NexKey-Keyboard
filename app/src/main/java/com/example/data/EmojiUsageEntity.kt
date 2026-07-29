package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emoji_usage")
data class EmojiUsageEntity(
    @PrimaryKey val emoji: String,
    val frequency: Int = 1,
    val lastUsedAt: Long = System.currentTimeMillis()
)
