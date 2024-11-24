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

    private var editTextList = Array(7) {EditText(this)}

    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_entry)

        editTextList[0] = findViewById(R.id.food_name_text)
        editTextList[1] = findViewById(R.id.calories_text)
        editTextList[2] = findViewById(R.id.carbs_text)
        editTextList[3] = findViewById(R.id.fats_text)
        editTextList[4] = findViewById(R.id.protein_text)
        editTextList[5] = findViewById(R.id.sugars_text)
        editTextList[6] = findViewById(R.id.price_text)

        saveButton = findViewById(R.id.save_food_entry)
        cancelButton = findViewById(R.id.cancel_food_entry)

        initFoodDB()
        initConsumptionDB()

        saveButton.setOnClickListener {
            val food = FoodItem()
            food.name = editTextList[0].toString()
            food.cals = editTextList[1].toString().toDouble()
            food.carbs = editTextList[2].toString().toDouble()
            food.fats = editTextList[3].toString().toDouble()
            food.protein = editTextList[4].toString().toDouble()
            food.sugars = editTextList[5].toString().toDouble()
            food.price = editTextList[6].toString().toDouble()

            val consumption = Consumption()
            consumption.foodId = food.id
            consumption.date = System.currentTimeMillis()
            lifecycleScope.launch {
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