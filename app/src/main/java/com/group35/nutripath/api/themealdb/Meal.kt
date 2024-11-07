package com.group35.nutripath.api.themealdb

// DATA CLASSES: Storing / organizing meal information and data received.

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
)
data class ResponseMeal(
    val meals: List<Meal>
)

data class MealInformation(
    val idMeal: String,
    val strMeal: String,
    val strInstructions: String,
    val strMealThumb: String,
    val strIngredient1: String?,
    val strIngredient2: String?,
    // Add more ingredients up to 20, if needed
    val strMeasure1: String?,
    val strMeasure2: String?
)
data class ResponseMealInformation(
    val mealInformation: List<MealInformation>
)


