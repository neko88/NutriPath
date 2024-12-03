package com.group35.nutripath.api.themealdb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.group35.nutripath.R


class MealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)
        bindMealMenuFragment()
    }

    private fun bindMealMenuFragment() {
        if (supportFragmentManager.findFragmentById(R.id.meal_fragment_container) == null) {
            val mealMenuFragment = MealFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.meal_fragment_container, mealMenuFragment) // Use FrameLayout ID
                .commit()
        }
    }

    fun showMealDetailFragment(meal: Meal) {
        val mealDetailFragment = MealDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("meal", meal) // Pass the meal object
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.meal_fragment_container, mealDetailFragment) // Replace the current fragment
            .addToBackStack(null) // Add to back stack to allow back navigation
            .commit()
    }
}
