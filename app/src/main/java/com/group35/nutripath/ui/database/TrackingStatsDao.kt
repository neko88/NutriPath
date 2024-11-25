package com.group35.nutripath.ui.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackingStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackingStats(stats: TrackingStats)

    @Query("SELECT * FROM tracking_stats WHERE date = :date LIMIT 1")
    suspend fun getStatsForDate(date: String): TrackingStats?

    @Query("UPDATE tracking_stats SET time = :time, distance = :distance, calories = :calories WHERE date = :date")
    suspend fun updateTrackingStats(date: String, time: String, distance: Double, calories: Int)
}