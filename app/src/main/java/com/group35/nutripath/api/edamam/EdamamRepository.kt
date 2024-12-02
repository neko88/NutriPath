package com.group35.nutripath.api.edamam

import android.util.Log
import android.view.WindowInsetsAnimation

import retrofit2.Call
import retrofit2.Response


class EdamamRepository {

    private val api = EdamamApi.RetrofitClient.instance.create(EdamamDAO::class.java)

    suspend fun searchRecipes(
        query: String,
        appId: String,
        appKey: String,
     //   cuisineType: String? = null,
      //  mealType: String? = null,
     //   dishType: String? = null,
        from: Int = 0,
        to: Int = 10
    ): Result<List<Recipe>> {
        return try {
          //  val recipeResponse = api.searchRecipes(query, appId, appKey, cuisineType, mealType, dishType, from, to)
            val recipeResponse = api.searchRecipes(query, appId, appKey, from, to)
            val recipes = recipeResponse.hits?.mapNotNull { it.recipe } ?: emptyList()
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e) // Handle errors like network issues
        }
    }


}


