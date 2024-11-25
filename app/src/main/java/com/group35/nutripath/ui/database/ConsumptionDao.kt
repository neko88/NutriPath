package com.group35.nutripath.ui.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumptionDao {
    @Insert
    suspend fun insertConsumption(c: Consumption)

    @Insert
    suspend fun insertFoodItem(f: FoodItem)
    @Query("DELETE FROM consumption_table")
    suspend fun deleteAll()

    @Transaction
    suspend fun insertFoodWithConsumption(f: FoodItem, c: Consumption){
        insertFoodItem(f)
        insertConsumption(c)
    }

    @Query("SELECT * FROM consumption_table")
    fun getAll(): Flow<List<Consumption>>

    @Query("SELECT * FROM consumption_table AS c INNER JOIN food_table AS f ON f.id = c.id")
    fun getSome(): Flow<List<Consumption>>

    @Query("""
        SELECT SUM(count * f.price) AS totalSpending
        FROM (
            SELECT food_id, COUNT(*) AS count
            FROM consumption_table
            WHERE date >= :start AND date < :end
            GROUP BY food_id
        ) AS consumptionCount
        INNER JOIN food_table AS f ON consumptionCount.food_id = f.id
    """)
    suspend fun getTotalSpendingForMonth(start: Long, end: Long): Double? // FORMAT: YYYY-MM

    @Query("""
    SELECT SUM(COALESCE(f.calories, 0.0) * consumptionCount.count) AS totalCalories
    FROM (
        SELECT food_id, COUNT(*) AS count
        FROM consumption_table
        WHERE date >= :start AND date < :end
        GROUP BY food_id
    ) AS consumptionCount
    INNER JOIN food_table AS f ON consumptionCount.food_id = f.id
""")
    fun getTotalCaloriesForDay(start: Long, end: Long): Flow<Double> // SQLITE DATE FORMAT


    @Query("""
        SELECT SUM(f.fats * count) AS totalFats
        FROM (
            SELECT food_id, COUNT(*) AS count 
            FROM consumption_table
            WHERE date >= :start AND date < :end
            GROUP BY food_id
            ) AS consumptionCount
            INNER JOIN food_table AS f ON consumptionCount.food_id = f.id
    """)
    suspend fun getTotalFatsForDay(start: Long, end: Long): Double? // SQLITE DATE FORMAT

    @Query("""
        SELECT SUM(f.carbs * count) AS totalCarbs
        FROM (
            SELECT food_id, COUNT(*) AS count 
            FROM consumption_table
            WHERE date >= :start AND date < :end
            GROUP BY food_id
            ) AS consumptionCount
            INNER JOIN food_table AS f ON consumptionCount.food_id = f.id
    """)
    suspend fun getTotalCarbsForDay(start: Long, end: Long): Double? // SQLITE DATE FORMAT
    @Query("""
        SELECT SUM(f.protein * count) AS totalProtein
        FROM (
            SELECT food_id, COUNT(*) AS count 
            FROM consumption_table
            WHERE date >= :start AND date < :end
            GROUP BY food_id
            ) AS consumptionCount
            INNER JOIN food_table AS f ON consumptionCount.food_id = f.id
    """)
    suspend fun getTotalProteinForDay(start: Long, end: Long): Double? // SQLITE DATE FORMAT

    @Query("""
        SELECT SUM(f.sugars * count) AS totalSugars
        FROM (
            SELECT food_id, COUNT(*) AS count 
            FROM consumption_table
            WHERE date >= :start AND date < :end
            GROUP BY food_id
            ) AS consumptionCount
            INNER JOIN food_table AS f ON consumptionCount.food_id = f.id
    """)
    suspend fun getTotalSugarsForDay(start: Long, end: Long): Double? // SQLITE DATE FORMAT

}