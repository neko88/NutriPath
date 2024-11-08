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

        return view
    }

    private fun loadData() {
        val name = sharedPreferences.getString("name", "")
        val gender = sharedPreferences.getString("gender", "")
        val age = sharedPreferences.getString("age", "")
        val height = sharedPreferences.getString("height", "")
        val weight = sharedPreferences.getString("weight", "")
        val occupation = sharedPreferences.getString("occupation", "")

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
    }

    private fun saveData() {
        val name = view.findViewById<EditText>(R.id.name_edit)?.text.toString()
        val gender = view.findViewById<Spinner>(R.id.gender_edit)?.selectedItem.toString()
        val age = view.findViewById<EditText>(R.id.age_edit)?.text.toString()
        val height = view.findViewById<EditText>(R.id.height_edit)?.text.toString()
        val weight = view.findViewById<EditText>(R.id.weight_edit)?.text.toString()
        val occupation = view.findViewById<EditText>(R.id.occupation_edit)?.text.toString()

        val editor = sharedPreferences.edit()

        editor.putString("name", name)
        editor.putString("age", age)
        editor.putString("height", height)
        editor.putString("weight", weight)
        editor.putString("occupation", occupation)

        if (gender == "(Select your gender here)") {
            editor.putString("gender", "")
        } else {
            editor.putString("gender", gender)
        }

        editor.apply()
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