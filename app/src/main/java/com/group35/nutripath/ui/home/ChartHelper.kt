package com.group35.nutripath.utils

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.components.XAxis

object ChartHelper {

    fun setupEmptyPieChart(pieChart: PieChart) {
        val placeholderEntries = listOf(PieEntry(1f, ""))
        val dataSet = PieDataSet(placeholderEntries, "").apply {
            colors = listOf(Color.LTGRAY)
            setDrawValues(false)
        }
        pieChart.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            legend.isEnabled = false
            invalidate()
        }
    }

    fun setupEmptyLineChart(lineChart: LineChart) {
        val placeholderEntries = listOf(Entry(0f, 0f))
        val dataSet = LineDataSet(placeholderEntries, "").apply {
            color = Color.LTGRAY
            setDrawValues(false)
        }
        lineChart.apply {
            data = LineData(dataSet)
            description.isEnabled = false
            legend.isEnabled = false
            setupAxisProperties()
            invalidate()
        }
    }

    private fun LineChart.setupAxisProperties() {
        xAxis.apply {
            granularity = 1f
            setLabelCount(7, true)
            axisMinimum = 1f
            axisMaximum = 7f
            position = XAxis.XAxisPosition.BOTTOM
        }
        axisLeft.apply {
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "$" + String.format("%.2f", value)
                }
            }
        }
        axisRight.isEnabled = false
    }

    fun updateFoodBudgetPieChart(pieChart: PieChart, budget: Float, expenses: Float) {
        val remaining = budget - expenses
        val exceedingAmount = (expenses - budget).coerceAtLeast(0f)
        val spentWithinBudget = expenses.coerceAtMost(budget)

        // Prepare data entries for spent, remaining, and exceeding portions
        val entries = mutableListOf<PieEntry>().apply {
            if (spentWithinBudget > 0) add(PieEntry(spentWithinBudget, "Spent"))
            if (remaining > 0) add(PieEntry(remaining, "Remaining"))
            if (exceedingAmount > 0) add(PieEntry(exceedingAmount, "Exceeding"))
        }

        // Define colors for the chart
        val colors = mutableListOf<Int>().apply {
            if (spentWithinBudget > 0) add(Color.parseColor("#738E51")) // Solid green for spent
            if (remaining > 0) add(Color.parseColor("#9dba9a")) // Muted green for remaining
            if (exceedingAmount > 0) add(Color.parseColor("#ba3c3c")) // Red for exceeding
        }

        // Create dataset and data
        val dataSet = PieDataSet(entries, "").apply {
            this.colors = colors
            sliceSpace = 2f
            valueTextColor = Color.BLACK
            valueTextSize = 14f
        }

        val pieData = PieData(dataSet).apply {
            setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "$" + String.format("%.2f", value)
                }
            })
        }

        // Update the PieChart
        pieChart.apply {
            data = pieData
            description.isEnabled = false
            legend.isEnabled = false
            setEntryLabelColor(Color.BLACK)
            invalidate() // Refresh the chart
        }
    }

    fun updateLineChart(lineChart: LineChart, entries: List<Entry>) {
        val dataSet = LineDataSet(entries, "Spending Over Last 7 Days").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            valueTextSize = 10f
            setDrawValues(false)
        }
        lineChart.apply {
            data = LineData(dataSet)
            description.isEnabled = false
            legend.isEnabled = false
            setupAxisProperties()
            invalidate()
        }
    }
}
