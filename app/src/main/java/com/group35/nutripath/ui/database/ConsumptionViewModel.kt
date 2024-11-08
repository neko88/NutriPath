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

    fun getTotalSpendingForMonth(yearMonth: String): LiveData<Double> {
        val spendingLiveData = MutableLiveData<Double>()
        viewModelScope.launch {
            val totalSpending = repository.getMonthlySpending(yearMonth)
            spendingLiveData.postValue(totalSpending)
        }
        return spendingLiveData
    }


    fun getDailyCalories(date: String): LiveData<Double> {
        val calories = MutableLiveData<Double>()
        viewModelScope.launch {
            val totalCals = repository.getTotalCaloriesForDay(date)
            calories.postValue(totalCals)
        }
        return calories
    }
}

class ConsumptionViewModelFactory (private val repository: ConsumptionRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
        if(modelClass.isAssignableFrom(ConsumptionViewModel::class.java))
            return ConsumptionViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
