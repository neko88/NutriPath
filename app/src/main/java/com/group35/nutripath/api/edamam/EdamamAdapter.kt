package com.group35.nutripath.api.edamam

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.group35.nutripath.R

class EdamamAdapter(private var recipes: List<Recipe>,
                    private val onItemClick: (Recipe) -> Unit ) :
    RecyclerView.Adapter<EdamamAdapter.RecipeViewHolder>() {
// Pass a lambda for item clicks
    // ViewHolder holds references to the views in the item layout
    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.ivRecipeImage)
        val recipeName: TextView = itemView.findViewById(R.id.tvRecipeName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_edamam_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.recipeName.text = recipe.label
        Glide.with(holder.itemView.context) // Load image using Glide
            .load(recipe.image)
            .placeholder(R.drawable.ic_nutripath_logo) // Optional placeholder
            .into(holder.recipeImage)

        holder.itemView.setOnClickListener {
            onItemClick(recipe) // Pass the clicked recipe to the listener
        }
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged() // Refresh the RecyclerView
    }
}

