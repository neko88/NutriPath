package com.group35.nutripath.ui.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert
    suspend fun insertNotification(notification: Notification)

    @Query("SELECT * FROM notifications_table")
    fun getAllNotifications(): Flow<List<Notification>>

    @Query("DELETE FROM notifications_table")
    fun deleteAllNotifications()

    @Query("DELETE FROM notifications_table WHERE id = :key")
    suspend fun deleteNotification(key: Long)
}