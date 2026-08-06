package com.example.prediction.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** User-bookmarked dictionary words (Phase 8 Dictionary Search UI). */
@Entity(tableName = "favorite_words")
data class FavoriteWordEntity(
    @PrimaryKey val word: String,
    val addedAt: Long = System.currentTimeMillis()
)
