package com.group35.nutripath.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.group35.nutripath.data.BudgetRepository
import org.apache.commons.lang3.mutable.Mutable

class DashboardViewModel : ViewModel() {
    private val _startDay =  MutableLiveData<Long>()
    private val _endDay = MutableLiveData<Long>()
    val startDay: MutableLiveData<Long> = _startDay
    val endDay: MutableLiveData<Long> = _endDay


}