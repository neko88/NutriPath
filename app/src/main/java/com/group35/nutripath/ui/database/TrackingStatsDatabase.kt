package com.group35.nutripath.ui.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TrackingStats::class], version = 2)
abstract class TrackingStatsDatabase : RoomDatabase() {
    abstract fun trackingStatsDao(): TrackingStatsDao

    companion object {
        @Volatile
        private var INSTANCE: TrackingStatsDatabase? = null

        fun getInstance(context: Context): TrackingStatsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrackingStatsDatabase::class.java,
                    "tracking_stats_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}