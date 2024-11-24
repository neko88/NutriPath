package com.group35.nutripath.ui.dashboard

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.group35.nutripath.R
import com.group35.nutripath.databinding.ActivityFoodEntryBinding
import com.group35.nutripath.databinding.ActivityMainBinding
import com.group35.nutripath.ui.database.Consumption
import com.group35.nutripath.ui.database.ConsumptionDao
import com.group35.nutripath.ui.database.ConsumptionDatabase
import com.group35.nutripath.ui.database.ConsumptionRepository
import com.group35.nutripath.ui.database.ConsumptionViewModel
import com.group35.nutripath.ui.database.ConsumptionViewModelFactory
import com.group35.nutripath.ui.database.FoodItem
import com.group35.nutripath.ui.database.FoodItemDao
import com.group35.nutripath.ui.database.FoodItemDatabase
import com.group35.nutripath.ui.database.FoodItemListAdapter
import com.group35.nutripath.ui.database.FoodItemRepository
import com.group35.nutripath.ui.database.FoodItemViewModel
import com.group35.nutripath.ui.database.FoodItemViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class FoodEntryActivity: AppCompatActivity() {
    private lateinit var consumptionDB: ConsumptionDatabase
    private lateinit var consumptionDao: ConsumptionDao
    private lateinit var consumptionRepository: ConsumptionRepository

    private lateinit var consumptionViewModelFactory: ConsumptionViewModelFactory
    private lateinit var consumptionViewModel: ConsumptionViewModel

    private lateinit var foodDB: FoodItemDatabase
    private lateinit var foodDao: FoodItemDao
    private lateinit var foodRepository: FoodItemRepository


    private lateinit var foodViewModelFactory: FoodItemViewModelFactory
    private lateinit var foodViewModel: FoodItemViewModel

    private lateinit var foodNameText: EditText
    private lateinit var caloriesText: EditText
    private lateinit var carbsText: EditText
    private lateinit var fatsText: EditText
    private lateinit var proteinText: EditText
    private lateinit var sugarsText: EditText
    private lateinit var priceText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_entry)

        foodNameText = findViewById(R.id.food_name_text)
        caloriesText = findViewById(R.id.calories_text)
        carbsText = findViewById(R.id.carbs_text)
        fatsText = findViewById(R.id.fats_text)
        proteinText = findViewById(R.id.protein_text)
        sugarsText = findViewById(R.id.sugars_text)
        priceText = findViewById(R.id.price_text)

        saveButton = findViewById(R.id.save_food_entry)
        cancelButton = findViewById(R.id.cancel_food_entry)

        initFoodDB()
        initConsumptionDB()

        saveButton.setOnClickListener {
            val food = FoodItem()
            food.name = foodNameText.text.toString()
            food.cals = caloriesText.text.toString().toDoubleOrNull() ?: 0.0
            food.carbs = carbsText.text.toString().toDoubleOrNull() ?: 0.0
            food.fats = fatsText.text.toString().toDoubleOrNull() ?: 0.0
            food.protein = proteinText.text.toString().toDoubleOrNull() ?: 0.0
            food.sugars = sugarsText.text.toString().toDoubleOrNull() ?: 0.0
            food.price = priceText.text.toString().toDoubleOrNull() ?: 0.0

            val consumption = Consumption()
            consumption.foodId = food.id
            consumption.date = System.currentTimeMillis()
            CoroutineScope(IO).launch {
                foodViewModel.insert(food)
                consumptionViewModel.insert(consumption)
            }
            finish()
        }
        cancelButton.setOnClickListener {
            finish()
        }


    }

    private fun initFoodDB(){
        foodDB = FoodItemDatabase.getInstance(this)
        foodDao = foodDB.foodItemDao
        foodRepository = FoodItemRepository(foodDao)
        foodViewModelFactory = FoodItemViewModelFactory(foodRepository)
        foodViewModel = ViewModelProvider(this, foodViewModelFactory).get(FoodItemViewModel::class.java)
    }
    private fun initConsumptionDB(){
        consumptionDB = ConsumptionDatabase.getInstance(this)
        consumptionDao = consumptionDB.consumptionDao
        consumptionRepository = ConsumptionRepository(consumptionDao)
        consumptionViewModelFactory = ConsumptionViewModelFactory(consumptionRepository)
        consumptionViewModel = ViewModelProvider(this, consumptionViewModelFactory).get(ConsumptionViewModel::class.java)
    }
}