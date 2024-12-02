package com.group35.nutripath.homemenu.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.group35.nutripath.NutriPathApplication
import com.group35.nutripath.NutriPathApplicationViewModel
import com.group35.nutripath.R
import com.group35.nutripath.api.edamam.EdamamRepository
import com.group35.nutripath.api.edamam.EdamamViewModel
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
import kotlinx.coroutines.launch

class HomeMenuFragment : Fragment() {

    private lateinit var binding: FragmentHomeMenuBinding
    private val homePageViewModel = MainHomeViewModel()
    private val edamamViewModel = EdamamViewModel()
    private val sliderHandler = Handler(Looper.getMainLooper())
    private lateinit var sliderAdapter: TopObjectViewAdapter
    private val repository = EdamamRepository()
    private lateinit var appViewModel: NutriPathApplicationViewModel
    private var bannerRotationCount = 0

    private lateinit var bannerSlideCollection: MutableList<TopDataObject>
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeMenuBinding.inflate(inflater, container, false)
        appViewModel =
            (requireActivity().application as NutriPathApplication).nutriPathApplicationViewModel

        homePageViewModel.loadTopBannerItems()
        homePageViewModel.loadMiddleBannerItems()
        homePageViewModel.loadBottomBannerItems()

        middleBanner()
        bottomBanner()

        setupName()
        setupBannerCollection()
        setupSlider()
        setupAutoSlide()
        scheduleApiUpdates()

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
            val intent = Intent(
                requireContext(),
                BarcodeScannerActivity::class.java
            )
            Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.imageView2.setOnClickListener {
            findNavController().navigate(
                R.id.navigation_settings,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_home_menu, true)
                    .build()
            )
        }
        binding.imageView3.setOnClickListener {
            findNavController().navigate(R.id.homeMenu_to_profile)
        }

        /*
        // observe recipes and update the slider
        edamamViewModel.recipesLiveData.observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNotEmpty()) {
                val recipe = recipes.first()
                val recipeSlide = TopDataObject(
                    resource = recipe.image,
                    label = recipe.label,
                    url = recipe.url
                )
                sliderAdapter.updateSingleItem(0, recipeSlide)
            }
        }*/
        return binding.root
    }


    private fun setupBannerCollection() {
        bannerSlideCollection =
            homePageViewModel.topObjectInitialSlides as MutableList<TopDataObject>
        homePageViewModel.topObjectSliderItems.observe(viewLifecycleOwner) { it ->
            bannerSlideCollection = it as MutableList<TopDataObject>
        }
    }

    private fun setupSlider() {
        sliderAdapter =
            TopObjectViewAdapter(bannerSlideCollection, binding.topObjectViewPagerSlider)
        binding.topObjectViewPagerSlider.adapter = sliderAdapter

        // ViewPager2 settings
        binding.topObjectViewPagerSlider.apply {
            offscreenPageLimit = 3 // seconds to switch
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun setupName() {
        sharedPreferences = requireActivity().getSharedPreferences("profile", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "")

        if (name != "") {
            binding.textView4.text = name
        }
    }

    private fun changeRecipeBanner() {
        if (bannerRotationCount >= bannerSlideCollection.size) {
            bannerRotationCount = 0
        } else { bannerRotationCount += 1 }
        fetchRandomRecipe { recipeSlide ->
            sliderAdapter.updateSingleItem(bannerRotationCount, recipeSlide)
        }
    }

    private fun setupAutoSlide() {
        val autoSlideRunnable = object : Runnable {
            override fun run() {
                if (isAdded && view != null) {
                    binding.topObjectViewPagerSlider.apply {
                        currentItem = if (adapter != null) {
                            (currentItem + 1) % adapter!!.itemCount
                        } else { 0 }
                    }
                }
            }
        }
        sliderHandler.post(autoSlideRunnable)
        // Pause auto-slide when user interacts
        binding.topObjectViewPagerSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                sliderHandler.removeCallbacks(autoSlideRunnable)
                sliderHandler.postDelayed(autoSlideRunnable, 3000) // Resume after 3 seconds
            }
        })
    }

    // api calls every 1 minute due to restrictions.
    private fun scheduleApiUpdates() {
        val apiUpdateHandler = Handler(Looper.getMainLooper())
        val apiUpdateRunnable = object : Runnable {
            override fun run() {
                if (isAdded && view != null) {
                    if (appViewModel.edamamApiCallOK()) {
                        changeRecipeBanner()
                    } else {
                        Log.d("HomeMenuFragment", "API call skipped since it's been less than a minute.")
                    }
                    apiUpdateHandler.postDelayed(this, 60000)
                }
            }
        }
        apiUpdateHandler.post(apiUpdateRunnable)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        sliderHandler.removeCallbacksAndMessages(null) // Stop all updates
    }

    private val appId = "d45fcd57"
    private val appKey = "2f4ad82db15fbca9481a538196db68dd"

    private fun fetchRandomRecipe(onSuccess: (TopDataObject) -> Unit) {
        val randomQuery = edamamViewModel.getRandomFoodTagList()
        if (randomQuery != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val result = repository.searchRecipes(
                        query = randomQuery,
                        appId = appId,
                        appKey = appKey,
                        //       cuisineType= "Asian",
                        //       mealType = "",
                        //       dishType = "",
                        from = 0,
                        to = 1
                    )
                    result.fold(onSuccess = { recipes ->
                        if (recipes.isNotEmpty()) {
                            val recipe = recipes.first()
                            val recipeSlide = TopDataObject(
                                resource = recipe.image,
                                label = recipe.label,
                                url = recipe.url
                            )
                            onSuccess(recipeSlide)
                            edamamViewModel.saveRecipeToJson(requireContext(), recipe)
                        }
                    }, onFailure = { error ->
                        Log.e("HomeMenuFragment", "Error fetching recipe: ${error.message}")
                    })
                } catch (e: Exception) {
                    Log.e("HomeMenuFragment", "Unexpected error: ${e.message}")
                }
            }
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
