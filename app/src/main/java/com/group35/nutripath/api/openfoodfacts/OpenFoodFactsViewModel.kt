package com.group35.nutripath.api.openfoodfacts

import ProductRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.MutableLiveData
import retrofit2.Response

class OpenFoodFactsViewModel : ViewModel() {
    private val repository = ProductRepository()

    val productLiveData: MutableLiveData<ProductResponse?> = MutableLiveData()

    fun findFoodByBarcode(barcode: String) {
        viewModelScope.launch {
            try {
                val response: Response<ProductResponse> = repository.getProductByBarcode(barcode)
                if (response.isSuccessful) {
                    productLiveData.postValue(response.body())      // deliver the response
                } else {
                    Log.e("FoodViewModel", "Error: ${response.errorBody()}")
                    productLiveData.postValue(null)
                }
            } catch (e: Exception) {
                Log.e("FoodViewModel", "Exception occurred: ${e.message}")
                productLiveData.postValue(null)
            }
        }
    }


}