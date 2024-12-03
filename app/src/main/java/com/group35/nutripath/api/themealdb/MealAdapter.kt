package com.group35.nutripath.api.themealdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.group35.nutripath.NutriPathFoodViewModel
import com.group35.nutripath.R

class MealAdapter(private val nutriPathFoodViewModel: NutriPathFoodViewModel, private val onClick: (Meal) -> Unit) :

    ListAdapter<Meal, MealAdapter.MealViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal_recipe_card, parent, false)
        return MealViewHolder(nutriPathFoodViewModel, view, onClick)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class MealViewHolder(private val nutriPathFoodViewModel: NutriPathFoodViewModel, itemView: View, private val onClick: (Meal) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val mealImageView: ImageView = itemView.findViewById(R.id.mealImageView)
        private val mealNameTextView: TextView = itemView.findViewById(R.id.mealTitle)
        private val ingredientImages: List<ImageView> = listOf(
            itemView.findViewById(R.id.ingredientImage1),
            itemView.findViewById(R.id.ingredientImage2),
            itemView.findViewById(R.id.ingredientImage3)
        )
        private val likedButton: ImageButton = itemView.findViewById(R.id.likedButton)
        private val notLikedButton: ImageButton = itemView.findViewById(R.id.NotLikedButton)
        fun bind(meal: Meal) {
            mealNameTextView.text = meal.strMeal

            Glide.with(itemView.context)
                .load(meal.strMealThumb)
                .apply(RequestOptions().transform(RoundedCorners(50))) // 50dp corner radius
                .into(mealImageView)

            ingredientImages.forEach { it.setImageDrawable(null) }

            val ingredientUrls = meal.ingredients.keys.map { ingredient ->
                "https://www.themealdb.com/images/ingredients/$ingredient.png"
            }

            likedButton.setOnClickListener{
                likedButton.visibility = View.GONE
                notLikedButton.visibility = View.VISIBLE
                notLikedButton.isClickable = true
                nutriPathFoodViewModel.addUserFoodTag(meal.strMeal)
                nutriPathFoodViewModel.removeFavouriteMeal(meal)
            }
            notLikedButton.setOnClickListener{
                notLikedButton.visibility = View.GONE
                likedButton.visibility = View.VISIBLE
                likedButton.isClickable = true
                nutriPathFoodViewModel.addUserFoodTag(meal.strMeal)
                nutriPathFoodViewModel.addFavouriteMeal(meal)

            }

            ingredientUrls.forEachIndexed { index, url ->
                if (index < ingredientImages.size) {
                    val encodedUrl = url.replace(" ", "%20") // URL encode spaces
                    Glide.with(itemView.context)
                        .load(encodedUrl)
                        .into(ingredientImages[index])
                }
            }

            itemView.setOnClickListener {
                onClick(meal)
            }
        }
    }

    class MealDiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }
        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
}

