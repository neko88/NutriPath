package com.group35.nutripath.api.openfoodfacts

import FoodViewModel
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.group35.nutripath.R
import com.group35.nutripath.databinding.ActivityFoodProductBinding
import kotlinx.coroutines.launch

class FoodProductActivity : AppCompatActivity() {
    private lateinit var barcodeInput: EditText
    private lateinit var barcodeTextView: TextView
    private lateinit var findFoodButton: Button
    private lateinit var binding: ActivityFoodProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_product)


        binding = ActivityFoodProductBinding.inflate(layoutInflater)
        barcodeInput = binding.barcodeInput
        barcodeTextView = binding.barcodeInputTextView
        findFoodButton = findViewById(R.id.findFoodButton)

        val foodViewModel: FoodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)

        findFoodButton.setOnClickListener {
            val barcode = barcodeInput.text.toString()
            lifecycleScope.launch {
                foodViewModel.findFoodByBarcode(barcode)
            }
        }
    }

}