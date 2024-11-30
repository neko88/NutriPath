package com.group35.nutripath.homemenu.demo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

class CartActivity : AppCompatActivity() {
    private lateinit var binding: CartActivityBinding
    private lateinit var managmentCart: ManagmentCart
    private var tax: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)

        setVariable()
        calculateCart()
        initCartList()

    }

    private fun initCartList() {
        with(binding) {
            emptyTxt.visibility =
                if (managmentCart.getListCart().isEmpty()) View.VISIBLE else View.GONE
            scrollView2.visibility =
                if (managmentCart.getListCart().isEmpty()) View.GONE else View.VISIBLE
        }

        binding.viewCart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.viewCart.adapter=
            CartAdapter(managmentCart.getListCart(),this,object : ChangeNumberItemsListener {
            override fun onChanged() {
            calculateCart()
            }

        })
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 10.0
        tax = Math.round((managmentCart.getTotalFee() * percentTax) * 100) / 100.0
        val total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100
        val itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100

        with(binding) {
            totalFeeTxt.text = "$$itemTotal"
            taxTxt.text = "$$tax"
            deliveryTxt.text = "$$delivery"
            totalTxt.text = "$$total"
        }
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }
}