package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ClipEntity::class, LearnedWordEntity::class, TypingSessionEntity::class, EmojiUsageEntity::class, DailyStatsEntity::class, SpeedRecordEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clipDao(): ClipDao
    abstract fun learnedWordDao(): LearnedWordDao
    abstract fun typingSessionDao(): TypingSessionDao
    abstract fun emojiUsageDao(): EmojiUsageDao
    abstract fun speedRecordDao(): SpeedRecordDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nexkey_database"
                ).fallbackToDestructiveMigrationOnDowngrade()
                    .build().also { INSTANCE = it }
            }
        }
    }
}
