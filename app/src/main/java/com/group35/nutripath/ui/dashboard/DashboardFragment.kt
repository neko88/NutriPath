package com.group35.nutripath.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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

    private var caloriesForDay: Double? = 0.0
    private var carbsForDay: Double? = 0.0
    private var proteinForDay: Double? = 0.0
    private var fatsForDay: Double? = 0.0
    private var sugarsForDay: Double? = 0.0
    private var spendingForMonth: Double? = 0.0
    private lateinit var dayInterval: Pair<Long, Long>
    private lateinit var monthInterval: Pair<Long, Long>


    private lateinit var caloriesPieChart: PieChart
    private lateinit var carbsPieChart: PieChart
    private lateinit var proteinPieChart: PieChart
    private lateinit var fatsPieChart: PieChart



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
        //foodViewModel.deleteAll()
        consumptionViewModel.allConsumptionLiveData.observe(viewLifecycleOwner){ it ->
            println("debug: Dashboard fragment: all consumption $it")
            println("debug: Home fragment: all consumption: $it")
            CoroutineScope(IO).launch {
                getConsumptionData()
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

        }
        addFoodButton.setOnClickListener {
            val intent = Intent(requireContext(), FoodEntryActivity::class.java)
            startActivity(intent)
        }

        binding.customToolbar.toolbarTitle.text = SimpleDateFormat("EEE MMM d yyyy", Locale.getDefault()).format(calendar.time)

        binding.customToolbar.btnNextDay.setOnClickListener(){
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            binding.customToolbar.toolbarTitle.text = SimpleDateFormat("EEE MMM d yyyy", Locale.getDefault()).format(calendar.time)
            dayInterval = Globals().getDateInterval(calendar.timeInMillis)
            monthInterval = Globals().getMonthInterval(calendar.timeInMillis)
            getConsumptionData()
        }
        binding.customToolbar.btnPreviousDay.setOnClickListener(){
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            binding.customToolbar.toolbarTitle.text = SimpleDateFormat("EEE MMM d yyyy", Locale.getDefault()).format(calendar.time)

            dayInterval = Globals().getDateInterval(calendar.timeInMillis)
            monthInterval = Globals().getMonthInterval(calendar.timeInMillis)
            getConsumptionData()
        }

        return root
    }
    private fun getConsumptionData(){
        lifecycleScope.launch {
            caloriesForDay = consumptionViewModel.getDailyCalories(dayInterval.first, dayInterval.second).value ?: 0.0
            proteinForDay = consumptionViewModel.getDailyProtein(dayInterval.first, dayInterval.second).value ?: 0.0
            fatsForDay = consumptionViewModel.getDailyFats(dayInterval.first, dayInterval.second).value?: 0.0
            sugarsForDay = consumptionViewModel.getDailySugars(dayInterval.first, dayInterval.second).value?: 0.0
            carbsForDay = consumptionViewModel.getDailyCarbs(dayInterval.first, dayInterval.second).value?: 0.0
            spendingForMonth = consumptionViewModel.getTotalSpendingForMonth(monthInterval.first, monthInterval.second).value ?: 0.0
            println("debug: home fragment $caloriesForDay $proteinForDay $fatsForDay $sugarsForDay $carbsForDay $spendingForMonth")
            withContext(Main){
                ChartHelper.updateMacrosPieChart(caloriesPieChart, 2000f, caloriesForDay!!.toFloat())
                ChartHelper.updateMacrosPieChart(fatsPieChart, 34f, fatsForDay!!.toFloat())
                ChartHelper.updateMacrosPieChart(carbsPieChart, 225f, carbsForDay!!.toFloat())
                ChartHelper.updateMacrosPieChart(proteinPieChart, 55f, proteinForDay!!.toFloat())
            }
        }

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

        ChartHelper.setupEmptyPieChart(fatsPieChart)
        ChartHelper.setupEmptyPieChart(carbsPieChart)
        ChartHelper.setupEmptyPieChart(caloriesPieChart)
        ChartHelper.setupEmptyPieChart(proteinPieChart)


        dayInterval = Globals().getDateInterval(calendar.timeInMillis)
        monthInterval = Globals().getMonthInterval(calendar.timeInMillis)

    }

}