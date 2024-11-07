package com.group35.nutripath.api.meal

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback


class MealViewModel : ViewModel(){
    private val repository = TheMealDBRepository()
    val mealRecommendationList = MutableLiveData<List<Meal>>()
    val mealInformationList = MutableLiveData<List<MealInformation>>()

    fun getMealByIngredient(ingredient: String) {
        repository.getMealByIngredient(ingredient).enqueue(object : Callback<ResponseMeal> {
            override fun onResponse(call: Call<ResponseMeal>, response: retrofit2.Response<ResponseMeal>){
                if (response.isSuccessful) {
                    mealRecommendationList.value = response.body()?.meals
                    mealRecommendationList.value?.let {
                        Log.d("MealViewModel", "getMealByIngredient Response Successful : ${mealRecommendationList.value}")
                    }
                      //  Log.d("Meal", "Meal Name: ${it.get()}")
                     //    Log.d("Meal", "Category: ${it.strCategory}")
                     //   Log.d("Meal", "Instructions: ${it.strInstructions}")
                }
            }
            override fun onFailure(call: Call<ResponseMeal>, t: Throwable) {
                Log.d("MealViewModel", "getMealByIngredient Response Failed : ${t}")
            }
        })
    }

    fun getMealInformation(meal: String) {
        repository.getMealInformation(meal).enqueue(object : Callback<ResponseMealInformation> {
            override fun onResponse(call: Call<ResponseMealInformation>, response: retrofit2.Response<ResponseMealInformation>) {
                if (response.isSuccessful) {
                    mealInformationList.value = response.body()?.mealInformation
                    mealInformationList.value?.let {
                        Log.d("MealViewModel", "getMealByIngredient Response Successful : ${mealInformationList.value}")
                    }
                }
            }
            override fun onFailure(call: Call<ResponseMealInformation>, t: Throwable) {
                Log.d("MealViewModel", "getMealByIngredient Response Failed : ${t}")
            }
        })
    }


}