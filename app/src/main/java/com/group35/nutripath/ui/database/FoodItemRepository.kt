package com.group35.nutripath.ui.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FoodItemRepository(private val foodItemDao: FoodItemDao) {
    val allFoodItems: Flow<List<FoodItem>> = foodItemDao.getAllFood()

    fun insert(food: FoodItem){
        CoroutineScope(IO).launch{
            foodItemDao.insertFood(food)
        }
    }


    fun deleteAll(){
        CoroutineScope(IO).launch {
            foodItemDao.deleteAllFood()
        }
    }


}