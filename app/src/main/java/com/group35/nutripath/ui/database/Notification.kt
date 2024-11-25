package com.group35.nutripath.ui.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications_table")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "content")
    val content: String = "",

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = 0L
)
