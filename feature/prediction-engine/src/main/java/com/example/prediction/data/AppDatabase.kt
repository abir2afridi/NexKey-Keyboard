package com.example.prediction.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Prediction-engine database — physically separate from the app's other tables so
 * "clear learned words" wipes only this file and builtin assets are never affected.
 */
@Database(
    entities = [
        LearnedWordEntity::class,
        LearnedPhraseEntity::class,
        PersonalWordEntity::class,
        RecentWordEntity::class,
        FavoriteWordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun learnedWordDao(): LearnedWordDao
    abstract fun learnedPhraseDao(): LearnedPhraseDao
    abstract fun personalWordDao(): PersonalWordDao
    abstract fun recentWordDao(): RecentWordDao
    abstract fun favoriteWordDao(): FavoriteWordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nexkey_prediction_database"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
