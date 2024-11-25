package com.group35.nutripath.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.group35.nutripath.databinding.FragmentDashboardBinding
import com.group35.nutripath.ui.database.Consumption
import com.group35.nutripath.ui.database.ConsumptionDao
import com.group35.nutripath.ui.database.ConsumptionDatabase
import com.group35.nutripath.ui.database.ConsumptionListAdapter
import com.group35.nutripath.ui.database.ConsumptionRepository
import com.group35.nutripath.ui.database.ConsumptionViewModel
import com.group35.nutripath.ui.database.ConsumptionViewModelFactory
import com.group35.nutripath.ui.database.FoodItem
import com.group35.nutripath.ui.database.FoodItemDao
import com.group35.nutripath.ui.database.FoodItemDatabase
import com.group35.nutripath.ui.database.FoodItemListAdapter
import com.group35.nutripath.ui.database.FoodItemRepository
import com.group35.nutripath.ui.database.FoodItemViewModel
import com.group35.nutripath.ui.database.FoodItemViewModelFactory
import kotlinx.coroutines.launch

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

    private lateinit var foodDB: FoodItemDatabase
    private lateinit var foodDao: FoodItemDao
    private lateinit var foodRepository: FoodItemRepository
    private lateinit var foodListAdapter: FoodItemListAdapter
    private lateinit var foodArrayList: ArrayList<FoodItem>

    private lateinit var foodViewModelFactory: FoodItemViewModelFactory
    private val foodViewModel: FoodItemViewModel by activityViewModels() { foodViewModelFactory }

    private lateinit var consumptionDB: ConsumptionDatabase
    private lateinit var consumptionDao: ConsumptionDao
    private lateinit var consumptionRepository: ConsumptionRepository

    private lateinit var consumptionViewModelFactory: ConsumptionViewModelFactory
    private val consumptionViewModel: ConsumptionViewModel by activityViewModels() { consumptionViewModelFactory }



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
        }
        foodViewModel.allFoodItemLiveData.observe(viewLifecycleOwner){ it ->
            println("debug: Dashboard fragment: all food $it")
            foodListAdapter.replace(it)
            foodListAdapter.notifyDataSetChanged()
        }
        foodListView.setOnItemClickListener{ _, _, pos, _ ->
            // add a dialog perhaps
            val selected = foodListAdapter.getItem(pos) as FoodItem

            // create consumption instance with given food item
            val cons = Consumption(foodId = selected.id, date = System.currentTimeMillis())
//            cons.foodId = selected.id
//
//            cons.date = System.currentTimeMillis()
            println("debug: Dashboard fragment: selected = $selected")
            println("debug: Dashboard fragment: cons = $cons")
            lifecycleScope.launch {
                consumptionViewModel.insert(cons)

            }

        }
        addFoodButton.setOnClickListener {
            val intent = Intent(requireContext(), FoodEntryActivity::class.java)
            startActivity(intent)
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun initFoodDB(){
        foodDB = FoodItemDatabase.getInstance(requireActivity())
        foodDao = foodDB.foodItemDao
        foodRepository = FoodItemRepository(foodDao)
        foodViewModelFactory = FoodItemViewModelFactory(foodRepository)
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

        initFoodDB()
        initConsumptionDB()

    }

}