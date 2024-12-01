package com.group35.nutripath.api.edamam

import android.util.Log
import android.view.WindowInsetsAnimation

import retrofit2.Call
import retrofit2.Response


class EdamamRepository {
    private val api = EdamamApi.RetrofitClient.instance.create(EdamamDAO::class.java)

    fun searchRecipes(
        query: String,
        appId: String,
        appKey: String,
        from: Int,
        to: Int,
        onSuccess: (List<Recipe>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        api.searchRecipes(query, appId, appKey).enqueue(object : retrofit2.Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
                    val recipes = response.body()?.hits?.mapNotNull { it.recipe } ?: emptyList()
                    onSuccess(recipes)
                } else {
                    onFailure(Exception("Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

}


