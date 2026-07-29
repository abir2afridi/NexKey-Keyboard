package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_stats")
data class DailyStatsEntity(
    @PrimaryKey val date: String, // YYYY-MM-DD
    val sessionCount: Int = 0,
    val totalKeys: Int = 0,
    val totalWords: Int = 0,
    val totalEmojis: Int = 0,
    val usageMinutes: Int = 0
)
