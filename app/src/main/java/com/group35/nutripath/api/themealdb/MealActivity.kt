package com.group35.nutripath.api.themealdb

import MealAdapter
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.group35.nutripath.R
import com.group35.nutripath.databinding.ActivityMealBinding

class MealActivity: AppCompatActivity() {
    private lateinit var viewModel: MealViewModel
    private lateinit var ingredientInput: EditText
    private lateinit var ingredientInputTextView: TextView
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

        // Binding views
        binding = ActivityMealBinding.inflate(layoutInflater)
        ingredientInput = binding.ingredientInput
        ingredientInputTextView = binding.ingredientInputTextView

        viewModel = ViewModelProvider(this)[MealViewModel::class.java]

        // Fetch meals based on user-selected ingredients
        val selectedIngredient = viewModel.getMealByIngredient(ingredientInput.text.toString())
        ingredientInputTextView.text = selectedIngredient.toString() // preview selection

        // Recycler view to display the meal list
        val recyclerView = findViewById<RecyclerView>(R.id.mealRecycleView)
        mealAdapter = MealAdapter()
        recyclerView.adapter = mealAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.mealRecommendationList.observe(this) { updatedMeals ->
            // Update RecyclerView adapter with the new meal list
            mealAdapter.submitList(updatedMeals)
        }
    }
}