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
class FoodItemListAdapter(private val context: Context, private var foodItemList: List<FoodItem>) : BaseAdapter(){

    override fun getItem(position: Int): Any {
        return foodItemList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return foodItemList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // TODO: implement view
        val view: View = View.inflate(context, R.layout.layout_food_adapter,null)

        val foodName : TextView = view.findViewById(R.id.food_name)
        val numCalories : TextView = view.findViewById(R.id.food_calories)

        foodName.text = foodItemList[position].name
        numCalories.text = foodItemList[position].cals.toString()

        return view
    }

    fun replace(newFoodItemList: List<FoodItem>){
        foodItemList = newFoodItemList
    }

}