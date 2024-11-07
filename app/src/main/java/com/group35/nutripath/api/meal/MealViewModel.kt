package com.group35.nutripath.api.meal

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import retrofit2.Call
import retrofit2.Callback


class MealViewModel : ViewModel(){
    private val repository = MealRepository()
    val mealRecommendationList = MutableLiveData<List<Meal>>()
    val mealInformation = MutableLiveData<List<MealInformation>>()

    fun getMealByIngredient(ingredient: String) {
        repository.getMealByIngredient(ingredient).enqueue(object : Callback<ReturnedMeal> {
            override fun onResponse(
                call: Call<ReturnedMeal>,
                response: retrofit2.Response<ReturnedMeal>
            ) {
                if (response.isSuccessful) {
                    Log.d("MealViewModel", "getMealByIngredient Response Successful.")
                    mealRecommendationList.value = response.body()?.meals
                }
            }

            override fun onFailure(call: Call<ReturnedMeal>, t: Throwable) {
                Log.d("MealViewModel", "getMealByIngredient Response Failed.")
            }
        })
    }

    fun getMealInformation(meal: String) {
        repository.getMealInformation(meal).enqueue(object : Callback<ReturnedMealInformation> {
            override fun onResponse(
                call: Call<ReturnedMealInformation>,
                response: retrofit2.Response<ReturnedMealInformation>
            ) {
                if (response.isSuccessful) {
                    mealInformation.value = response.body()?.mealInformation
                }
            }
            override fun onFailure(call: Call<ReturnedMealInformation>, t: Throwable) {
                // Handle error to do
            }
        })
    }


}