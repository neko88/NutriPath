package com.group35.nutripath

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.group35.nutripath.api.themealdb.MealActivity
import com.group35.nutripath.databinding.ActivityMainBinding
import com.group35.nutripath.util.BarcodeScannerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Set up the button to open MapSearch activity
        binding.openMapButton.setOnClickListener {
            val intent = Intent(this, MapSearch::class.java)
            startActivity(intent)
        }
        binding.openBarcodeScannerButton.setOnClickListener{
            val intent = Intent(this, BarcodeScannerActivity::class.java)
            startActivity(intent)
        }
        binding.mealSuggestionButton.setOnClickListener{
            val intent = Intent(this, MealActivity::class.java)
            startActivity(intent)
        }
    }


}