package com.group35.nutripath.api.themealdb
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response

// REPOSITORY: Handle actions for the meal calls
// Call the request to the API
class TheMealDBRepository {
    private val apiCall = RetroFitCallerTheMealDB.api

    suspend fun getMealByIngredient(ingredient: String): Response<JsonObject> {
        return apiCall.getMealsByIngredient(ingredient)
    }

    suspend fun getMealInformation(mealId: String): Response<JsonObject> {
        return apiCall.getMealInformation(mealId)
    }
}

