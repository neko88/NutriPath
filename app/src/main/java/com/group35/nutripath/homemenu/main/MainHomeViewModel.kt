package com.group35.nutripath.homemenu.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.group35.nutripath.R
import com.group35.nutripath.homemenu.dataobject.BottomDataObject
import com.group35.nutripath.homemenu.dataobject.MiddleDataObject
import com.group35.nutripath.homemenu.dataobject.TopDataObject

class MainHomeViewModel : ViewModel() {
    private val _viewObjectTop = MutableLiveData<List<TopDataObject>>()
    private val _viewObjectMiddle = MutableLiveData<List<MiddleDataObject>>()
    private val _viewObjectBottom = MutableLiveData<List<BottomDataObject>>()
    val _randomFoodTagList = MutableLiveData<List<String>>()

    val viewObjectTop: LiveData<List<TopDataObject>> = _viewObjectTop
    val viewObjectMiddle: LiveData<List<MiddleDataObject>> = _viewObjectMiddle
    val viewObjectBottom: LiveData<List<BottomDataObject>> = _viewObjectBottom

    val randomFoodTagList: MutableLiveData<List<String>> = _randomFoodTagList

    init {
        loadTopBannerItems()
        randomFoodTagList.value = listOf("chicken", "pasta", "salad", "soup", "pizza", "apple", "cake")
    }

    fun loadTopBannerItems() {
        _viewObjectTop.value = listOf(
            TopDataObject(R.drawable.np_banner1,"",""),
            TopDataObject(R.drawable.np_banner2,"",""),
            TopDataObject(R.drawable.np_banner3,"","")
        )
    }
    fun addTopBannerItem(newItem: TopDataObject) {
        val currentItems = _viewObjectTop.value ?: listOf()
        _viewObjectTop.value = currentItems + newItem
    }

    fun loadMiddleBannerItems(){
        _viewObjectMiddle.value = listOf(
            MiddleDataObject("Title 1", 0, R.drawable.np_ic_chicken),
            MiddleDataObject("Title 2", 0, R.drawable.np_ic_sushi),
            MiddleDataObject("Title 3", 0, R.drawable.np_ic_vegetarian),
            MiddleDataObject("Title 4", 0, R.drawable.np_ic_breakfast)
        )
    }
    fun addMiddleBannerItem(newItem: MiddleDataObject) {
        val currentItems = _viewObjectMiddle.value ?: listOf()
        _viewObjectMiddle.value = currentItems + newItem
    }

    fun loadBottomBannerItems(){
        _viewObjectBottom.value = listOf(
            BottomDataObject("Title 1"),
            BottomDataObject("Title 2"),
            BottomDataObject("Title 3"),
            BottomDataObject("Title 4")
        )
    }
    fun addBottomBannerItem(newItem: BottomDataObject) {
        val currentItems = _viewObjectBottom.value ?: listOf()
        _viewObjectBottom.value = currentItems + newItem
    }

    fun getRandomFoodTagList(): List<String>? {
        return _randomFoodTagList.value
    }


}




