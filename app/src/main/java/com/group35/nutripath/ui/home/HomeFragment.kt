package com.group35.nutripath.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.group35.nutripath.R
import com.group35.nutripath.utils.ChartHelper
import com.group35.nutripath.utils.DialogHelper

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var pieChart: PieChart
    private lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        pieChart = root.findViewById(R.id.pieChart)
        lineChart = root.findViewById(R.id.lineChart)

        // Set up empty charts initially
        ChartHelper.setupEmptyPieChart(pieChart)
        ChartHelper.setupEmptyLineChart(lineChart)

        root.findViewById<Button>(R.id.setBudgetButton).setOnClickListener {
            DialogHelper.showSetBudgetDialog(requireContext()) { budget ->
                homeViewModel.updateBudgetAllocations(budget)
            }
        }

        root.findViewById<Button>(R.id.addExpenseButton).setOnClickListener {
            DialogHelper.showAddExpenseDialog(requireContext()) { expense ->
                homeViewModel.addExpense(expense)
            }
        }

        // Observe the LiveData to populate charts, including initial empty data
        homeViewModel.budgetAllocations.observe(viewLifecycleOwner, Observer { allocations ->
            if (allocations.isEmpty()) {
                ChartHelper.setupEmptyPieChart(pieChart)
            } else {
                ChartHelper.updatePieChart(pieChart, allocations)
            }
        })

        homeViewModel.expenseEntries.observe(viewLifecycleOwner, Observer { entries ->
            if (entries.isEmpty()) {
                ChartHelper.setupEmptyLineChart(lineChart)
            } else {
                ChartHelper.updateLineChart(lineChart, entries)
            }
        })

        return root
    }
}
