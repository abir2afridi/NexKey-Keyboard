package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speed_records")
data class SpeedRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val intervalLabel: String, // "5s", "10s", "1min"
    val intervalMs: Long,
    val recordAt: Long,
    val wordCount: Int,
    val keyCount: Int,
    val speed: Float, // CPS (chars per second) or CPM (chars per minute) depending on interval
    val streak: Int
)