package com.group35.nutripath.api.themealdb
import retrofit2.Call

// REPOSITORY: Handle actions for the meal calls
// Call the request to the API
class TheMealDBRepository {
    private val apiCall = RetroFitCallerTheMealDB.api

    suspend fun getMealByIngredient(ingredient: String): ResponseMeal{
        return apiCall.getMealsByIngredient(ingredient)
    }

    suspend fun getMealInformation(mealId: String): ResponseMealInformation {
        return apiCall.getMealInformation(mealId)
    }
}