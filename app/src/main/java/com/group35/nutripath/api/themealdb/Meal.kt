package com.group35.nutripath.api.themealdb
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
// DATA CLASSES: Storing / organizing meal information and data received.

@Parcelize
data class Meal(
    val idMeal: String = "",
    val strMeal: String = "",
    val strMealThumb: String = "",
    val strInstructions: String = "",
    val ingredients: Map<String, String> = emptyMap()
) : Parcelable

data class ResponseMeal(
    val meals: List<Meal>
)

@Parcelize
data class MealInformation(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strInstructions: String,
    val ingredients: Map<String, String>,
    val measurements: Map<String, String>
) : Parcelable

val emptyMeal = Meal(
    idMeal = "0",
    strMeal = "",
    strMealThumb = "",
    strInstructions = "",
    ingredients = emptyMap() // Empty ingredients
)

data class ResponseMealInformation(
    val mealInformation: List<MealInformation>
)


