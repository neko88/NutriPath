package com.group35.nutripath.api.meal
import retrofit2.Call

// REPOSITORY: Handle actions for the meal calls
// Call the request to the API
class MealRepository {
    private val api = RetroFitCaller.api

    fun getMealByIngredient(ingredient: String): Call<ReturnedMeal> {
        return api.getMealsByIngredient(ingredient)
    }

    fun getMealInformation(mealId: String): Call<ReturnedMealInformation> {
        return api.getMealInformation(mealId)
    }

    fun getMealsByCategory(mealId: String): Call<ReturnedMeal> {
        return api.getMealsByCategory(mealId)
    }

}