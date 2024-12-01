package com.group35.nutripath.homemenu.adapters


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.group35.nutripath.R
import com.group35.nutripath.homemenu.dataobject.ItemObject
import com.group35.nutripath.homemenu.dataobject.TopDataObject


class TopObjectViewAdapter(
    private var sliderItems: MutableList<TopDataObject>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<TopObjectViewAdapter.SliderViewHolder>() {

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageSlide)

        fun bind(data: TopDataObject) {
            val radius = 16 // Corner radius in pixels
            Glide.with(itemView.context)
                .load(data.resource)
                .transform(CenterCrop(), RoundedCorners(radius)) // Apply rounded corners
                .into(imageView)

          //  textView.text = data.label

            // Set click listener to open the URL in a browser
            imageView.setOnClickListener {
                val context = itemView.context
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.url))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.slider_item_container, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(sliderItems[position])
    }

    override fun getItemCount(): Int = sliderItems.size

    // Update a single item
    fun updateSingleItem(position: Int, newItem: TopDataObject) {
        if (position in sliderItems.indices) {
            sliderItems[position] = newItem
            notifyItemChanged(position) // Notify only the changed item
        }
    }
}

