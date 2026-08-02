package com.example.data

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object TypingAnalytics {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var sessionStartTime: Long = 0
    private var sessionKeyCount: Int = 0
    private var sessionWordCount: Int = 0
    private var sessionEmojiCount: Int = 0

    private var db: AppDatabase? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun init(context: Context) {
        db = AppDatabase.getInstance(context)
    }

    fun startSession() {
        sessionStartTime = System.currentTimeMillis()
        sessionKeyCount = 0
        sessionWordCount = 0
        sessionEmojiCount = 0
    }

    fun endSession() {
        val now = System.currentTimeMillis()
        val duration = now - sessionStartTime
        if (duration < 1000) return // ignore <1s sessions

        val minutes = ((duration / 60000) + 1).toInt()
        val date = dateFormat.format(Date(now))

        scope.launch {
            db?.typingSessionDao()?.insert(
                TypingSessionEntity(
                    startTime = sessionStartTime,
                    endTime = now,
                    keyCount = sessionKeyCount,
                    wordCount = sessionWordCount,
                    emojiCount = sessionEmojiCount
                )
            )

            if (db?.typingSessionDao()?.dailyStatsExist(date) == true) {
                db?.typingSessionDao()?.updateDailyStats(
                    date = date,
                    keys = sessionKeyCount,
                    words = sessionWordCount,
                    emojis = sessionEmojiCount,
                    minutes = minutes
                )
            } else {
                db?.typingSessionDao()?.insertDailyStats(
                    DailyStatsEntity(
                        date = date,
                        sessionCount = 1,
                        totalKeys = sessionKeyCount,
                        totalWords = sessionWordCount,
                        totalEmojis = sessionEmojiCount,
                        usageMinutes = minutes
                    )
                )
            }
        }
    }

    fun trackKeyPress() {
        sessionKeyCount++
    }

    fun trackWord() {
        sessionWordCount++
    }

    fun trackEmoji(emoji: String) {
        sessionEmojiCount++
        scope.launch {
            val dao = db?.emojiUsageDao() ?: return@launch
            val updated = dao.increment(emoji)
            if (updated == 0) {
                dao.upsert(
                    EmojiUsageEntity(
                        emoji = emoji,
                        frequency = 1,
                        lastUsedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun getDatabase(): AppDatabase? = db
}
