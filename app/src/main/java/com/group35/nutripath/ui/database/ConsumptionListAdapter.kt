package com.group35.nutripath.ui.database
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.group35.nutripath.R

/*
 * When entering a food item, user can view a list of all the previously entered food items, and add it.
 * User can also choose to enter a new food item instead.
 */
class ConsumptionListAdapter(private val context: Context, private var consumptionList: List<FoodItem>) : BaseAdapter(){

    override fun getItem(position: Int): Any {
        return consumptionList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return consumptionList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // TODO: implement view
        // might not be directly viewable
        val view: View = View.inflate(context, R.layout.layout_food_adapter,null)
        return view
    }

    fun replace(newFoodItemList: List<FoodItem>){
        consumptionList = newFoodItemList
    }

}
