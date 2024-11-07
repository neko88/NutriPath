package com.group35.nutripath.api.openfoodfacts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback

class FoodProductViewModel : ViewModel() {
    private val repository = OpenFoodFactsRepository()
    val foodProductResponseList = MutableLiveData<List<ResponseFoodProducts>>()

    fun findFoodsByBarcode(barcode: String) {
        repository.getProductByBarcode(barcode).enqueue(object : Callback<ResponseBarcodeFoodQuery> {
            override fun onResponse(call: Call<ResponseBarcodeFoodQuery>, response: retrofit2.Response<ResponseBarcodeFoodQuery>){
                if (response.isSuccessful) {
                    foodProductResponseList.value = response.body()?.products
                    foodProductResponseList.value?.let {
                        Log.d("MealViewModel", "getMealByIngredient Response Successful : ${foodProductResponseList.value}")
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBarcodeFoodQuery>, t: Throwable) {
                Log.d("MealViewModel", "getMealByIngredient Response Failed : ${t}")
            }
        })
    }
}