package com.group35.nutripath.ui.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {
    @Insert
    suspend fun insertFood(f: FoodItem)

    @Query("SELECT * FROM food_table")
    fun getAllFood(): Flow<List<FoodItem>>

    @Query("DELETE FROM food_table")
    fun deleteAllFood()
}