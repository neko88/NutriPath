package com.group35.nutripath.homemenu.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            val intent = Intent(this, HomeMainActivity::class.java)
            startActivity(intent)
        }

    }
}