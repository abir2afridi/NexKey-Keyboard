package com.example.prediction.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Auto-learned single word (any language incl. Banglish). Frequency + last-used for ranking. */
@Entity(tableName = "learned_words")
data class LearnedWordEntity(
    @PrimaryKey val word: String,
    val isBangla: Boolean = false,
    val languageTag: String = "en",
    val frequency: Int = 1,
    val lastUsedAt: Long = System.currentTimeMillis()
)
