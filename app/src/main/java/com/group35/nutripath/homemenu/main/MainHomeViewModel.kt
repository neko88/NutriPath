package com.group35.nutripath.homemenu.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.group35.nutripath.R
import com.group35.nutripath.api.themealdb.Meal
import com.group35.nutripath.homemenu.dataobject.BottomDataObject
import com.group35.nutripath.homemenu.dataobject.MiddleDataObject
import com.group35.nutripath.homemenu.dataobject.TopDataObject

class MainHomeViewModel : ViewModel() {
    private val _topObjectSliderItems = MutableLiveData<List<TopDataObject>>()
    private val _viewObjectMiddle = MutableLiveData<List<MiddleDataObject>>()
    private val _viewObjectBottom = MutableLiveData<MutableList<Meal>>()

    val topObjectSliderItems: LiveData<List<TopDataObject>> = _topObjectSliderItems
    val viewObjectMiddle: LiveData<List<MiddleDataObject>> = _viewObjectMiddle
    val viewObjectBottom: LiveData<MutableList<Meal>> = _viewObjectBottom

    val topObjectInitialSlides =  listOf(
        TopDataObject(R.drawable.np_banner1,"",""),
        TopDataObject(R.drawable.np_banner1,"",""),
        TopDataObject(R.drawable.np_banner3,"","")
    )

    init {
        loadTopBannerItems()
    }

    fun loadTopBannerItems() {
        _topObjectSliderItems.value = topObjectInitialSlides
    }
    fun addTopBannerItem(newItem: TopDataObject) {
        val currentItems = _topObjectSliderItems.value ?: listOf()
        _topObjectSliderItems.value = currentItems + newItem
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
        _viewObjectBottom.value = mutableListOf(
            Meal(),
        )
    }

    fun getBottomBannerItems(): MutableList<Meal>? {
        return _viewObjectBottom.value
    }
    fun addBottomBannerItem(newItem: BottomDataObject) {
        //val currentItems = _viewObjectBottom.value ?: listOf()
      //  _viewObjectBottom.value = currentItems + newItem
    }


}




