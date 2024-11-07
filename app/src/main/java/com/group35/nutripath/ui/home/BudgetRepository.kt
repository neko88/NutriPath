package com.group35.nutripath.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry

class BudgetRepository {

    private val _budgetAllocations = MutableLiveData<List<PieEntry>>().apply {
        value = emptyList() // Initialize with an empty list for the pie chart
    }
    val budgetAllocations: LiveData<List<PieEntry>> get() = _budgetAllocations

    private val _expenseEntries = MutableLiveData<List<Entry>>().apply {
        value = emptyList() // Initialize with an empty list for the line chart
    }
    val expenseEntries: LiveData<List<Entry>> get() = _expenseEntries

    fun updateBudgetAllocations(monthlyBudget: Float) {
        val allocations = listOf(
            PieEntry(0.35f * monthlyBudget, "Housing"),
            PieEntry(0.15f * monthlyBudget, "Transportation"),
            PieEntry(0.15f * monthlyBudget, "Food"),
            PieEntry(0.15f * monthlyBudget, "Savings"),
            PieEntry(0.10f * monthlyBudget, "Health"),
            PieEntry(0.10f * monthlyBudget, "Entertainment")
        )
        _budgetAllocations.value = allocations
    }

    fun addExpense(expenseAmount: Float) {
        val currentEntries = _expenseEntries.value?.toMutableList() ?: mutableListOf()
        if (currentEntries.size >= 7) {
            currentEntries.removeAt(0)
        }
        val day = (currentEntries.size + 1).toFloat()
        currentEntries.add(Entry(day, expenseAmount))
        _expenseEntries.value = currentEntries
    }
}
