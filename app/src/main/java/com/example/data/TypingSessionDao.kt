package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TypingSessionDao {
    @Insert
    suspend fun insert(session: TypingSessionEntity)

    @Query("SELECT * FROM typing_sessions ORDER BY startTime DESC LIMIT 1")
    suspend fun getLastSession(): TypingSessionEntity?

    @Query("SELECT * FROM typing_sessions WHERE startTime >= :since ORDER BY startTime DESC")
    fun getSessionsSince(since: Long): Flow<List<TypingSessionEntity>>

    @Query("SELECT * FROM typing_sessions WHERE startTime >= :since")
    suspend fun getSessionsSinceList(since: Long): List<TypingSessionEntity>

    @Query("SELECT SUM(keyCount) FROM typing_sessions WHERE startTime >= :since")
    suspend fun getTotalKeysSince(since: Long): Int?

    @Query("SELECT SUM(usageMinutes) FROM daily_stats WHERE date LIKE :yearMonth || '%'")
    suspend fun getMonthlyUsageMinutes(yearMonth: String): Int?

    @Query("SELECT * FROM daily_stats ORDER BY date ASC")
    fun getAllDailyStats(): Flow<List<DailyStatsEntity>>

    @Insert
    suspend fun insertDailyStats(stats: DailyStatsEntity)

    @Query("UPDATE daily_stats SET sessionCount = sessionCount + 1, totalKeys = totalKeys + :keys, totalWords = totalWords + :words, totalEmojis = totalEmojis + :emojis, usageMinutes = usageMinutes + :minutes WHERE date = :date")
    suspend fun updateDailyStats(date: String, keys: Int, words: Int, emojis: Int, minutes: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM daily_stats WHERE date = :date)")
    suspend fun dailyStatsExist(date: String): Boolean
}
