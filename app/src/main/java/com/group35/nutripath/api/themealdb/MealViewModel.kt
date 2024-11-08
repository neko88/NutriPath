package com.group35.nutripath.api.themealdb

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback


class MealViewModel : ViewModel(){
    private val repository = TheMealDBRepository()
    val mealRecommendationList = MutableLiveData<List<Meal>>()
    val mealInformationList = MutableLiveData<List<MealInformation>>()

    suspend fun getMealByIngredient(ingredient: String) {
        val result = repository.getMealByIngredient(ingredient)
        if (result == null) {
            Log.d("MealByIngredient", "No meals found for the ingredient: $ingredient")
        } else {
            Log.d("MealByIngredient", "Meals found: ${result}")
            mealRecommendationList.value = result.meals
        }
    }

    suspend fun getMealInformation(meal: String) {

    }

}