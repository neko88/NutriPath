package com.group35.nutripath.api.openfoodfacts

import OpenFoodFactsApi
import com.group35.nutripath.api.themealdb.ResponseMeal
import com.group35.nutripath.api.themealdb.ResponseMealInformation
import retrofit2.Call

class OpenFoodFactsRepository {
    private val apiCall = RetroFitCallerOpenFoodFacts.api

    fun getProductByBarcode(barcode: String): Call<ResponseFoodProducts> {
        return apiCall.getProductByBarcode(barcode)
    }
}