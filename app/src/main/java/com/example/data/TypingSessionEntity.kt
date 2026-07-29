package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "typing_sessions")
data class TypingSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Long,
    val endTime: Long,
    val keyCount: Int = 0,
    val wordCount: Int = 0,
    val emojiCount: Int = 0
)
