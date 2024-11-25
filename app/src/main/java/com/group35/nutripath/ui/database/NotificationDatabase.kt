package com.group35.nutripath.ui.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Notification::class], version = 2)
abstract class NotificationDatabase: RoomDatabase() {
    abstract val notificationDao: NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: NotificationDatabase? = null

        fun getInstance(context: Context): NotificationDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, NotificationDatabase::class.java, "notifications_table").build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}