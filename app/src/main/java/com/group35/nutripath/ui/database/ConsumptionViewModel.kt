package com.group35.nutripath.ui.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

// Adapted from CommentViewModel
class ConsumptionViewModel(private val repository: ConsumptionRepository) : ViewModel() {
    val allConsumptionLiveData: LiveData<List<Consumption>> = repository.allConsumption.asLiveData()


    fun insert(consumption: Consumption) {
        viewModelScope.launch {
            repository.insert(consumption)
        }
    }

    fun deleteAll(id: Long){
        viewModelScope.launch {
            repository.deleteAll()
        }

    }

    fun getTotalSpendingForMonth(start: Long, end: Long): LiveData<Double> {
        val spendingLiveData = MutableLiveData<Double>()
        viewModelScope.launch {
            val totalSpending = repository.getMonthlySpending(start, end)
            spendingLiveData.postValue(totalSpending)
        }
        return spendingLiveData
    }


    fun getDailyCalories(start: Long, end: Long): LiveData<Double> {
        val calories = MutableLiveData<Double>()
        viewModelScope.launch {
            val totalCals = repository.getTotalCaloriesForDay(start, end)
            calories.postValue(totalCals)
        }
        return calories
    }

    fun getDailyFats(start: Long, end: Long): LiveData<Double> {
        val fats = MutableLiveData<Double>()
        viewModelScope.launch {
            val totalFats = repository.getTotalFatsForDay(start, end)
            fats.postValue(totalFats)
        }
        return fats
    }
    fun getDailyCarbs(start: Long, end: Long): LiveData<Double> {
        val carbs = MutableLiveData<Double>()
        viewModelScope.launch{
            val totalCarbs = repository.getTotalCarbsForDay(start, end)
            carbs.postValue(totalCarbs)
        }
        return carbs
    }
    fun getDailyProtein(start: Long, end: Long): LiveData<Double> {
        val protein = MutableLiveData<Double>()
        viewModelScope.launch{
            val totalProtein = repository.getTotalProteinForDay(start, end)
            protein.postValue(totalProtein)
        }
        return protein
    }

    fun getDailySugars(start: Long, end: Long): LiveData<Double> {
        val sugars = MutableLiveData<Double>()
        viewModelScope.launch{
            val totalSugars = repository.getTotalSugarsForDay(start, end)
            sugars.postValue(totalSugars)
        }
        return sugars
    }
}

class ConsumptionViewModelFactory (private val repository: ConsumptionRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
        if(modelClass.isAssignableFrom(ConsumptionViewModel::class.java))
            return ConsumptionViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
