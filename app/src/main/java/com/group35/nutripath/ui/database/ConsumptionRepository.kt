package com.group35.nutripath.ui.database

import androidx.room.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ConsumptionRepository(private val consumptionDao: ConsumptionDao) {

    val allConsumption: Flow<List<Consumption>> = consumptionDao.getAllConsumption()
    val allFoodItems: Flow<List<FoodItem>> = consumptionDao.getAllFood()

    suspend fun insertConsumption(consumption: Consumption){
        consumptionDao.insertConsumption(consumption)
    }
    suspend fun insertFood(foodItem: FoodItem){
        consumptionDao.insertFoodItem(foodItem)
    }

    suspend fun deleteAllConsumption(){
        consumptionDao.deleteAllConsumption()
    }

    suspend fun insertFoodWithConsumption(food: FoodItem, con: Consumption){
        consumptionDao.insertFoodWithConsumption(food, con)
    }

    suspend fun deleteAllFood(){
        consumptionDao.deleteAllFood()
    }

    suspend fun getMonthlySpending(start: Long, end: Long): Double? {
        return consumptionDao.getTotalSpendingForMonth(start, end)
    }
    suspend fun getTotalCaloriesForDay(start: Long, end: Long): Double? {
        return consumptionDao.getTotalCaloriesForDay(start, end)
    }
    suspend fun getTotalFatsForDay(start: Long, end: Long): Double? {
        return consumptionDao.getTotalFatsForDay(start, end)
    }
    suspend fun getTotalCarbsForDay(start: Long, end: Long): Double? {
        return consumptionDao.getTotalCarbsForDay(start, end)
    }
    suspend fun getTotalProteinForDay(start: Long, end: Long): Double? {
        return consumptionDao.getTotalProteinForDay(start, end)
    }
    suspend fun getTotalSugarsForDay(start: Long, end: Long): Double? {
        return consumptionDao.getTotalSugarsForDay(start, end)
    }
}
