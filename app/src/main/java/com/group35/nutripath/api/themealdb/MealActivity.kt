package com.group35.nutripath.api.themealdb

import MealAdapter
import android.os.Bundle
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.group35.nutripath.R
import com.group35.nutripath.databinding.ActivityMealBinding
import kotlinx.coroutines.launch

class MealActivity: AppCompatActivity() {
    private lateinit var viewModel: MealViewModel
    private lateinit var searchFood: SearchView
    private lateinit var foodTextView: TextView
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

        binding = ActivityMealBinding.inflate(layoutInflater)
        searchFood = binding.mealSearchBar

        viewModel = ViewModelProvider(this)[MealViewModel::class.java]

        lifecycleScope.launch {
           viewModel.getMealByIngredient(searchFood.toString())
        }

        val recyclerView = findViewById<RecyclerView>(R.id.mealRecycleView)
        mealAdapter = MealAdapter()
        recyclerView.adapter = mealAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns

        viewModel.mealRecommendationList.observe(this) { updatedMeals ->
            mealAdapter.submitList(updatedMeals)
        }
    }
}