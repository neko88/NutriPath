package com.group35.nutripath.api.themealdb

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.group35.nutripath.R
import com.group35.nutripath.api.themealdb.Meal

class MealAdapter(private val onClick: (Meal) -> Unit) :
    ListAdapter<Meal, MealAdapter.MealViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MealViewHolder(itemView: View, private val onClick: (Meal) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val mealImageView: ImageView = itemView.findViewById(R.id.mealImageView)
        private val mealNameTextView: TextView = itemView.findViewById(R.id.mealTitle)
        private val mealDescriptionTextView: TextView = itemView.findViewById(R.id.mealDescription)
        private val ingredientImages: List<ImageView> = listOf(
            itemView.findViewById(R.id.ingredientImage1),
            itemView.findViewById(R.id.ingredientImage2),
            itemView.findViewById(R.id.ingredientImage3)
        )

        fun bind(meal: Meal) {
            // Bind meal name and description
            mealNameTextView.text = meal.strMeal
            mealDescriptionTextView.text = meal.strInstructions

            // Load meal image
            Glide.with(itemView.context)
                .load(meal.strMealThumb)
                .apply(RequestOptions().transform(RoundedCorners(50))) // 50dp corner radius
                .into(mealImageView)

            // Clear ingredient images to handle dynamic updates
            ingredientImages.forEach { it.setImageDrawable(null) }

            // Load ingredient images dynamically from the map
            val ingredientUrls = meal.ingredients.keys.map { ingredient ->
                "https://www.themealdb.com/images/ingredients/$ingredient.png"
            }

            ingredientUrls.forEachIndexed { index, url ->
                if (index < ingredientImages.size) {
                    val encodedUrl = url.replace(" ", "%20") // URL encode spaces
                    Glide.with(itemView.context)
                        .load(encodedUrl)
                        .into(ingredientImages[index])
                }
            }

            // Set click listener for item
            itemView.setOnClickListener {
                onClick(meal) // Pass the meal to the onClick function
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

