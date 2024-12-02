package com.group35.nutripath.api.themealdb
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.group35.nutripath.R



class IngredientAdapter(private val ingredients: List<Triple<String, String, String>>) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient_detail, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val (ingredientName, ingredientImageUrl, ingredientMeasure) = ingredients[position]
        holder.bind(ingredientName, ingredientImageUrl, ingredientMeasure)
    }

    override fun getItemCount(): Int = ingredients.size

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ingredientImage: ImageView = itemView.findViewById(R.id.ingredientImage)
        private val ingredientName: TextView = itemView.findViewById(R.id.ingredientName)
        private val ingredientMeasure: TextView = itemView.findViewById(R.id.ingredientMeasure)

        fun bind(name: String, imageUrl: String, measure: String) {
            ingredientName.text = name
            ingredientMeasure.text = measure

            Glide.with(itemView.context)
                .load(imageUrl)
                .circleCrop() // Makes the image round
                .placeholder(R.drawable.np_radish_bigheart) // Optional placeholder image
                .error(R.drawable.np_radish_bigheart) // Optional error image
                .into(ingredientImage)
        }
    }
}