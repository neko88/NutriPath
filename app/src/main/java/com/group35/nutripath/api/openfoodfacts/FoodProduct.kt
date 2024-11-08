package com.group35.nutripath.api.openfoodfacts

data class ProductResponse(
    val code: String,
    val status: Int,
    val product: Product?
)

data class Product(
    val product_name: String?,
    val ingredients_text: String?,
    val nutriments: Nutriments?
)

data class Nutriments(
    val energy_kcal: Double?,
    val fat: Double?,
    val sugars: Double?,
    val proteins: Double?
)
