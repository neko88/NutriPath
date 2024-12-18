package com.group35.nutripath.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.group35.nutripath.data.BudgetRepository
/*
 * November 25, 2024 - Modified by Cameron Yee-Ping
 *                   - Added setExpense function for database use
 */
class HomeViewModel : ViewModel() {

    private val repository = BudgetRepository()

    // Exposing LiveData for budget and expense entries
    val budgetAllocations: LiveData<List<PieEntry>> get() = repository.budgetAllocations
    val expenseEntries: LiveData<List<Entry>> get() = repository.expenseEntries

    // Updating budget allocations and expenses by delegating to the repository
    fun updateBudgetAllocations(monthlyBudget: Float) {
        repository.updateBudgetAllocations(monthlyBudget)
    }

    // Set the total amount of money spent
    fun setExpense(expenseAmount: Float){
        repository.setTotalExpense(expenseAmount)
    }
    fun addExpense(expenseAmount: Float) {
        repository.addExpense(expenseAmount)
    }
}
