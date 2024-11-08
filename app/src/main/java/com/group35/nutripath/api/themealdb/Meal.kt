package com.group35.nutripath.api.themealdb

// DATA CLASSES: Storing / organizing meal information and data received.

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strInstructions: String,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
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
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strMeasure1: String?,
    val strMeasure2: String?
)

data class ResponseMealInformation(
    val mealInformation: List<MealInformation>
)


