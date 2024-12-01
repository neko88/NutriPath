package com.group35.nutripath.homemenu.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.group35.nutripath.databinding.ViewholderSelectionBinding
import com.group35.nutripath.homemenu.helper.SelectionManager
import com.group35.nutripath.homemenu.dataobject.ItemObject
import kotlin.collections.get


interface ChangeNumberItemsListener {
    fun onChanged()
}

class CartAdapter(
    private val listItemSelected: ArrayList<ItemObject>,
    context: Context,
    var changeNumberItemsListener: ChangeNumberItemsListener? = null

) : RecyclerView.Adapter<CartAdapter.Viewholder>() {
    class Viewholder(val binding: ViewholderSelectionBinding) : RecyclerView.ViewHolder(binding.root)

    private val managmentCart = SelectionManager(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val binding =
            ViewholderSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = listItemSelected[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachItem.text = "$${item.price}"
        holder.binding.totalEachItem.text = "$${Math.round(item.numberInCart * item.price)}"
        holder.binding.numberItemTxt.text = item.numberInCart.toString()

        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.pic)

        holder.binding.plusCartBtn.setOnClickListener {
            managmentCart.plusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }

            })
        }
        holder.binding.minusCartBtn.setOnClickListener {
            managmentCart.minusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }

            })
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}