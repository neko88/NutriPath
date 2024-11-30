package com.group35.nutripath.homemenu.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.group35.nutripath.databinding.ActivityDetailHomeBinding
import com.group35.nutripath.homemenu.adapters.ColorAdapter
import com.group35.nutripath.homemenu.dataobject.BottomDataObject
import com.group35.nutripath.homemenu.adapters.TopObjectViewAdapter
import com.group35.nutripath.homemenu.dataobject.ItemObject
import com.group35.nutripath.homemenu.dataobject.TopDataObject
import com.group35.nutripath.homemenu.helper.SelectionManager

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHomeBinding
    private lateinit var item: ItemObject
    private var numberOrder = 1
    private lateinit var managmentCart: SelectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = SelectionManager(this)

        getBundle()
        Banners()
        initColorList()

    }

    private fun initColorList() {
        val colorList = ArrayList<String>()
        for (imageUrl in item.picUrl) {
            colorList.add(imageUrl)
        }
        binding.colorList.adapter = ColorAdapter(colorList)
        binding.colorList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun Banners() {
        val sliderItems = ArrayList<TopDataObject>()
        for (imageUrl in item.picUrl) {
            sliderItems.add(TopDataObject(imageUrl))
        }

        binding.slider.adapter = TopObjectViewAdapter(sliderItems, binding.slider)
        binding.slider.clipToPadding = false
        binding.slider.clipChildren = false
        binding.slider.offscreenPageLimit = 1
        binding.slider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }

        binding.slider.setPageTransformer(compositePageTransformer)
        if (sliderItems.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.slider)
        }
    }

    private fun getBundle() {
        item = intent.getParcelableExtra("object")!!

        binding.titleTxt.text = item.title
        binding.descriptionTxt.text = item.description
        binding.priceTxt.text = "$" + item.price
        binding.ratingTxt.text = "${item.rating} Rating"
        binding.addToCartBtn.setOnClickListener {
            item.numberInCart = numberOrder
            managmentCart.insertItem(item)
        }

        binding.backBtn.setOnClickListener { finish() }
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this@DetailActivity, SelectionActivity::class.java))
        }
    }
}