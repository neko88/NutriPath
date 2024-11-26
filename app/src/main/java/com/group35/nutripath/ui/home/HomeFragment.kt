package com.group35.nutripath.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.group35.nutripath.R
import com.group35.nutripath.api.openfoodfacts.ProductInfoActivity
import com.group35.nutripath.api.themealdb.MealActivity
import com.group35.nutripath.ui.database.Consumption
import com.group35.nutripath.ui.database.ConsumptionDao
import com.group35.nutripath.ui.database.ConsumptionDatabase
import com.group35.nutripath.ui.database.ConsumptionRepository
import com.group35.nutripath.ui.database.ConsumptionViewModel
import com.group35.nutripath.ui.database.ConsumptionViewModelFactory
import com.group35.nutripath.util.Globals
import com.group35.nutripath.utils.ChartHelper
import com.group35.nutripath.utils.DialogHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
/*
 * November 25 2024 - Modified by Cameron Yee-Ping
 *                  - updated fragment to work with the new food database implementation
 */
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var pieChart: PieChart
    private lateinit var lineChart: LineChart

    private lateinit var consumptionDB: ConsumptionDatabase
    private lateinit var consumptionViewModelFactory: ConsumptionViewModelFactory
    private val consumptionViewModel: ConsumptionViewModel by activityViewModels { consumptionViewModelFactory }
    private lateinit var consumptionDao: ConsumptionDao
    private lateinit var consumptionRepository: ConsumptionRepository
    private var caloriesForDay: Double? = 0.0
    private var carbsForDay: Double? = 0.0
    private var proteinForDay: Double? = 0.0
    private var fatsForDay: Double? = 0.0
    private var sugarsForDay: Double? = 0.0
    private var spendingForMonth: Double? = 0.0
    private lateinit var dayInterval: Pair<Long, Long>
    private lateinit var monthInterval: Pair<Long, Long>

    private var consumptionList: List<Consumption> = listOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        pieChart = root.findViewById(R.id.pieChart)
        lineChart = root.findViewById(R.id.lineChart)
        val date = System.currentTimeMillis()
        dayInterval = Globals().getDateInterval(date)
        monthInterval = Globals().getMonthInterval(date)
        println("debug: date = $date, dayInterval ${dayInterval.first} to ${dayInterval.second}")
        println("debug: month = ${monthInterval.first} ${monthInterval.second}")
        val c0 = Calendar.getInstance()
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        c0.timeInMillis = date
        c1.timeInMillis = dayInterval.first
        c2.timeInMillis = dayInterval.second

        println("debug: Home fragment: date = ${c0.time} interval = ${c1.time} to ${c2.time}")

        c1.timeInMillis = monthInterval.first
        c2.timeInMillis = monthInterval.second

        println("debug: Home fragment: month interval = ${c1.time} to ${c2.time}")
        initConsumptionDB()
        //consumptionViewModel.deleteAll()
        consumptionViewModel.allConsumptionLiveData.observe(requireActivity()){ it ->
            println("debug: Home fragment: all consumption: $it")
            consumptionList = it
            CoroutineScope(IO).launch {
                caloriesForDay = consumptionViewModel.getDailyCalories(dayInterval.first, dayInterval.second).value
                proteinForDay = consumptionViewModel.getDailyProtein(dayInterval.first, dayInterval.second).value
                fatsForDay = consumptionViewModel.getDailyFats(dayInterval.first, dayInterval.second).value
                sugarsForDay = consumptionViewModel.getDailySugars(dayInterval.first, dayInterval.second).value
                carbsForDay = consumptionViewModel.getDailyCarbs(dayInterval.first, dayInterval.second).value
                spendingForMonth = consumptionViewModel.getTotalSpendingForMonth(monthInterval.first, monthInterval.second).value ?: 0.0
                println("debug: home fragment $caloriesForDay $proteinForDay $fatsForDay $sugarsForDay $carbsForDay $spendingForMonth")
                withContext(Main){
                    homeViewModel.setExpense(spendingForMonth!!.toFloat())
                }

            }
        }


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
        // only for testing - can remove later - nat
        root.findViewById<Button>(R.id.recipesButton).setOnClickListener {
            val intent = Intent(requireContext(), MealActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        // only for testing - can remove later - nat
        root.findViewById<Button>(R.id.barcodeButton).setOnClickListener {
            val intent = Intent(requireContext(), ProductInfoActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        // Observe the LiveData to populate charts, including initial empty data
        homeViewModel.budgetAllocations.observe(viewLifecycleOwner) { allocations ->
            if (allocations.isEmpty()) {
                ChartHelper.setupEmptyPieChart(pieChart)
            } else {
                val spent = allocations.firstOrNull { it.label == "Spent" }?.value ?: 0f
                val remaining = allocations.firstOrNull { it.label == "Remaining" }?.value ?: 0f
                val exceeding = allocations.firstOrNull { it.label == "Exceeding" }?.value ?: 0f
                ChartHelper.updateFoodBudgetPieChart(pieChart, spent + remaining, spent + exceeding)
            }
        }

        homeViewModel.expenseEntries.observe(viewLifecycleOwner, Observer { entries ->
            if (entries.isEmpty()) {
                ChartHelper.setupEmptyLineChart(lineChart)
            } else {
                ChartHelper.updateLineChart(lineChart, entries)
            }
        })

        return root
    }

    private fun initConsumptionDB(){
        consumptionDB = ConsumptionDatabase.getInstance(requireActivity())
        consumptionDao = consumptionDB.consumptionDao
        consumptionRepository = ConsumptionRepository(consumptionDao)
        consumptionViewModelFactory = ConsumptionViewModelFactory(consumptionRepository)
    }
    // temporary functions to get aggregates

}
