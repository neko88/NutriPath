package com.group35.nutripath.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        sharedPreferences = requireActivity().getSharedPreferences("profile", Context.MODE_PRIVATE)

        loadData()

        println("Porchee debug: budget = \$ ${getMonthlyBudget()}")

        return view
    }

    // get the monthly budget from user

    // if it's 0, alert the user and add a notification?
    fun getMonthlyBudget(): Int {
        val budget = sharedPreferences.getString("budget", "")

        return if (budget == "") {
            0
        } else {
            budget!!.replace("[^\\d]".toRegex(), "").toInt()
        }
    }

    private fun loadData() {
        val name = sharedPreferences.getString("name", "")
        val gender = sharedPreferences.getString("gender", "")
        val age = sharedPreferences.getString("age", "")
        val height = sharedPreferences.getString("height", "")
        val weight = sharedPreferences.getString("weight", "")
        val occupation = sharedPreferences.getString("occupation", "")
        val budget = sharedPreferences.getString("budget", "")
        val activityLevel = sharedPreferences.getString("activityLevel", "")
        val healthGoal = sharedPreferences.getString("healthGoal", "")

        view.findViewById<EditText>(R.id.name_edit)?.setText(name)
        view.findViewById<EditText>(R.id.age_edit)?.setText(age)
        view.findViewById<EditText>(R.id.height_edit)?.setText(height)
        view.findViewById<EditText>(R.id.weight_edit)?.setText(weight)
        view.findViewById<EditText>(R.id.occupation_edit)?.setText(occupation)

        if (gender == "") {
            view.findViewById<Spinner>(R.id.gender_edit)?.setSelection(0)
        } else {
            val genderOptions = resources.getStringArray(R.array.gender_options)
            val genderIndex = genderOptions.indexOf(gender)
            view.findViewById<Spinner>(R.id.gender_edit)?.setSelection(genderIndex)
        }

        if (budget == "") {
            view.findViewById<Spinner>(R.id.budget_edit)?.setSelection(0)
        } else {
            val budgetOptions = resources.getStringArray(R.array.budget_options)
            val budgetIndex = budgetOptions.indexOf(budget)
            view.findViewById<Spinner>(R.id.budget_edit)?.setSelection(budgetIndex)
        }

        if (activityLevel == "") {
            view.findViewById<Spinner>(R.id.activityLevel_edit)?.setSelection(0)
        } else {
            val activityLevelOptions = resources.getStringArray(R.array.activityLevel_options)
            val activityLevelIndex = activityLevelOptions.indexOf(activityLevel)
            view.findViewById<Spinner>(R.id.activityLevel_edit)?.setSelection(activityLevelIndex)
        }

        if (healthGoal == "") {
            view.findViewById<Spinner>(R.id.healthgoal_edit)?.setSelection(0)
        } else {
            val healthGoalOptions = resources.getStringArray(R.array.healthGoal_options)
            val healthGoalIndex = healthGoalOptions.indexOf(healthGoal)
            view.findViewById<Spinner>(R.id.healthgoal_edit)?.setSelection(healthGoalIndex)
        }
    }

    private fun saveData() {
        val name = view.findViewById<EditText>(R.id.name_edit)?.text.toString()
        val gender = view.findViewById<Spinner>(R.id.gender_edit)?.selectedItem.toString()
        val age = view.findViewById<EditText>(R.id.age_edit)?.text.toString()
        val height = view.findViewById<EditText>(R.id.height_edit)?.text.toString()
        val weight = view.findViewById<EditText>(R.id.weight_edit)?.text.toString()
        val occupation = view.findViewById<EditText>(R.id.occupation_edit)?.text.toString()
        val budget = view.findViewById<Spinner>(R.id.budget_edit)?.selectedItem.toString()
        val activityLevel = view.findViewById<Spinner>(R.id.activityLevel_edit)?.selectedItem.toString()
        val healthGoal = view.findViewById<Spinner>(R.id.healthgoal_edit)?.selectedItem.toString()

        val editor = sharedPreferences.edit()

        editor.putString("name", name)
        editor.putString("age", age)
        editor.putString("height", height)
        editor.putString("weight", weight)
        editor.putString("occupation", occupation)

        if (gender == "(Select your gender)") {
            editor.putString("gender", "")
        } else {
            editor.putString("gender", gender)
        }

        if (budget == "(Select your budget)") {
            editor.putString("budget", "")
        } else {
            editor.putString("budget", budget)
        }

        if (activityLevel == "(Select your activity level)") {
            editor.putString("activityLevel", "")
        } else {
            editor.putString("activityLevel", activityLevel)
        }

        if (healthGoal == "(Select your health goal)") {
            editor.putString("healthGoal", "")
        } else {
            editor.putString("healthGoal", healthGoal)
        }

        editor.apply()

        Toast.makeText(requireActivity(), "You have saved your profile.", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
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
            R.id.save -> {
                saveData()
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}