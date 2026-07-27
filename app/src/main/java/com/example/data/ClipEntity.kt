package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clips")
data class ClipEntity(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isPinned: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
