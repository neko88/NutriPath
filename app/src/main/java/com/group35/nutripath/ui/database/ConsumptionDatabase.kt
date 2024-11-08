package com.group35.nutripath.ui.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Consumption::class, FoodItem::class], version = 3)
abstract class ConsumptionDatabase : RoomDatabase() {
    abstract val consumptionDao: ConsumptionDao

    companion object {
        // instance visible to all threads
        @Volatile
        private var INSTANCE: ConsumptionDatabase? = null

        fun getInstance(context: Context): ConsumptionDatabase {
            // prevent multiple threads from creating more than one instance
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ConsumptionDatabase::class.java, "consumption_table"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
