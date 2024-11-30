package com.group35.nutripath.homemenu

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.group35.nutripath.databinding.ViewholderBottomObjectViewBinding

class BottomObjectViewAdapter (val items: MutableList<BottomDataObject>) :
    RecyclerView.Adapter<BottomObjectViewAdapter.Viewholder>() {

        private var context: Context? = null

        class Viewholder(val binding: ViewholderBottomObjectViewBinding) :
            RecyclerView.ViewHolder(binding.root)


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BottomObjectViewAdapter.Viewholder {
            context = parent.context
            val binding =
                ViewholderBottomObjectViewBinding.inflate(LayoutInflater.from(context), parent, false)
            return Viewholder(binding)
        }

        override fun onBindViewHolder(holder: BottomObjectViewAdapter.Viewholder, position: Int) {
            holder.binding.titleTxt.text = items[position].title
            holder.binding.priceTxt.text = "$" + items[position].price.toString()
            holder.binding.ratingTxt.text = items[position].rating.toString()

            Glide.with(holder.itemView.context)
                .load(items[position].picUrl[0])
                .apply(RequestOptions.centerCropTransform())
                .into(holder.binding.pic)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, BottomObjectViewActivity::class.java)
                intent.putExtra("object", items[position])
                holder.itemView.context.startActivity(intent)
            }

        }

        override fun getItemCount(): Int = items.size
    }
