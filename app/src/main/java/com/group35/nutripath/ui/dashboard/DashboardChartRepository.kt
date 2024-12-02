package com.group35.nutripath.ui.dashboard

import com.github.mikephil.charting.data.PieEntry

class DashboardChartRepository {


    fun setTotalExpense(expenseAmount: Float) {
        val totalExpenses = expenseAmount // Add to cumulative expenses
        val currentBudget = 225.0f//_budgetAllocations.value?.sumOf { it.value.toDouble() }?.toFloat() ?: 0f

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
        //_budgetAllocations.value = allocations
    }
}