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

    fun updatePieChart(pieChart: PieChart, allocations: List<PieEntry>) {
        val dataSet = PieDataSet(allocations, "").apply {
            colors = listOf(
                Color.parseColor("#FF5733"), // Housing
                Color.parseColor("#33FF57"), // Transportation
                Color.parseColor("#3357FF"), // Food
                Color.parseColor("#FF33FF"), // Savings
                Color.parseColor("#FFFF33"), // Health
                Color.parseColor("#33FFFF")  // Entertainment
            )
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
        pieChart.apply {
            data = pieData
            description.isEnabled = false
            legend.isEnabled = false
            setEntryLabelColor(Color.BLACK)
            invalidate()
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
