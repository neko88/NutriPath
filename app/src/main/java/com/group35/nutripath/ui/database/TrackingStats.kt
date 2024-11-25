package com.group35.nutripath.ui.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracking_stats")
data class TrackingStats(
    @PrimaryKey val date: String,
    val time: String,
    val distance: Double,
    val calories: Int
)