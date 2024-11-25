package com.group35.nutripath.api.openfoodfacts

// Represents the response from the Open Food Facts API
data class ProductResponse(
    val code: String,
    val status: Int,
    val product: Product?
)

// Represents the detailed product information
data class Product(
    val product_name: String?,

    val generic_name: String?,
    val brands: String?,
    val categories: String?,
    val labels: String?,
    val packaging: String?,
    val quantity: String?,
    val stores: String?,
    val countries_tags: List<String>?,

    val ingredients_text: String?,
    val allergens: String?,
    val traces: String?,

    val serving_size: String?,

    val additives_tags: List<String>?,


    val image_url: String?,
    val image_small_url: String?,

    // nutrition grade
    val nutrition_grade_fr: String?,

    // nutri score
    val nutriscore_score: Int?,
    val nutriscore_grade: String?,

    // nova group
    val nova_group: Int?,

    // eco score
    val ecoscore_score: Int?,
    val ecoscore_grade: String?,

    // nutriments
    val nutriments: Nutriments?,

    )

// Represents the nutritional information of the product
data class Nutriments(
    val energy_kcal: Double?,
    val fat: Double?,
    val saturated_fat: Double?,
    val carbohydrates: Double?,
    val sugars: Double?,
    val fiber: Double?,
    val proteins: Double?,
    val salt: Double?,
    val sodium: Double?
)


