package com.group35.nutripath.homemenu.helper



import android.content.Context
import android.widget.Toast

import com.group35.nutripath.homemenu.adapters.ChangeNumberItemsListener
import com.group35.nutripath.homemenu.dataobject.ItemObject


class SelectionManager(val context: Context) {

    private val tinyDB = TinyDB(context)

    fun insertItem(item: ItemObject) {
        var listItem = getListCart()
        val existAlready = listItem.any { it.title == item.title }
        val index = listItem.indexOfFirst { it.title == item.title }

        if (existAlready) {
            listItem[index].numberInCart = item.numberInCart
        } else {
            listItem.add(item)
        }
        tinyDB.putListObject("CartList", listItem)
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<ItemObject> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    fun minusItem(listItem: ArrayList<ItemObject>, position: Int, listener: ChangeNumberItemsListener) {
        if (listItem[position].numberInCart == 1) {
            listItem.removeAt(position)
        } else {
            listItem[position].numberInCart--
        }
        tinyDB.putListObject("CartList", listItem)
        listener.onChanged()
    }

    fun plusItem(listItem: ArrayList<ItemObject>, position: Int, listener: ChangeNumberItemsListener) {
        listItem[position].numberInCart++
        tinyDB.putListObject("CartList", listItem)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listItem = getListCart()
        var fee = 0.0
        for (item in listItem) {
            fee += item.price * item.numberInCart
        }
        return fee
    }
}