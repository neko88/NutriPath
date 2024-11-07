package com.group35.nutripath.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.group35.nutripath.data.BudgetRepository

class HomeViewModel : ViewModel() {

    private val repository = BudgetRepository()

    // Exposing LiveData for budget and expense entries
    val budgetAllocations: LiveData<List<PieEntry>> get() = repository.budgetAllocations
    val expenseEntries: LiveData<List<Entry>> get() = repository.expenseEntries

    // Updating budget allocations and expenses by delegating to the repository
    fun updateBudgetAllocations(monthlyBudget: Float) {
        repository.updateBudgetAllocations(monthlyBudget)
    }

    fun addExpense(expenseAmount: Float) {
        repository.addExpense(expenseAmount)
    }
}
