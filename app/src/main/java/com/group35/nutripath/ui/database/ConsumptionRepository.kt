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

    suspend fun getMonthlySpending(yearMonth: String): Double {
        return consumptionDao.getTotalSpendingForMonth(yearMonth)
    }
    suspend fun getTotalCaloriesForDay(date: String): Double {
        return consumptionDao.getTotalCaloriesForDay(date)
    }
}
