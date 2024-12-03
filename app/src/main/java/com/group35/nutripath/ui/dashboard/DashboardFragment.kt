package com.group35.nutripath.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Contacts
import android.provider.Contacts.Intents.UI
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.group35.nutripath.databinding.FragmentDashboardBinding
import com.group35.nutripath.ui.database.Consumption
import com.group35.nutripath.ui.database.ConsumptionDao
import com.group35.nutripath.ui.database.ConsumptionDatabase
import com.group35.nutripath.ui.database.ConsumptionListAdapter
import com.group35.nutripath.ui.database.ConsumptionRepository
import com.group35.nutripath.ui.database.ConsumptionViewModel
import com.group35.nutripath.ui.database.ConsumptionViewModelFactory
import com.group35.nutripath.ui.database.FoodItem
import com.group35.nutripath.ui.database.FoodItemListAdapter
import com.group35.nutripath.util.Globals
import com.group35.nutripath.utils.ChartHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/*
 * Nov 23, 2024 - Modified by Cameron Yee-Ping
 *
 * TODO: add auto-complete functionality (if time permits)
 * This module contains functionality for adding new food items to the database
 * Allows the user to choose a food item that already exists in the db to add OR to create a new
 * food item to the database
 */
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var addFoodButton: Button
    private lateinit var foodListView: ListView


    private lateinit var foodListAdapter: FoodItemListAdapter
    private lateinit var foodArrayList: ArrayList<FoodItem>

    private lateinit var consumptionDB: ConsumptionDatabase
    private lateinit var consumptionDao: ConsumptionDao
    private lateinit var consumptionRepository: ConsumptionRepository

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var consumptionViewModelFactory: ConsumptionViewModelFactory
    private val consumptionViewModel: ConsumptionViewModel by activityViewModels() { consumptionViewModelFactory }

    private lateinit var calendar: Calendar


    private lateinit var dayInterval: Pair<Long, Long>
    private lateinit var monthInterval: Pair<Long, Long>


    private lateinit var caloriesPieChart: PieChart
    private lateinit var carbsPieChart: PieChart
    private lateinit var proteinPieChart: PieChart
    private lateinit var fatsPieChart: PieChart
    private lateinit var monthlySpendingChart: PieChart



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initialize()
        consumptionViewModel.allConsumptionLiveData.observe(viewLifecycleOwner){ it ->
            println("debug: Dashboard fragment: all consumption $it")
            consumptionViewModel.updateMacros(dayInterval.first, dayInterval.second)
            consumptionViewModel.getTotalSpendingForMonth(monthInterval.first, monthInterval.second)
        }
        consumptionViewModel.dailyCalories.observe(viewLifecycleOwner) { calories ->
            // Assuming you also fetch protein, fats, etc., similarly
            lifecycleScope.launch {
                ChartHelper.updateMacrosPieChart(caloriesPieChart, Globals().getCalories(requireContext()).toFloat(), calories.toFloat())
            }
        }
        consumptionViewModel.dailyFats.observe(viewLifecycleOwner) { calories ->
            // Assuming you also fetch protein, fats, etc., similarly
            lifecycleScope.launch {
                ChartHelper.updateMacrosPieChart(fatsPieChart, Globals().getFats(requireContext()).toFloat(), calories.toFloat())
            }
        }
        consumptionViewModel.monthlySpending.observe(viewLifecycleOwner) { it ->
            lifecycleScope.launch {
                ChartHelper.updateFoodBudgetPieChart(monthlySpendingChart, Globals().getBudget(requireContext()).toFloat(), it.toFloat())
            }
        }
        consumptionViewModel.dailyCarbs.observe(viewLifecycleOwner) { it ->
            lifecycleScope.launch {
                ChartHelper.updateMacrosPieChart(carbsPieChart, Globals().getCarbs(requireContext()).toFloat(), it.toFloat())
            }
        }
        consumptionViewModel.dailyProtein.observe(viewLifecycleOwner){it ->
            lifecycleScope.launch {
                ChartHelper.updateMacrosPieChart(proteinPieChart, Globals().getProtein(requireContext()).toFloat(), it.toFloat())
            }
        }
        consumptionViewModel.allFoodItemLiveData.observe(viewLifecycleOwner){ it ->
            println("debug: Dashboard fragment: all food $it")
            foodListAdapter.replace(it)
            foodListAdapter.notifyDataSetChanged()
        }


        foodListView.setOnItemClickListener{ _, _, pos, _ ->
            // add a dialog perhaps
            val selected = foodListAdapter.getItem(pos) as FoodItem

            // create consumption instance with given food item
            val cons = Consumption(foodId = selected.id, date = calendar.timeInMillis)
//            cons.foodId = selected.id
//
//            cons.date = System.currentTimeMillis()
            println("debug: Dashboard fragment: selected = $selected")
            println("debug: Dashboard fragment: cons = $cons")
            consumptionViewModel.insertConsumption(cons)

            Toast.makeText(requireContext(), "Tracked consumption of ${selected.name}", Toast.LENGTH_SHORT).show()

        }
        addFoodButton.setOnClickListener {
            val intent = Intent(requireContext(), FoodEntryActivity::class.java)
            startActivity(intent)
        }

        binding.customToolbar.toolbarTitle.text = SimpleDateFormat("EEE MMM d yyyy", Locale.getDefault()).format(calendar.time)

        binding.customToolbar.btnNextDay.setOnClickListener(){
            invalidateCharts()
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            binding.customToolbar.toolbarTitle.text = SimpleDateFormat("EEE MMM d yyyy", Locale.getDefault()).format(calendar.time)
            dayInterval = Globals().getDateInterval(calendar.timeInMillis)
            println("debug: time = ${calendar.time}")
            monthInterval = Globals().getMonthInterval(calendar.timeInMillis)
            consumptionViewModel.updateMacros(dayInterval.first, dayInterval.second)
        }
        binding.customToolbar.btnPreviousDay.setOnClickListener(){
            invalidateCharts()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            binding.customToolbar.toolbarTitle.text = SimpleDateFormat("EEE MMM d yyyy", Locale.getDefault()).format(calendar.time)
            println("debug: time = ${calendar.time}")
            dayInterval = Globals().getDateInterval(calendar.timeInMillis)
            if (monthInterval != Globals().getMonthInterval(calendar.timeInMillis)){
                monthInterval = Globals().getMonthInterval(calendar.timeInMillis)
                consumptionViewModel.getTotalSpendingForMonth(monthInterval.first, monthInterval.second)
            }
            consumptionViewModel.updateMacros(dayInterval.first, dayInterval.second)
        }

        return root
    }
    private fun invalidateCharts(){
        caloriesPieChart.apply { invalidate() }
        fatsPieChart.apply { invalidate() }
        carbsPieChart.apply { invalidate()}
        proteinPieChart.apply { invalidate() }
        monthlySpendingChart.apply { invalidate() }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initConsumptionDB(){
        consumptionDB = ConsumptionDatabase.getInstance(requireActivity())
        consumptionDao = consumptionDB.consumptionDao
        consumptionRepository = ConsumptionRepository(consumptionDao)
        consumptionViewModelFactory = ConsumptionViewModelFactory(consumptionRepository)
    }
    private fun initialize(){
        // initialize all variables (used when view is created)
        addFoodButton = binding.dashboardAddItemButton
        foodListView = binding.dashboardFoodItemList

        foodArrayList = ArrayList()
        foodListAdapter = FoodItemListAdapter(requireContext(), foodArrayList)
        foodListView.adapter = foodListAdapter
        initConsumptionDB()
        calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        fatsPieChart = binding.fatsChart
        carbsPieChart = binding.carbsChart
        caloriesPieChart = binding.calChart
        proteinPieChart = binding.proteinChart
        monthlySpendingChart = binding.monthlySpendChart

        ChartHelper.setupEmptyPieChart(fatsPieChart)
        ChartHelper.setupEmptyPieChart(carbsPieChart)
        ChartHelper.setupEmptyPieChart(caloriesPieChart)
        ChartHelper.setupEmptyPieChart(proteinPieChart)
        ChartHelper.setupEmptyPieChart(monthlySpendingChart)


        dayInterval = Globals().getDateInterval(calendar.timeInMillis)
        monthInterval = Globals().getMonthInterval(calendar.timeInMillis)

    }

}