package com.group35.nutripath.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FoodViewModel : ViewModel() {
    private val _recommendedFoods = MutableLiveData<List<FoodItem>>()
    val recommendedFoods: LiveData<List<FoodItem>> get() = _recommendedFoods

    private lateinit var foodData: List<FoodItem>

    init {
        // Load the .tsv data
        foodData = loadFoodData()
    }

}
