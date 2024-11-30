
package com.group35.nutripath.api.themealdb

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.group35.nutripath.databinding.FragmentMealBinding
import kotlinx.coroutines.launch

class MealFragment : Fragment() {

    private var _binding: FragmentMealBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MealViewModel
    private lateinit var mealAdapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[MealViewModel::class.java]

        // Set up RecyclerView and Adapter
        mealAdapter = MealAdapter { meal ->
            val action = MealFragmentDirections.actionMealFragmentToMealDetailFragment(meal)
            findNavController().navigate(action)
        }
        binding.mealRecycleView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mealAdapter
        }

        // Observe LiveData
        viewModel.mealRecommendationList.observe(viewLifecycleOwner) { meals ->
            mealAdapter.submitList(meals) // Update the adapter
        }

        // Fetch default meals
        lifecycleScope.launch {
            viewModel.getMealByIngredient("") // Pass an empty string or default ingredient
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

