package com.group35.nutripath.api.edamam

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface EdamamDAO {
    @GET("search")
    suspend fun searchRecipes(
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
     //   @Query("cuisineType") cuisineType: String? = null,
    //    @Query("mealType") mealType: String? = null,
     //   @Query("dishType") dishType: String? = null,
        @Query("from") from: Int,
        @Query("to") to: Int

        ): RecipeResponse
}