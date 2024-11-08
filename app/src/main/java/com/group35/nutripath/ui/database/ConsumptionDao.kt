package com.group35.nutripath.ui.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumptionDao {
    @Insert
    suspend fun insertConsumption(c: Consumption)

    @Query("DELETE FROM consumption_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM consumption_table")
    fun getAll(): Flow<List<Consumption>>

    @Query("""
        SELECT SUM(count * f.price) AS totalSpending
        FROM (
            SELECT food_id, COUNT(*) AS count
            FROM consumption_table
            WHERE strftime('%Y-%m', date) = :month
            GROUP BY food_id
        ) AS consumptionCount
        INNER JOIN food_table AS f ON consumptionCount.food_id = f.id
    """)
    suspend fun getTotalSpendingForMonth(month: String): Double // FORMAT: YYYY-MM

    @Query("""
        SELECT SUM(f.calories * count) AS totalCalories
        FROM (
            SELECT food_id, COUNT(*) AS count
            FROM consumption_table
            WHERE date = :date
            GROUP BY food_id
        ) AS consumptionCount
        INNER JOIN food_table AS f ON consumptionCount.food_id = f.id
    """)
    suspend fun getTotalCaloriesForDay(date: String): Double // SQLITE DATE FORMAT
}