package com.group35.nutripath

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class NutriPathApplication: Application() {

    val nutripathFoodViewModel by lazy {
        NutriPathFoodViewModel(this)
    }

    override fun onCreate() {
        super.onCreate()
    }

}