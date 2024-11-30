package com.group35.nutripath.homemenu


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.group35.nutripath.R


class TopObjectViewAdapter(
    private var sliderItems: List<TopDataObject>,
    private val viewPager2: ViewPager2

) : RecyclerView.Adapter<TopObjectViewAdapter.SliderViewholder>() {
    private lateinit var context: Context
    private val runnable = Runnable {
        sliderItems = sliderItems
        notifyDataSetChanged()
    }

    class SliderViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageSlide)

        fun setImage(sliderModel: TopDataObject, context: Context) {
            Glide.with(context)
                .load(sliderModel.url)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopObjectViewAdapter.SliderViewholder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.slider_item_container, parent, false)
        return SliderViewholder(view)
    }

    override fun onBindViewHolder(holder: TopObjectViewAdapter.SliderViewholder, position: Int) {
        holder.setImage(sliderItems[position], context)
        if (position == sliderItems.lastIndex - 1) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int = sliderItems.size
}