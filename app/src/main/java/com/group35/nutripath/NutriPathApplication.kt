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


class NutriPathApplicationViewModel(application: Application) : AndroidViewModel(application) {

    private var lastEdamamApiCall: Long = 0L
    private val _edamamApiTimerOK = MutableLiveData<Boolean>(true)
    private val callInterval = 60000L

    fun edamamApiCallOK(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEdamamApiCall >= callInterval) {
            lastEdamamApiCall = currentTime
            return true
        }
        return false
    }
}



class NutriPathApplication: Application() {

    val nutriPathApplicationViewModel by lazy {
        NutriPathApplicationViewModel(this)
    }

    override fun onCreate() {
        super.onCreate()
    }

}