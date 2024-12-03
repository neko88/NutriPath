package com.group35.nutripath.ui.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

// Adapted from CommentViewModel
class ConsumptionViewModel(private val repository: ConsumptionRepository) : ViewModel() {
    val allConsumptionLiveData: LiveData<List<Consumption>> = repository.allConsumption.asLiveData()
    private val _dailyProtein = MutableLiveData<Double>()
    val dailyProtein: LiveData<Double> get() = _dailyProtein

    private val _dailyFats = MutableLiveData<Double>()
    val dailyFats: LiveData<Double> get() = _dailyFats

    private val _dailyCalories = MutableLiveData<Double>()
    val dailyCalories: LiveData<Double> get() = _dailyCalories

    private val _dailyCarbs = MutableLiveData<Double>()
    val dailyCarbs: LiveData<Double> get() = _dailyCarbs

    private val _dailySugars = MutableLiveData<Double>()
    val dailySugars: LiveData<Double> get() = _dailySugars

    private val _monthlySpending = MutableLiveData<Double>()
    val monthlySpending: LiveData<Double> get() = _monthlySpending



    val allFoodItemLiveData: LiveData<List<FoodItem>> = repository.allFoodItems.asLiveData()
    fun insertFood(foodItem: FoodItem) {
        viewModelScope.launch {
            repository.insertFood(foodItem)
        }
    }

    fun deleteAllFood(){
        viewModelScope.launch {
            repository.deleteAllFood()
        }

    }
    fun insertConsumption(consumption: Consumption) {
        viewModelScope.launch {
            repository.insertConsumption(consumption)
        }
    }

    fun insertFoodWithConsumption(food: FoodItem, consumption: Consumption){
        viewModelScope.launch {
            repository.insertFoodWithConsumption(food, consumption)
        }
    }

    fun deleteAllConsumption(){
        viewModelScope.launch {
            repository.deleteAllConsumption()
        }

    }

    fun getTotalSpendingForMonth(start: Long, end: Long): LiveData<Double> {
        viewModelScope.launch {
            val totalSpent: Double = repository.getMonthlySpending(start, end) ?: 0.0
            _monthlySpending.postValue(totalSpent)
        }
        return monthlySpending
    }

    fun updateMacros(start: Long, end: Long){
        getDailyCalories(start, end)
        getDailyCarbs(start,end)
        getDailyProtein(start, end)
        getDailyFats(start, end)
        getDailySugars(start, end)
    }
    fun getDailyCalories(start: Long, end: Long): LiveData<Double> {
        viewModelScope.launch {
            val totalCals: Double = repository.getTotalCaloriesForDay(start, end) ?: 0.0
            _dailyCalories.postValue(totalCals)
        }

        return dailyCalories
    }

    fun getDailyFats(start: Long, end: Long): LiveData<Double> {
        viewModelScope.launch {
            val totalFats = repository.getTotalFatsForDay(start, end)?: 0.0
            _dailyFats.postValue(totalFats)
        }
        return dailyFats
    }
    fun getDailyCarbs(start: Long, end: Long): LiveData<Double> {
        viewModelScope.launch{
            val totalCarbs = repository.getTotalCarbsForDay(start, end)?: 0.0
            _dailyCarbs.postValue(totalCarbs)
        }
        return dailyCarbs
    }
    fun getDailyProtein(start: Long, end: Long): LiveData<Double> {
        viewModelScope.launch{
            val totalProtein = repository.getTotalProteinForDay(start, end) ?: 0.0
            _dailyProtein.postValue(totalProtein)
        }
        return dailyProtein
    }

    fun getDailySugars(start: Long, end: Long): LiveData<Double> {
        viewModelScope.launch{
            val totalSugars = repository.getTotalSugarsForDay(start, end)?: 0.0
            _dailySugars.postValue(totalSugars)
        }
        return dailySugars
    }


}

class ConsumptionViewModelFactory (private val repository: ConsumptionRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
        if(modelClass.isAssignableFrom(ConsumptionViewModel::class.java))
            return ConsumptionViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
