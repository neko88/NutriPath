package com.group35.nutripath.api.openfoodfacts

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.group35.nutripath.R
import java.util.Locale

class OpenFoodFactsActivity : AppCompatActivity() {


    private lateinit var barcodeInputEditText: EditText
    private lateinit var fetchProductButton: Button
    private lateinit var productImageView: ImageView
    private lateinit var productNameTextView: TextView
    private lateinit var ingredientsTextView: TextView

    // Nutrition Scores Fields Initialization
    private lateinit var nutriscoreScoreTextView: TextView
    private lateinit var nutriscoreGradeTextView: TextView
    private lateinit var novascoreScoreTextView: TextView
    private lateinit var novascoreGradeTextView: TextView
    private lateinit var ecoscoreScoreTextView: TextView
    private lateinit var ecoscoreGradeTextView: TextView

    // Nutrition Facts Fields Initialization
    private lateinit var servingSizeTextView: TextView
    private lateinit var caloriesTextView: TextView
    private lateinit var totalFatTextView: TextView
    private lateinit var saturatedFatTextView: TextView
    private lateinit var carbohydratesTextView: TextView
    private lateinit var sugarsTextView: TextView
    private lateinit var proteinTextView: TextView
    private lateinit var saltTextView: TextView

    private lateinit var foodViewModel: OpenFoodFactsViewModel

    // Cube Grid Fields Initialization
    private lateinit var genericNameValue: TextView
    private lateinit var brandValue: TextView
    private lateinit var categoryValue: TextView
    private lateinit var labelValue: TextView
    private lateinit var packagingValue: TextView
    private lateinit var quantityValue: TextView
    private lateinit var storesValue: TextView
    private lateinit var countriesValue: TextView

    private lateinit var nutriscoreImageView: ImageView
    private lateinit var novascoreImageView: ImageView
    private lateinit var ecoscoreImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_openfoodfacts_foodinfo)

        barcodeInputEditText = findViewById(R.id.barcodeInputEditText)
        fetchProductButton = findViewById(R.id.fetchProductButton)
        productImageView = findViewById(R.id.productImageView)
        productNameTextView = findViewById(R.id.productNameTextView)
        ingredientsTextView = findViewById(R.id.ingredients_desc_text_view)

        // Nutrition Scores Fields Initialization
        nutriscoreScoreTextView = findViewById(R.id.nutriscore_score_textview)
    //    nutriscoreGradeTextView = findViewById(R.id.nutriscore_grade_textview)
        novascoreScoreTextView = findViewById(R.id.novascore_score_textview)
     //   novascoreGradeTextView = findViewById(R.id.novascore_grade_textview)
        ecoscoreScoreTextView = findViewById(R.id.ecoscore_score_textview)
      //  ecoscoreGradeTextView = findViewById(R.id.ecoscore_grade_textview)

        // Nutrition Facts Fields Initialization
        servingSizeTextView = findViewById(R.id.servingSizeTextView)
        caloriesTextView = findViewById(R.id.caloriesTextView)
        totalFatTextView = findViewById(R.id.totalFatTextView)
        saturatedFatTextView = findViewById(R.id.saturatedFatTextView)
        carbohydratesTextView = findViewById(R.id.carbohydratesTextView)
        sugarsTextView = findViewById(R.id.sugarsTextView)
        proteinTextView = findViewById(R.id.proteinTextView)
        saltTextView = findViewById(R.id.saltTextView)

        genericNameValue = findViewById(R.id.genericNameValue)
        brandValue = findViewById(R.id.brandValue)
        categoryValue = findViewById(R.id.categoryValue)
        labelValue = findViewById(R.id.labelValue)
        packagingValue = findViewById(R.id.packagingValue)
        quantityValue = findViewById(R.id.quantityValue)
        storesValue = findViewById(R.id.storesValue)
        countriesValue = findViewById(R.id.countriesValue)

        // Initialize the ViewModel
        foodViewModel = ViewModelProvider(this).get(OpenFoodFactsViewModel::class.java)

        // Set button click listener to fetch product info
        fetchProductButton.setOnClickListener {
            val barcode = barcodeInputEditText.text.toString()
            if (barcode.isNotBlank()) {
                foodViewModel.findFoodByBarcode(barcode)
            }
        }

        // Check if barcode was passed via intent and fetch product info
        val barcode = intent.getStringExtra("barcode")
        if (!barcode.isNullOrEmpty()) {
            barcodeInputEditText.setText(barcode)
            foodViewModel.findFoodByBarcode(barcode)
        }

        // Observe productLiveData from ViewModel
        foodViewModel.productLiveData.observe(this, Observer { product ->
            if (product != null) {
                productNameTextView.text = "${product.product?.product_name ?: "N/A"}"

                // Load product image using Glide
                product.product?.image_url?.let { imageUrl ->
                    Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_nutripath_logo)
                        .error(R.drawable.ic_nutripath_logo)
                        .into(productImageView)
                }

                ingredientsTextView.text = product.product?.ingredients_text ?: "M/A"

                // Set Nutrition Facts Table
                val nutriments = product.product?.nutriments
                servingSizeTextView.text = product.product?.serving_size ?: "N/A"
                caloriesTextView.text = nutriments?.energy_kcal?.toString() ?: "N/A"
                totalFatTextView.text = nutriments?.fat?.toString() + " g" ?: "N/A"
                saturatedFatTextView.text = nutriments?.saturated_fat?.toString() + " g" ?: "N/A"
                carbohydratesTextView.text = nutriments?.carbohydrates?.toString() + " g" ?: "N/A"
                sugarsTextView.text = nutriments?.sugars?.toString() + " g" ?: "N/A"
                proteinTextView.text = nutriments?.proteins?.toString() + " g" ?: "N/A"
                saltTextView.text = nutriments?.salt?.toString() + " g" ?: "N/A"

                genericNameValue.text = product.product?.generic_name ?: "N/A"
                brandValue.text = product.product?.brands ?: "N/A"
                categoryValue.text = product.product?.categories ?: "N/A"
                labelValue.text = product.product?.labels ?: "N/A"
                packagingValue.text = product.product?.packaging ?: "N/A"
                quantityValue.text = product.product?.quantity ?: "N/A"
                storesValue.text = product.product?.stores ?: "N/A"
                countriesValue.text = product.product?.countries_tags?.joinToString(", ") ?: "N/A"

                nutriscoreImageView = findViewById(R.id.nutriscore_imageview)
                novascoreImageView = findViewById(R.id.novascore_imageview)
                ecoscoreImageView = findViewById(R.id.ecoscore_imageview)

                // Set Nutrition Scores and Grades
                val nutriScore = product.product?.nutriscore_score
                val nutriGrade = product.product?.nutrition_grade_fr
                nutriscoreScoreTextView.text = nutriScore?.toString() ?: "N/A"
             //   nutriscoreGradeTextView.text = nutriGrade?.toUpperCase() ?: "N/A"

                val novaScore = product.product?.nova_group
                novascoreScoreTextView.text = novaScore?.toString() ?: "N/A"
             //   novascoreGradeTextView.text = if (novaScore != null) "Group $novaScore" else "N/A"

                val ecoScore = product.product?.ecoscore_score
                val ecoGrade = product.product?.ecoscore_grade
                ecoscoreScoreTextView.text = ecoScore?.toString() ?: "N/A"

             //   ecoscoreGradeTextView.text = ecoGrade?.uppercase(Locale.getDefault()) ?: "N/A"
                // Set NutriScore Image
                when (nutriGrade?.uppercase(Locale.ROOT)) {
                    "A" -> nutriscoreImageView.setImageResource(R.drawable.nutriscore_a)
                    "B" -> nutriscoreImageView.setImageResource(R.drawable.nutriscore_b)
                    "C" -> nutriscoreImageView.setImageResource(R.drawable.nutriscore_c)
                    "D" -> nutriscoreImageView.setImageResource(R.drawable.nutriscore_d)
                    "E" -> nutriscoreImageView.setImageResource(R.drawable.nutriscore_e)
                    else -> nutriscoreImageView.setImageResource(R.drawable.ic_nutripath_logo)
                }

                // Set NovaScore Image
                when (novaScore) {
                    1 -> novascoreImageView.setImageResource(R.drawable.nova_1)
                    2 -> novascoreImageView.setImageResource(R.drawable.nova_2)
                    3 -> novascoreImageView.setImageResource(R.drawable.nova_3)
                    4 -> novascoreImageView.setImageResource(R.drawable.nova_4)
                    else -> novascoreImageView.setImageResource(R.drawable.ic_nutripath_logo)
                }

                // Set EcoScore Image
                when (ecoGrade?.uppercase(Locale.ROOT)) {
                    "A" -> ecoscoreImageView.setImageResource(R.drawable.nutriscore_a)
                    "B" -> ecoscoreImageView.setImageResource(R.drawable.nutriscore_b)
                    "C" -> ecoscoreImageView.setImageResource(R.drawable.nutriscore_c)
                    "D" -> ecoscoreImageView.setImageResource(R.drawable.nutriscore_d)
                    "E" -> ecoscoreImageView.setImageResource(R.drawable.nutriscore_e)
                    else -> ecoscoreImageView.setImageResource(R.drawable.ic_nutripath_logo)
                }

            } else {
                // Set default values if the product is null
                productNameTextView.text = "Product not found or error occurred."
                servingSizeTextView.text = "N/A"
                caloriesTextView.text = "N/A"
                totalFatTextView.text = "N/A"
                saturatedFatTextView.text = "N/A"
                carbohydratesTextView.text = "N/A"
                sugarsTextView.text = "N/A"
                proteinTextView.text = "N/A"
                saltTextView.text = "N/A"

                genericNameValue.text = "N/A"
                brandValue.text = "N/A"
                categoryValue.text = "N/A"
                labelValue.text = "N/A"
                packagingValue.text = "N/A"
                quantityValue.text = "N/A"
                storesValue.text = "N/A"
                countriesValue.text = "N/A"

                // Set default values for Nutrition Scores
                nutriscoreScoreTextView.text = "N/A"
                nutriscoreGradeTextView.text = "N/A"
                novascoreScoreTextView.text = "N/A"
                novascoreGradeTextView.text = "N/A"
                ecoscoreScoreTextView.text = "N/A"
                ecoscoreGradeTextView.text = "N/A"

                // Set default images for scores
                nutriscoreImageView.setImageResource(R.drawable.ic_nutripath_logo)
                novascoreImageView.setImageResource(R.drawable.ic_nutripath_logo)
                ecoscoreImageView.setImageResource(R.drawable.ic_nutripath_logo)
            }
        })

    }
}
