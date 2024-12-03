package com.group35.nutripath.homemenu.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.group35.nutripath.NutriPathFoodViewModel
import com.group35.nutripath.R
import com.group35.nutripath.api.themealdb.Meal
import com.group35.nutripath.api.themealdb.MealAdapter.MealDiffCallback


class BottomObjectViewAdapter(
    private val onClick: (Meal) -> Unit
) : ListAdapter<Meal, BottomObjectViewAdapter.BottomViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_bottom_object, parent, false)
        return BottomViewHolder(itemView, onClick)
    }

    override fun onBindViewHolder(holder: BottomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BottomViewHolder(itemView: View, private val onClick: (Meal) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val favouriteMealTitle: TextView = itemView.findViewById(R.id.favouritedRecipeTitle)
        private val favouriteMealImage: ImageView = itemView.findViewById(R.id.favouritedRecipeImage)

        fun bind(meal: Meal) {
            favouriteMealTitle.text = meal.strMeal
            Glide.with(itemView.context)
                .load(meal.strMealThumb)
                .apply(RequestOptions().transform(RoundedCorners(50)))
                .into(favouriteMealImage)

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
