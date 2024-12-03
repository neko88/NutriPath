
package com.group35.nutripath.api.themealdb

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.group35.nutripath.NutriPathApplication
import com.group35.nutripath.NutriPathFoodViewModel
import com.group35.nutripath.databinding.FragmentRecipeMenuBinding
import kotlinx.coroutines.launch

class MealFragment : Fragment() {

    private var _binding: FragmentRecipeMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MealViewModel
    private lateinit var mealAdapter: MealAdapter

    private val appFoodViewModel: NutriPathFoodViewModel by lazy {
        (requireActivity().application as NutriPathApplication).nutripathFoodViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[MealViewModel::class.java]
        mealAdapter = MealAdapter(appFoodViewModel){ meal ->
            val action = MealFragmentDirections.actionMealFragmentToMealDetailFragment(meal)
            findNavController().navigate(action)
        }

        binding.mealRecycleView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mealAdapter
        }

        // Observe LiveData
        viewModel.mealRecommendationList.observe(viewLifecycleOwner) { meals ->
            mealAdapter.submitList(meals.toList()) // Update the adapter
            mealAdapter.notifyDataSetChanged()
        }

        // Fetch default meals
        lifecycleScope.launch {
            viewModel.getMealByIngredient("") // Pass an empty string or default ingredient
        }

        binding.mealSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
             //   Log.d("SearchView", "Searching submitted for: $query")
                query?.let {
                    viewModel.getMealByIngredient(it)
                    appFoodViewModel.addUserFoodTag(it)}    // add it to the user's tag list
                binding.mealSearchBar.clearFocus() // Clear focus to hide keyboard
                return true }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

