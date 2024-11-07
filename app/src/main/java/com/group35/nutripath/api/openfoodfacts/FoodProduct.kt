package com.group35.nutripath.api.openfoodfacts


data class FoodItem(
    val name: String?,
    val ingredients: String?
)

data class ResponseFoodProducts(
    val products: List<FoodItem>
)

data class ResponseBarcodeFoodQuery (
    val barcode: String,
    val products: List<ResponseFoodProducts>,
    val status: Int,
    val status_verbose: String
)

