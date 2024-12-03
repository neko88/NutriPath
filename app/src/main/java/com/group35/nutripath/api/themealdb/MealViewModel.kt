package com.group35.nutripath.api.themealdb

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.google.gson.JsonObject

// For the recipe detail card
class MealViewModel : ViewModel() {
    private val repository = TheMealDBRepository()
    val mealRecommendationList = MutableLiveData<List<Meal>>()
    val mealArea = MutableLiveData<String>()
    val mealCategory = MutableLiveData<String>()
    val mealMainIngredient = MutableLiveData<String>()
    val mealImageUrl = MutableLiveData<String>()

    private fun parseMeal(mealJson: JsonObject): Meal {
        val ingredients = mutableMapOf<String, String>()

        // extract ingredients and measurements
        for (i in 1..20) {
            val ingredient = mealJson.get("strIngredient$i")?.takeIf { it.isJsonPrimitive }?.asString
            val measure = mealJson.get("strMeasure$i")?.takeIf { it.isJsonPrimitive }?.asString

            if (!ingredient.isNullOrBlank()) {
                ingredients[ingredient] = measure ?: ""
            }
        }

        // parse other meal details
        return Meal(
            idMeal = mealJson.get("idMeal")?.asString ?: "",
            strMeal = mealJson.get("strMeal")?.asString ?: "",
            strMealThumb = mealJson.get("strMealThumb")?.asString ?: "",
            strInstructions = mealJson.get("strInstructions")?.asString ?: "",
            ingredients = ingredients
        )
    }

    // call meal details for the instructions in the recipe
    fun getMealDetails(mealId: String) {
        viewModelScope.launch {
            val result = repository.getMealInformation(mealId)
            result.body()?.getAsJsonArray("meals")?.firstOrNull()?.asJsonObject?.let { mealJson ->
                val imageUrl = mealJson.get("strMealThumb")?.asString ?: ""
                val area = mealJson.get("strArea")?.asString ?: "Unknown"
                val category = mealJson.get("strCategory")?.asString ?: "Unknown"
                val mainIngredient = mealJson.get("strIngredient1")?.asString ?: "Unknown"

                // post the data for using them for the UI
                mealImageUrl.postValue(imageUrl) // Update with the image URL
                mealArea.value = area
                mealCategory.value = category
                mealMainIngredient.value = mainIngredient
            }
        }
    }

    // Fetch meals and parse them
    fun getMealByIngredient(ingredient: String) {
        viewModelScope.launch {
            val response = repository.getMealByIngredient(ingredient)
            val mealsJsonArray = response.body()?.getAsJsonArray("meals") ?: return@launch
            val meals = mealsJsonArray.map { mealJson ->
                parseMeal(mealJson.asJsonObject)
            }
          //  Log.d("MealViewModel", "Saerching meal by term: ${ingredient}...")
          //  Log.d("MealViewModel", "Received: ${meals}...")
            mealRecommendationList.postValue(meals)
        }
    }
}
