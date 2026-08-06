package com.example.prediction.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Rolling recent-words log (bounded, e.g. last 200 commits). */
@Entity(tableName = "recent_words")
data class RecentWordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String,
    val isBangla: Boolean = false,
    val usedAt: Long = System.currentTimeMillis()
)
