package com.group35.nutripath.homemenu.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.group35.nutripath.MainActivity
import com.group35.nutripath.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val isProfileComplete = sharedPreferences.getBoolean("isProfileComplete", false)

            val intent = if (isProfileComplete) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, ProfileSetupActivity::class.java)
            }
            Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }
}