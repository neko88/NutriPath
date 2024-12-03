
package com.group35.nutripath.api.themealdb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.group35.nutripath.R
import com.bumptech.glide.Glide
import com.group35.nutripath.databinding.FragmentRecipeMenuBinding
import com.group35.nutripath.databinding.FragmentMealDetailsBinding

class MealDetailFragment : Fragment() {

    private var _binding: FragmentMealDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MealViewModel
    private lateinit var meal: Meal

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        meal = arguments?.getParcelable("meal") ?: throw IllegalStateException("Meal is required")

        viewModel = ViewModelProvider(this)[MealViewModel::class.java]

        val ingredients = meal.ingredients.map { (name, measure) ->
            Triple(name, "https://www.themealdb.com/images/ingredients/$name.png", measure)
        }

        val adapter = IngredientAdapter(ingredients)
        binding.ingredientsRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 5) // 5 columns
            this.adapter = adapter
        }

        binding.mealName.text = meal.strMeal
        binding.mealDescription
        viewModel.getMealDetails(meal.idMeal)

        viewModel.mealImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.np_nutrileaf) // Placeholder
                .error(R.drawable.np_nutrileaf) // Error image
                .into(binding.mealImageView)
        }

        viewModel.mealArea.observe(viewLifecycleOwner) { area ->
            binding.cuisineTextView.text = area
            binding.cuisineImageView.setImageResource(getFlagForArea(area))
        }

        viewModel.mealCategory.observe(viewLifecycleOwner) { category ->
            binding.categoryTextView.text = category
            val categoryImageUrl = "https://www.themealdb.com/images/category/$category.png"
            Glide.with(requireContext())
                .load(categoryImageUrl)
                .placeholder(R.drawable.np_radish_balloon)
                .error(R.drawable.np_radish_balloon)
                .into(binding.categoryImageView)
        }

        viewModel.mealMainIngredient.observe(viewLifecycleOwner) { mainIngredient ->
            binding.mainIngTextView.text = mainIngredient
            val ingredientImageUrl = "https://www.themealdb.com/images/ingredients/$mainIngredient.png"
            Glide.with(requireContext())
                .load(ingredientImageUrl)
                .placeholder(R.drawable.np_radish_icecream)
                .error(R.drawable.np_radish_icecream)
                .into(binding.ingredientMainImageView)
        }
    }

    // Map area names to emoji flags
    private fun getFlagForArea(area: String): Int {
        return when (area) {
            "American" -> R.drawable.np_flag_canada
            "British" -> R.drawable.np_nutrileaf
            "Canadian" -> R.drawable.np_flag_canada
            "Chinese" -> R.drawable.np_flag_china
            "French" -> R.drawable.np_flag_france
            //filipino
            "Greek" -> R.drawable.np_nutrileaf
            "Indian" -> R.drawable.np_flag_india
            "Italian" -> R.drawable.np_nutrileaf
            "Japanese" -> R.drawable.np_nutrileaf
            "Mexican" -> R.drawable.np_nutrileaf
            "Spanish" -> R.drawable.np_nutrileaf
            "Thai" -> R.drawable.np_nutrileaf
            //turkish
            //tunisian
            "Vietnamese" -> R.drawable.np_flag_viet
            else -> R.drawable.np_nutrileaf // Default placeholder image
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
