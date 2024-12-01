package com.group35.nutripath.api.edamam

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.group35.nutripath.databinding.ActivityEdamamRecipeBinding
import com.group35.nutripath.homemenu.dataobject.TopDataObject

class EdamamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEdamamRecipeBinding
    private lateinit var recipeAdapter: EdamamAdapter
    private val repository = EdamamRepository()
    private val appId = "d45fcd57" // Replace with your actual App ID
    private val appKey = "2f4ad82db15fbca9481a538196db68dd" // Replace with your actual App Key

    private var lastRequestTime: Long = 0
    private val REQUEST_INTERVAL = 60000 // 1 minute in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEdamamRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvRecipes.layoutManager = LinearLayoutManager(this)
        recipeAdapter = EdamamAdapter(listOf()) { recipe ->
            showRecipeDetails(recipe)
        }
        binding.rvRecipes.adapter = recipeAdapter

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearchQuery.text.toString().trim()
            if (query.isNotEmpty()) {
                fetchRecipes(query)
            } else {
                Toast.makeText(this, "Please enter a query", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchRecipes(query: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRequestTime < REQUEST_INTERVAL) {
            val waitTime = (REQUEST_INTERVAL - (currentTime - lastRequestTime)) / 1000
            Toast.makeText(this, "Please wait $waitTime seconds before searching again", Toast.LENGTH_SHORT).show()
            return
        }

        lastRequestTime = currentTime
        binding.btnSearch.isEnabled = false
        binding.etSearchQuery.isEnabled = false

        val from = 0
        val to = 1

        repository.searchRecipes(query, appId, appKey, from, to, { recipes ->
            runOnUiThread {
                recipeAdapter.updateRecipes(recipes)
                // Re-enable the search button after the request completes
                binding.btnSearch.isEnabled = true
                binding.etSearchQuery.isEnabled = true
            }
        }, { error ->
            runOnUiThread {
                // Re-enable the search button even if the request fails
                binding.btnSearch.isEnabled = true
                binding.etSearchQuery.isEnabled = true
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    @SuppressLint("SetTextI18n")
    private fun showRecipeDetails(recipe: Recipe) {
        val details = """
            Name: ${recipe.label ?: "N/A"}
            Source: ${recipe.source ?: "N/A"}
            URL: ${recipe.url ?: "N/A"}
            Calories: ${recipe.calories ?: 0.0} kcal
            Servings: ${recipe.yield ?: 0}
            Ingredients:
            ${recipe.ingredientLines?.joinToString("\n") { "- $it" } ?: "N/A"}
            Diet Labels: ${recipe.dietLabels?.joinToString(", ") ?: "N/A"}
            Health Labels: ${recipe.healthLabels?.joinToString(", ") ?: "N/A"}
            Cuisine Type: ${recipe.cuisineType?.joinToString(", ") ?: "N/A"}
            Meal Type: ${recipe.mealType?.joinToString(", ") ?: "N/A"}
            Dish Type: ${recipe.dishType?.joinToString(", ") ?: "N/A"}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Recipe Details")
            .setMessage(details)
            .setPositiveButton("OK", null)
            .setNegativeButton("View Full Recipe") { _, _ ->
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.url ?: ""))
                startActivity(browserIntent)
            }
            .show()
    }
}
