package com.group35.nutripath.homemenu.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.group35.nutripath.databinding.ViewholderColorBinding
import com.group35.nutripath.R

class ColorAdapter(val items: MutableList<String>) :
    RecyclerView.Adapter<ColorAdapter.Viewholder>() {

    private var selectedPosition = -1
    private var lastSelectionPosition = -1
    private lateinit var context: Context


    inner class Viewholder(val binding: ViewholderColorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderColorBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: ColorAdapter.Viewholder, position: Int) {
        // Load the image using Glide
        Glide.with(holder.itemView.context)
            .load(items[holder.adapterPosition]) // Use bindingAdapterPosition instead of position
            .into(holder.binding.pic)

        // Handle item click
        holder.binding.root.setOnClickListener {
            lastSelectionPosition = selectedPosition
            selectedPosition = holder.adapterPosition // Use bindingAdapterPosition here
            notifyItemChanged(lastSelectionPosition)
            notifyItemChanged(selectedPosition)
        }

        // Update the background based on selection
        if (selectedPosition == holder.adapterPosition) {
            holder.binding.colorLayout.setBackgroundResource(R.drawable.icon_nutripath)
        } else {
            holder.binding.colorLayout.setBackgroundResource(R.drawable.icon_nutripath)
        }
    }


    override fun getItemCount(): Int = items.size
}