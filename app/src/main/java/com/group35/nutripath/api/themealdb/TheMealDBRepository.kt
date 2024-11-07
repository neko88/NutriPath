package com.group35.nutripath.api.themealdb
import retrofit2.Call

// REPOSITORY: Handle actions for the meal calls
// Call the request to the API
class TheMealDBRepository {
    private val apiCall = RetroFitCallerTheMealDB.api

    fun getMealByIngredient(ingredient: String): Call<ResponseMeal> {
        return apiCall.getMealsByIngredient(ingredient)
    }

    fun getMealInformation(mealId: String): Call<ResponseMealInformation> {
        return apiCall.getMealInformation(mealId)
    }
}