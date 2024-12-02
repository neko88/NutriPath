package com.group35.nutripath.homemenu.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.group35.nutripath.MainActivity
import com.group35.nutripath.databinding.ActivityProfileSetupBinding
import com.group35.nutripath.util.Globals

class ProfileSetupActivity: AppCompatActivity() {
    private lateinit var binding: ActivityProfileSetupBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)

        setupView()

        binding.buttonFinish.setOnClickListener {
            if (noEmptyFields()) {
                saveProfile()
                Toast.makeText(this, "Profile setup complete!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupView() {
        val genderOptions = listOf("Male", "Female")
        val genderAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, genderOptions)
        binding.editGender.setAdapter(genderAdapter)

        val activityLevelOptions = listOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extra Active")
        val activityLevelAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, activityLevelOptions)
        binding.editActivityLevel.setAdapter(activityLevelAdapter)

        val healthGoalOptions = listOf("Weight Loss", "Muscle Gain", "Maintenance")
        val healthGoalAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, healthGoalOptions)
        binding.editHealthGoal.setAdapter(healthGoalAdapter)

        val dietaryPreferenceOptions = listOf("None/Normal", "Vegetarian", "Vegan", "Dairy-Free", "Low-Fat")
        val dietaryPreferenceAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dietaryPreferenceOptions)
        binding.editDietaryPreference.setAdapter(dietaryPreferenceAdapter)
    }

    private fun noEmptyFields(): Boolean {
        var noEmpty = true

        // Check if Name field is empty
        if (binding.textName.editText?.text.isNullOrEmpty()) {
            binding.textName.error = " "
            noEmpty = false
        } else {
            binding.textName.error = null
        }

        // Check if Gender field is empty
        if (binding.textGender.editText?.text.isNullOrEmpty()) {
            binding.textGender.error = " "
            noEmpty = false
        } else {
            binding.textGender.error = null
        }

        // Check if Age field is empty
        if (binding.textAge.editText?.text.isNullOrEmpty()) {
            binding.textAge.error = " "
            noEmpty = false
        } else {
            binding.textAge.error = null
        }

        // Check if Height field is empty
        if (binding.textHeight.editText?.text.isNullOrEmpty()) {
            binding.textHeight.error = " "
            noEmpty = false
        } else {
            binding.textHeight.error = null
        }

        // Check if Weight field is empty
        if (binding.textWeight.editText?.text.isNullOrEmpty()) {
            binding.textWeight.error = " "
            noEmpty = false
        } else {
            binding.textWeight.error = null
        }

        // Check if Occupation field is empty
        if (binding.textOccupation.editText?.text.isNullOrEmpty()) {
            binding.textOccupation.error = " "
            noEmpty = false
        } else {
            binding.textOccupation.error = null
        }

        // Check if Budget field is empty
        if (binding.textBudget.editText?.text.isNullOrEmpty()) {
            binding.textBudget.error = " "
            noEmpty = false
        } else {
            binding.textBudget.error = null
        }

        // Check if Activity Level field is empty
        if (binding.textActivityLevel.editText?.text.isNullOrEmpty()) {
            binding.textActivityLevel.error = " "
            noEmpty = false
        } else {
            binding.textActivityLevel.error = null
        }

        // Check if Health Goal field is empty
        if (binding.textHealthGoal.editText?.text.isNullOrEmpty()) {
            binding.textHealthGoal.error = " "
            noEmpty = false
        } else {
            binding.textHealthGoal.error = null
        }

        // Check if Dietary Preference field is empty
        if (binding.textDietaryPreference.editText?.text.isNullOrEmpty()) {
            binding.textDietaryPreference.error = " "
            noEmpty = false
        } else {
            binding.textDietaryPreference.error = null
        }

        return noEmpty
    }

    private fun saveProfile() {
        var editor = sharedPreferences.edit()

        val name = binding.editName.text.toString()
        val gender = binding.editGender.text.toString()
        val age = binding.editAge.text.toString()
        val height = binding.editHeight.text.toString()
        val weight = binding.editWeight.text.toString()
        val occupation = binding.editOccupation.text.toString()
        val budget = binding.editBudget.text.toString()
        val activityLevel = binding.editActivityLevel.text.toString()
        val healthGoal = binding.editHealthGoal.text.toString()
        val dietaryPreference = binding.editDietaryPreference.text.toString()

        editor.putString("name", name)
        editor.putString("gender", gender)
        editor.putString("age", age)
        editor.putString("height", height)
        editor.putString("weight", weight)
        editor.putString("occupation", occupation)
        editor.putString("budget", budget)
        editor.putString("activityLevel", activityLevel)
        editor.putString("healthGoal", healthGoal)
        editor.putString("dietaryPreference", dietaryPreference)

        // user has now completed their profile
        editor.putBoolean("isProfileComplete", true)
        editor.apply()
    }
}