package com.group35.nutripath.api.meal

// DATA CLASSES: Storing / organizing meal information and data received.
data class ReturnedMeal(
    val meals: List<Meal>
)

data class Meal(
    val iD: Int,
    val name: String,
    val image: String
)

data class ReturnedMealInformation(
    val mealInformation: List<MealInformation>
)

data class MealInformation(
    val iD: Int,
    val name: String,
    val image: String,
    val instructions: ArrayList<String>,
    val ingredients: HashMap<String,String>
)


