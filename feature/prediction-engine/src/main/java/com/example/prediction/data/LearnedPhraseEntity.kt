package com.example.prediction.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Auto-learned bigram/trigram ("vhalo" followed by "lagtase"), for next-word + phrase prediction. */
@Entity(tableName = "learned_phrases")
data class LearnedPhraseEntity(
    @PrimaryKey val key: String,
    val firstWord: String,
    val secondWord: String,
    val thirdWord: String? = null,
    val isBangla: Boolean = false,
    val frequency: Int = 1,
    val lastUsedAt: Long = System.currentTimeMillis()
)
