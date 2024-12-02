package com.group35.nutripath.api.edamam

data class RecipeResponse(
    val hits: List<Hit>? = null // Ensure this matches the API response
)

data class Hit(
    val recipe: Recipe? = null
)


data class Recipe(
    val uri: String = "", // Unique identifier for the recipe
    val label: String = "", // Recipe title
    val image: String = "", // Image URL
    val source: String = "", // Source site identifier
    val url: String = "", // Original recipe URL
    val shareAs: String = "", // URL to share the recipe
    val yield: Int = 0, // Number of servings
    val calories: Double = 0.0, // Total energy in kcal
    val totalWeight: Double = 0.0, // Total weight in grams
    val totalTime: Double = 0.0, // Total time in minutes
    val ingredients: List<Ingredient> = emptyList(), // List of ingredients
    val totalNutrients: Map<String, NutrientInfo> = emptyMap(), // Nutrients for the entire recipe
    val totalDaily: Map<String, NutrientInfo> = emptyMap(), // % daily value for the entire recipe
    val dietLabels: List<String> = emptyList(), // Diet labels (e.g., "low-carb")
    val healthLabels: List<String> = emptyList(), // Health labels (e.g., "gluten-free")
    val cautions: List<String> = emptyList(), // Cautionary labels (e.g., "soy")
    val cuisineType: List<String> = emptyList(), // Cuisine type (e.g., "Italian")
    val mealType: List<String> = emptyList(), // Meal type (e.g., "dinner")
    val dishType: List<String> = emptyList(), // Dish type (e.g., "main course")
    val digest: List<DigestEntry> = emptyList(), // Nutrient breakdown
)

data class Ingredient(
    val text: String = "", // Original ingredient text
    val quantity: Double = 0.0, // Quantity of the ingredient
    val measure: String? = null, // Measurement unit
    val food: String = "", // Food name
    val weight: Double = 0.0, // Weight in grams
    val foodCategory: String? = null, // Food category
    val foodId: String = "", // Food identifier
    val image: String? = null // Image URL of the ingredient
)

data class NutrientInfo(
    val label: String = "", // Nutrient name
    val quantity: Double = 0.0, // Quantity of the nutrient
    val unit: String = "" // Unit of the nutrient
)

data class DigestEntry(
    val label: String = "", // Nutrient label
    val tag: String = "", // Nutrient tag
    val schemaOrgTag: String? = null, // Schema.org tag
    val total: Double = 0.0, // Total amount
    val hasRDI: Boolean = false, // Has recommended daily intake
    val daily: Double = 0.0, // Daily percentage
    val unit: String = "", // Unit of measurement
    val sub: List<DigestEntry>? = null // Sub-nutrients
)

data class ImageSizes(
    val THUMBNAIL: Image? = null,
    val SMALL: Image? = null,
    val REGULAR: Image? = null
)

data class Image(
    val url: String = "", // URL of the image
    val width: Int = 0,   // Width of the image
    val height: Int = 0   // Height of the image
)