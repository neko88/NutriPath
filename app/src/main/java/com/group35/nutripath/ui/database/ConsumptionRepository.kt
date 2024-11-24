package com.group35.nutripath.ui.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ConsumptionRepository(private val consumptionDao: ConsumptionDao) {

    val allConsumption: Flow<List<Consumption>> = consumptionDao.getAll()

    fun insert(consumption: Consumption){
        CoroutineScope(IO).launch{
            consumptionDao.insertConsumption(consumption)
        }
    }

    fun deleteAll(){
        CoroutineScope(IO).launch {
            consumptionDao.deleteAll()
        }
    }

    suspend fun getMonthlySpending(start: Long, end: Long): Double {
        return consumptionDao.getTotalSpendingForMonth(start, end)
    }
    suspend fun getTotalCaloriesForDay(start: Long, end: Long): Double {
        return consumptionDao.getTotalCaloriesForDay(start, end)
    }
    suspend fun getTotalFatsForDay(start: Long, end: Long): Double {
        return consumptionDao.getTotalFatsForDay(start, end)
    }
    suspend fun getTotalCarbsForDay(start: Long, end: Long): Double {
        return consumptionDao.getTotalCarbsForDay(start, end)
    }
    suspend fun getTotalProteinForDay(start: Long, end: Long): Double {
        return consumptionDao.getTotalProteinForDay(start, end)
    }
    suspend fun getTotalSugarsForDay(start: Long, end: Long): Double {
        return consumptionDao.getTotalSugarsForDay(start, end)
    }
}
