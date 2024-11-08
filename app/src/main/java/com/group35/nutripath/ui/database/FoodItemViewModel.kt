package com.group35.nutripath.ui.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException


// Adapted from CommentViewModel
class FoodItemViewModel(private val repository: FoodItemRepository) : ViewModel() {
    val allFoodItemLiveData: LiveData<List<FoodItem>> = repository.allFoodItems.asLiveData()
    private val _item = MutableLiveData<FoodItem?>()


    fun insert(foodItem: FoodItem) {
        viewModelScope.launch {
            repository.insert(foodItem)
        }
    }

    fun deleteAll(id: Long){
        viewModelScope.launch {
            repository.deleteAll()
        }

    }

}

class FoodItemViewModelFactory (private val repository: FoodItemRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{ //create() creates a new instance of the modelClass, which is CommentViewModel in this case.
        if(modelClass.isAssignableFrom(FoodItemViewModel::class.java))
            return FoodItemViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
