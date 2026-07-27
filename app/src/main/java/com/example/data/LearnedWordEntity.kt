package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learned_words")
data class LearnedWordEntity(
    @PrimaryKey val word: String,
    val isBangla: Boolean = false,
    val frequency: Int = 1,
    val lastUsedAt: Long = System.currentTimeMillis()
)
