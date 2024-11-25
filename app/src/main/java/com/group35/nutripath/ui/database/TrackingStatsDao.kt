package com.group35.nutripath.ui.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackingStatsDao {

    // use REPLACE strategy to update existing records with the same date
    // next one should just add the newer data into existing records
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackingStats(stats: TrackingStats)

    @Query("SELECT * FROM tracking_stats WHERE date = :date LIMIT 1")
    suspend fun getStatsForDate(date: String): TrackingStats?

    @Query("SELECT * FROM tracking_stats")
    suspend fun getAllStats(): List<TrackingStats>
}