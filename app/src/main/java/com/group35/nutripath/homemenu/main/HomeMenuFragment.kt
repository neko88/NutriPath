package com.group35.nutripath.homemenu.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.group35.nutripath.R
import com.group35.nutripath.api.edamam.EdamamActivity
import com.group35.nutripath.api.edamam.EdamamApi
import com.group35.nutripath.api.edamam.EdamamDAO
import com.group35.nutripath.api.edamam.EdamamRepository
import com.group35.nutripath.api.openfoodfacts.OpenFoodFactsActivity
import com.group35.nutripath.api.themealdb.MealActivity
import com.group35.nutripath.databinding.FragmentHomeMenuBinding
import com.group35.nutripath.homemenu.adapters.BottomObjectViewAdapter
import com.group35.nutripath.homemenu.adapters.MiddleObjectViewAdapter
import com.group35.nutripath.homemenu.adapters.TopObjectViewAdapter
import com.group35.nutripath.homemenu.dataobject.TopDataObject
import com.group35.nutripath.homemenu.main.MainHomeViewModel
import com.group35.nutripath.ui.home.AnalyticsActivity
import com.group35.nutripath.util.BarcodeScannerActivity

class HomeMenuFragment : Fragment() {

    private lateinit var binding: FragmentHomeMenuBinding
    private val homePageViewModel = MainHomeViewModel()
    private val sliderHandler = Handler(Looper.getMainLooper())
    private lateinit var sliderAdapter: TopObjectViewAdapter
    private val repository = EdamamRepository()
    private val appId = "d45fcd57" // Replace with your actual App ID
    private val appKey = "2f4ad82db15fbca9481a538196db68dd" // Replace with your actual App Key

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeMenuBinding.inflate(inflater, container, false)

        setupSlider()
        setupAutoSlide() // Auto-slide functionality
        scheduleRandomRecipeUpdate() // Update the first slide with a random recipe every minute

        homePageViewModel.loadTopBannerItems()
        homePageViewModel.loadMiddleBannerItems()
        homePageViewModel.loadBottomBannerItems()

        middleBanner()
        bottomBanner()

        binding.analyticsButton.setOnClickListener {
            val intent = Intent(requireContext(), AnalyticsActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.recipeButton.setOnClickListener {
            val intent = Intent(requireContext(), MealActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.searchProductButton.setOnClickListener {
            val intent = Intent(requireContext(), OpenFoodFactsActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.barcodeButton.setOnClickListener {
            val intent = Intent(requireContext(),
                BarcodeScannerActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }


        return binding.root
    }


    private val staticSlides = mutableListOf(
        TopDataObject(R.drawable.np_banner1, "Static Slide 1"),
        TopDataObject(R.drawable.np_banner2, "Static Slide 2"),
        TopDataObject(R.drawable.np_banner3, "Static Slide 3")
    )

    override fun onDestroyView() {
        super.onDestroyView()
        sliderHandler.removeCallbacksAndMessages(null) // Stop updates when fragment is destroyed
    }

    private fun setupSlider() {
        // Add a placeholder for the dynamic recipe slide
        val initialSlides = mutableListOf(
            TopDataObject(R.drawable.np_banner2, "Random Recipe")
        )
        initialSlides.addAll(staticSlides) // Add static slides after the dynamic recipe slide

        sliderAdapter = TopObjectViewAdapter(initialSlides, binding.topObjectViewPagerSlider)
        binding.topObjectViewPagerSlider.adapter = sliderAdapter

        // ViewPager2 settings
        binding.topObjectViewPagerSlider.apply {
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun setupAutoSlide() {
        val autoSlideRunnable = object : Runnable {
            override fun run() {
                val currentItem = binding.topObjectViewPagerSlider.currentItem
                val totalItems = sliderAdapter.itemCount
                binding.topObjectViewPagerSlider.currentItem = (currentItem + 1) % totalItems
                sliderHandler.postDelayed(this, 3000) // Slide every 3 seconds
            }
        }
        sliderHandler.post(autoSlideRunnable)

        // Pause auto-slide when the user interacts with the slider
        binding.topObjectViewPagerSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                sliderHandler.removeCallbacks(autoSlideRunnable)
                sliderHandler.postDelayed(autoSlideRunnable, 3000)
            }
        })
    }

    private fun scheduleRandomRecipeUpdate() {
        val recipeUpdateHandler = Handler(Looper.getMainLooper())
        val updateRunnable = object : Runnable {
            override fun run() {
                fetchRandomRecipe { recipeSlide ->
                    sliderAdapter.updateSingleItem(0, recipeSlide) // Update only the first slide
                }
                recipeUpdateHandler.postDelayed(this, 60000) // Schedule next update after 1 minute
            }
        }
        recipeUpdateHandler.post(updateRunnable)
    }

    private fun fetchRandomRecipe(onSuccess: (TopDataObject) -> Unit) {
        val randomQuery =  homePageViewModel.getRandomFoodTagList()?.random()

        if (randomQuery != null) {
            repository.searchRecipes(randomQuery, appId, appKey, from = 0, to = 1, { recipes ->
                if (recipes.isNotEmpty()) {
                    val recipe = recipes.first()
                    val recipeSlide = TopDataObject(
                        resource = recipe.image,
                        label = recipe.label
                    )
                    onSuccess(recipeSlide)
                }
            }, { error ->
                Log.e("HomeMenuFragment", "Error fetching recipe: ${error.message}")
            })
        }
    }



    private fun middleBanner() {
        homePageViewModel.viewObjectMiddle.observe(viewLifecycleOwner, Observer {
            binding.midRecyclerViewModel.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.midRecyclerViewModel.adapter = MiddleObjectViewAdapter(it)
        })
    }

    private fun bottomBanner() {
        homePageViewModel.viewObjectBottom.observe(viewLifecycleOwner, Observer {
            binding.bottomObjectViewRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.bottomObjectViewRecycler.adapter = BottomObjectViewAdapter(it)
        })
    }



}
