package com.group35.nutripath.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.group35.nutripath.R
import com.group35.nutripath.databinding.ActivityAnalyticsBinding

class AnalyticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = supportFragmentManager.findFragmentById(R.id.homeFragmentContainer)

    }
}