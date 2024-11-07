package com.group35.nutripath.api.meal
import retrofit2.Call

// REPOSITORY: Handle actions for the meal calls
// Call the request to the API
class TheMealDBRepository {
    private val apiCall = RetroFitCaller.api

    fun getMealByIngredient(ingredient: String): Call<ResponseMeal> {
        return apiCall.getMealsByIngredient(ingredient)
    }

    fun getMealInformation(mealId: String): Call<ResponseMealInformation> {
        return apiCall.getMealInformation(mealId)
    }
}