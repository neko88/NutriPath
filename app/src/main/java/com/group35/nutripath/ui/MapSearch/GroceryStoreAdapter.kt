package com.group35.nutripath.ui.MapSearch

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.group35.nutripath.R


class GroceryStoreAdapter(private val groceryStores: List<GroceryStore>, private val onItemClick: (LatLng) -> Unit) :
    RecyclerView.Adapter<GroceryStoreAdapter.GroceryStoreViewHolder>() {

    class GroceryStoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.grocery_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryStoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grocery_store, parent, false)
        return GroceryStoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryStoreViewHolder, position: Int) {
        val groceryStore = groceryStores[position]
        holder.nameTextView.text = groceryStore.name
        Log.d("GroceryStoreAdapter", "Displaying grocery store: ${groceryStore.name}")
        holder.itemView.setOnClickListener {
            onItemClick(groceryStore.location)
        }
    }

    override fun getItemCount(): Int {
        return groceryStores.size
    }
}