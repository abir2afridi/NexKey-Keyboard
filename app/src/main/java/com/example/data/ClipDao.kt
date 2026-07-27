package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ClipDao {
    @Query("SELECT * FROM clips ORDER BY isPinned DESC, timestamp DESC")
    suspend fun getAllClips(): List<ClipEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClip(clip: ClipEntity)

    @Query("UPDATE clips SET isPinned = NOT isPinned WHERE id = :clipId")
    suspend fun togglePin(clipId: String)

    @Query("DELETE FROM clips WHERE id = :clipId")
    suspend fun deleteClip(clipId: String)

    @Query("DELETE FROM clips WHERE isPinned = 0")
    suspend fun clearAllUnpinned()

    @Query("SELECT * FROM clips WHERE text = :text LIMIT 1")
    suspend fun findClipByText(text: String): ClipEntity?
}
