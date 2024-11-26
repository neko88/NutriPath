package com.group35.nutripath.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
/*
 * November 25, 2024 - Modified by Cameron Yee-Ping
 * Added setTotalExpense function for database operations
 */
class BudgetRepository {

    private val _budgetAllocations = MutableLiveData<List<PieEntry>>().apply {
        value = emptyList() // Initialize with an empty list for the pie chart
    }
    val budgetAllocations: LiveData<List<PieEntry>> get() = _budgetAllocations

    private val _expenseEntries = MutableLiveData<List<Entry>>().apply {
        value = emptyList() // Initialize with an empty list for the line chart
    }
    val expenseEntries: LiveData<List<Entry>> get() = _expenseEntries

    private var totalExpenses: Float = 0f // Track cumulative expenses

    fun updateBudgetAllocations(monthlyBudget: Float) {
        totalExpenses = 0f // Reset expenses when budget is updated
        val allocations = listOf(
            PieEntry(monthlyBudget, "Remaining") // Start with the entire budget as remaining
        )
        _budgetAllocations.value = allocations
    }


    fun setTotalExpense(expenseAmount: Float) {
        totalExpenses = expenseAmount // Add to cumulative expenses
        val currentBudget = _budgetAllocations.value?.sumOf { it.value.toDouble() }?.toFloat() ?: 0f

        // Calculate spent, remaining, and exceeding portions
        val spentWithinBudget = totalExpenses.coerceAtMost(currentBudget)
        val exceedingAmount = (totalExpenses - currentBudget).coerceAtLeast(0f)
        val remaining = (currentBudget - spentWithinBudget).coerceAtLeast(0f)

        val allocations = mutableListOf<PieEntry>().apply {
            if (spentWithinBudget > 0) add(PieEntry(spentWithinBudget, "Spent"))
            if (remaining > 0) add(PieEntry(remaining, "Remaining"))
            if (exceedingAmount > 0) add(PieEntry(exceedingAmount, "Exceeding"))
        }

        // Update LiveData with new allocations
        _budgetAllocations.value = allocations
    }
    fun addExpense(expenseAmount: Float) {
        totalExpenses += expenseAmount // Add to cumulative expenses
        val currentBudget = _budgetAllocations.value?.sumOf { it.value.toDouble() }?.toFloat() ?: 0f

        // Calculate spent, remaining, and exceeding portions
        val spentWithinBudget = totalExpenses.coerceAtMost(currentBudget)
        val exceedingAmount = (totalExpenses - currentBudget).coerceAtLeast(0f)
        val remaining = (currentBudget - spentWithinBudget).coerceAtLeast(0f)

        val allocations = mutableListOf<PieEntry>().apply {
            if (spentWithinBudget > 0) add(PieEntry(spentWithinBudget, "Spent"))
            if (remaining > 0) add(PieEntry(remaining, "Remaining"))
            if (exceedingAmount > 0) add(PieEntry(exceedingAmount, "Exceeding"))
        }

        // Update LiveData with new allocations
        _budgetAllocations.value = allocations
    }
}
