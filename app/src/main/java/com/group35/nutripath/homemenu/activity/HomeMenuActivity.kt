package com.group35.nutripath.homemenu.activity
import com.group35.nutripath.databinding.ActivityHomeMenuBinding
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.firebase.FirebaseApp
import com.group35.nutripath.homemenu.adapters.BottomObjectViewAdapter
import com.group35.nutripath.homemenu.helper.MainHomeViewModel
import com.group35.nutripath.homemenu.adapters.MiddleObjectViewAdapter
import com.group35.nutripath.homemenu.adapters.TopObjectViewAdapter
import com.group35.nutripath.homemenu.dataobject.ItemObject
import com.group35.nutripath.homemenu.dataobject.TopDataObject

class HomeMenuActivity : AppCompatActivity() {
    private val homePageViewModel = MainHomeViewModel()
    private lateinit var binding: ActivityHomeMenuBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        initBanners()
        middleBanner()
        bottomBanner()
    }

    // top banner
    private fun initBanners() {
        binding.progressBarBanner.visibility = View.VISIBLE
        homePageViewModel.viewObjectTop.observe(this, Observer {
            topBanner(it)
            binding.progressBarBanner.visibility = View.GONE
        })
     //   homePageViewModel.loadTopObjectViewBanner()
    }

    private fun topBanner(it: List<TopDataObject>) {
        // adapter for first slider
        binding.topObjectViewPager.adapter = TopObjectViewAdapter(it, binding.topObjectViewPager)
        binding.topObjectViewPager.clipToPadding = false
        binding.topObjectViewPager.clipChildren = false
        binding.topObjectViewPager.offscreenPageLimit = 3
        binding.topObjectViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }

        binding.topObjectViewPager.setPageTransformer(compositePageTransformer)
        if (it.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.topObjectViewPager)
        }
    }
    // middle view model
    private fun middleBanner() {
        binding.progressBarCategory.visibility = View.VISIBLE
        homePageViewModel.viewObjectMiddle.observe(this, Observer {
            binding.midRecyclerViewModel.layoutManager =
                LinearLayoutManager(this@HomeMenuActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.midRecyclerViewModel.adapter = MiddleObjectViewAdapter(it)
            binding.progressBarCategory.visibility = View.GONE
        })
      //  homePageViewModel.loadMiddleObjectViewBanner() replace for firebase
    }

    private fun bottomBanner() {
        binding.progressBarRecommendation.visibility = View.VISIBLE
        homePageViewModel.viewObjectBottom.observe(this, Observer {
            binding.viewRecommendation.layoutManager = GridLayoutManager(this, 2)
            binding.viewRecommendation.adapter = BottomObjectViewAdapter(it)
            binding.progressBarRecommendation.visibility = View.GONE
        })
    //    homePageViewModel.loadBottomObjectViewBanner()
    }
}