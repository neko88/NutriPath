package com.group35.nutripath.api.edamam

data class RecipeResponse(
    val hits: List<Hit>
)

data class Hit(
    val recipe: Recipe
)

data class Recipe(
    val label: String = "",
    val image: String = "",
    val source: String = "",
    val url: String = "",
    val calories: Double = 0.0,
    val yield: Int = 0,
    val ingredientLines: List<String> = emptyList(),
    val dietLabels: List<String> = emptyList(),
    val healthLabels: List<String> = emptyList(),
    val cuisineType: List<String> = emptyList(),
    val mealType: List<String> = emptyList(),
    val dishType: List<String> = emptyList()
)

data class Ingredient(
    val text: String,
    val weight: Double
)
