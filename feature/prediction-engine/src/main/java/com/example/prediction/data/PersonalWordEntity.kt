package com.example.prediction.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Manually added words via the Dictionary UI (Phase 9). Never merged with learned rows. */
@Entity(tableName = "personal_words")
data class PersonalWordEntity(
    @PrimaryKey val word: String,
    val isBangla: Boolean = false,
    val languageTag: String = "en",
    val frequency: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)
