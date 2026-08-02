package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeedRecordDao {
    @Insert
    suspend fun insert(record: SpeedRecordEntity)

    @Query("SELECT * FROM speed_records ORDER BY recordAt DESC")
    fun allRecords(): Flow<List<SpeedRecordEntity>>

    @Query("SELECT * FROM speed_records WHERE intervalLabel = :label ORDER BY recordAt ASC")
    fun recordsForInterval(label: String): Flow<List<SpeedRecordEntity>>

    @Query("SELECT * FROM speed_records WHERE intervalLabel = :label ORDER BY speed DESC LIMIT 1")
    suspend fun bestForInterval(label: String): SpeedRecordEntity?

    @Query("SELECT COALESCE(MAX(streak), 0) FROM speed_records WHERE intervalLabel = :label")
    suspend fun maxStreak(label: String): Int
}