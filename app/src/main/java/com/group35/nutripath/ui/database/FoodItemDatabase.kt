package com.group35.nutripath.ui.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [FoodItem::class], version = 3)
abstract class FoodItemDatabase : RoomDatabase() {
    abstract val foodItemDao: FoodItemDao

    companion object {
        // instance visible to all threads
        @Volatile
        private var INSTANCE: FoodItemDatabase? = null

        fun getInstance(context: Context): FoodItemDatabase {
            // prevent multiple threads from creating more than one instance
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FoodItemDatabase::class.java, "food_table"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
