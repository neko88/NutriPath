package com.group35.nutripath.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.group35.nutripath.R
import com.group35.nutripath.databinding.FragmentProfileBinding

class ProfileFragment: Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var view: View

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        view = binding.root

        sharedPreferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        setupView()

        loadProfile()

        binding.buttonFinish.setOnClickListener {
            if (noEmptyFields()) {
                saveProfile()
                Toast.makeText(requireActivity(), "You have saved your profile.", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp() // Navigate back to the previous fragment
            }
        }

        return view
    }

    private fun loadProfile() {
        val name = sharedPreferences.getString("name", "")
        val gender = sharedPreferences.getString("gender", "")
        val age = sharedPreferences.getString("age", "")
        val height = sharedPreferences.getString("height", "")
        val weight = sharedPreferences.getString("weight", "")
        val occupation = sharedPreferences.getString("occupation", "")
        val budget = sharedPreferences.getString("budget", "")
        val activityLevel = sharedPreferences.getString("activityLevel", "")
        val healthGoal = sharedPreferences.getString("healthGoal", "")
        val dietaryPreference = sharedPreferences.getString("dietaryPreference", "")

        view.findViewById<TextInputEditText>(R.id.editName).setText(name)
        view.findViewById<AutoCompleteTextView>(R.id.editGender).setText(gender, false)
        view.findViewById<TextInputEditText>(R.id.editAge).setText(age)
        view.findViewById<TextInputEditText>(R.id.editHeight).setText(height)
        view.findViewById<TextInputEditText>(R.id.editWeight).setText(weight)
        view.findViewById<TextInputEditText>(R.id.editOccupation).setText(occupation)
        view.findViewById<TextInputEditText>(R.id.editBudget).setText(budget)
        view.findViewById<AutoCompleteTextView>(R.id.editActivityLevel).setText(activityLevel, false)
        view.findViewById<AutoCompleteTextView>(R.id.editHealthGoal).setText(healthGoal, false)
        view.findViewById<AutoCompleteTextView>(R.id.editDietaryPreference).setText(dietaryPreference, false)
    }

    private fun setupView() {
        val genderOptions = listOf("Male", "Female")
        val genderAdapter = ArrayAdapter(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, genderOptions)
        binding.editGender.setAdapter(genderAdapter)

        val activityLevelOptions = listOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extra Active")
        val activityLevelAdapter = ArrayAdapter(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, activityLevelOptions)
        binding.editActivityLevel.setAdapter(activityLevelAdapter)

        val healthGoalOptions = listOf("Weight Loss", "Muscle Gain", "Maintenance")
        val healthGoalAdapter = ArrayAdapter(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, healthGoalOptions)
        binding.editHealthGoal.setAdapter(healthGoalAdapter)

        val dietaryPreferenceOptions = listOf("None/Normal", "Vegetarian", "Vegan", "Dairy-Free", "Low-Fat")
        val dietaryPreferenceAdapter = ArrayAdapter(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dietaryPreferenceOptions)
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
        val editor = sharedPreferences.edit()

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

        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        // Enable the up button in the action bar
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Navigate up to go back to the previous fragment
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}