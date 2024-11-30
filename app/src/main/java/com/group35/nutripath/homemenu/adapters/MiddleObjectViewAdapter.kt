package com.group35.nutripath.homemenu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.group35.nutripath.databinding.ViewHolderMidViewBinding
import com.group35.nutripath.homemenu.dataobject.MiddleDataObject


// For the middle slider view
class MiddleObjectViewAdapter (val items: MutableList<MiddleDataObject>) :

    // Recycler view holder
    RecyclerView.Adapter<MiddleObjectViewAdapter.Viewholder>(){
        private lateinit var context: Context

        inner class Viewholder(val binding: ViewHolderMidViewBinding) :
            RecyclerView.ViewHolder(binding.root) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
            context = parent.context
            val binding = ViewHolderMidViewBinding.inflate(LayoutInflater.from(context), parent, false)
            return Viewholder(binding)
        }

        override fun onBindViewHolder(holder: Viewholder, position: Int) {
            val item = items[position]
            holder.binding.titleTxt.text = item.title

            Glide.with(holder.itemView.context)
                .load(item.picUrl)
                .into(holder.binding.pic)
        }

        override fun getItemCount(): Int = items.size


}