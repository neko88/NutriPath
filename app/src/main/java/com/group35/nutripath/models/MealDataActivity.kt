package com.group35.nutripath.models
import MealAdapter
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.group35.nutripath.R
import com.group35.nutripath.api.meal.MealViewModel
import com.opencsv.CSVReader
import java.io.BufferedReader
import java.io.InputStreamReader
import com.group35.nutripath.databinding.ActivityMealBinding

data class FoodItem(
    val id: String,
    val productName: String,
    val categories: String,
    val ingredients: String,
    val nutritionGrade: String,
    val price: Float
)


class MealDataActivity : AppCompatActivity(){
    private lateinit var viewModel: MealViewModel
    private lateinit var ingredientInput: EditText
    private lateinit var ingredientInputTextView: TextView
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealAdapter: MealAdapter
    private val _recommendedFoods = MutableLiveData<List<FoodItem>>()
    val recommendedFoods: LiveData<List<FoodItem>> get() = _recommendedFoods
    private lateinit var foodData: List<FoodItem>

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_meal)
    }

    private fun loadFoodData(): List<FoodItem> {
        val foodData = mutableListOf<FoodItem>()
        val inputStream = assets.open("openfoodfacts_data.tsv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.readLine() // Skip header

        reader.forEachLine { line ->
            val tokens = line.split("\t")
            if (tokens.size >= 6) { // Check based on expected column count
                val foodItem = FoodItem(
                    id = tokens[0],
                    productName = tokens[1],
                    categories = tokens[2],
                    ingredients = tokens[3],
                    nutritionGrade = tokens[4],
                    price = tokens[5].toFloatOrNull() ?: 0.0f
                )
                foodData.add(foodItem)
            }
        }

        reader.close()
        return foodData
    }

    fun recommendFoods(selectedIngredient: String, budget: Float? = null) {
        val recommendations = foodData.filter { item ->
            item.ingredients.contains(selectedIngredient, ignoreCase = true) &&
                    (budget == null || item.price <= budget)
        }
        _recommendedFoods.value = recommendations
    }
}