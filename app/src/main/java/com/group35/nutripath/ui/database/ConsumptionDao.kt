package com.group35.nutripath.ui.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumptionDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItem(f: FoodItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsumption(consumption: Consumption)

    @Query("DELETE FROM consumption_table")
    suspend fun deleteAllConsumption()

    @Query("DELETE FROM food_table")
    suspend fun deleteAllFood()

    @Transaction
    suspend fun insertFoodWithConsumption(food: FoodItem, consumption: Consumption) {
        val foodId = insertFoodItem(food)
        // ensure the foodId matches
        val updatedConsumption = consumption.copy(foodId = food.id)
        insertConsumption(updatedConsumption)
    }
    @Query("SELECT * FROM consumption_table")
    fun getAllConsumption(): Flow<List<Consumption>>

    @Query("SELECT * FROM food_table")
    fun getAllFood(): Flow<List<FoodItem>>

    // returns all consumption of a given food within the time frame provided

    @Transaction
    @Query("""
        SELECT SUM(f.price) FROM food_table AS f INNER JOIN consumption_table AS c ON f.id = c.foodId
        WHERE c.date >= :start AND c.date < :end
    """)
    suspend fun getTotalSpendingForMonth(start: Long, end: Long): Double? // FORMAT: YYYY-MM

    @Transaction
    @Query("""
        SELECT SUM(f.calories) FROM food_table AS f INNER JOIN consumption_table AS c ON f.id = c.foodId
        WHERE c.date >= :start AND c.date < :end
    """)
    suspend fun getTotalCaloriesForDay(start: Long, end: Long): Double?// SQLITE DATE FORMAT


    @Transaction
    @Query("""
        SELECT SUM(f.fats) FROM food_table AS f INNER JOIN consumption_table AS c ON f.id = c.foodId
        WHERE c.date >= :start AND c.date < :end
    """)
    suspend fun getTotalFatsForDay(start: Long, end: Long): Double? // SQLITE DATE FORMAT

    @Transaction
    @Query("""
        SELECT SUM(f.carbs) FROM food_table AS f INNER JOIN consumption_table AS c ON f.id = c.foodId
        WHERE c.date >= :start AND c.date < :end
    """)
    suspend fun getTotalCarbsForDay(start: Long, end: Long): Double? // SQLITE DATE FORMAT

    @Transaction
    @Query("""
        SELECT SUM(f.protein) FROM food_table AS f INNER JOIN consumption_table AS c ON f.id = c.foodId
        WHERE c.date >= :start AND c.date < :end
    """)
    suspend fun getTotalProteinForDay(start: Long, end: Long): Double? // SQLITE DATE FORMAT

    @Transaction
    @Query("""
        SELECT SUM(f.sugars) FROM food_table AS f INNER JOIN consumption_table AS c ON f.id = c.foodId
        WHERE c.date >= :start AND c.date < :end
    """)
    suspend fun getTotalSugarsForDay(start: Long, end: Long): Double? // SQLITE DATE FORMAT

}