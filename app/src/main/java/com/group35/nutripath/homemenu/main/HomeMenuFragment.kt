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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.group35.nutripath.NutriPathApplication
import com.group35.nutripath.NutriPathFoodViewModel
import com.group35.nutripath.R
import com.group35.nutripath.api.edamam.Recipe
import com.group35.nutripath.api.openfoodfacts.OpenFoodFactsActivity
import com.group35.nutripath.api.themealdb.MealActivity
import com.group35.nutripath.api.themealdb.emptyMeal
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
    private lateinit var favouriteMealAdapter: BottomObjectViewAdapter
    private val appFoodViewModel: NutriPathFoodViewModel by lazy {
        (requireActivity().application as NutriPathApplication).nutripathFoodViewModel
    }
    private var bannerRotationCount = 1

    private lateinit var bannerSlideCollection: MutableList<TopDataObject>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeMenuBinding.inflate(inflater, container, false)
        homePageViewModel.loadTopBannerItems()
        homePageViewModel.loadMiddleBannerItems()
        homePageViewModel.loadBottomBannerItems()

        setupName()
        setupBannerCollection()
        setupSlider()
        setupAutoSlide()
        middleBanner()
        bottomBanner()

        binding.analyticsButton.setOnClickListener {
            val intent = Intent(requireContext(), AnalyticsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.recipeButton.setOnClickListener {
            val intent = Intent(requireContext(), MealActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.searchProductButton.setOnClickListener {
            val intent = Intent(requireContext(), OpenFoodFactsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.barcodeButton.setOnClickListener {
            val intent = Intent(
                requireContext(),
                BarcodeScannerActivity::class.java
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.imageView2.setOnClickListener {
            findNavController().navigate(R.id.homeMenu_to_profile)
        }
        return binding.root
    }

    private fun convertRecipeToTopDataObject(recipe: Recipe): TopDataObject {
        val newTopDataObject = TopDataObject(resource=recipe.image, label=recipe.label, url=recipe.url)
        return newTopDataObject
    }

    private fun setupName() {
        sharedPreferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "")
        binding.textView4.text = name
    }

    // call edamam api for a recipe, if its been over a minute then post result here.
    // update the banner slider on the home view.
    private fun setupBannerCollection() {
        bannerSlideCollection = homePageViewModel.topObjectInitialSlides as MutableList<TopDataObject>

        // update the top slider whenever there is a new recipe fetched from Edamam.
        appFoodViewModel.fetchedFoodList.observe(viewLifecycleOwner) { it ->
            val newSlide = convertRecipeToTopDataObject(it.last())
            Log.d("HomeMenuFragment", "Updated slide with ${newSlide.label}")
            sliderAdapter.updateSingleItem(bannerRotationCount, newSlide)
            if (bannerRotationCount >= bannerSlideCollection.size) {
                bannerRotationCount = 0
            } else { bannerRotationCount += 1 }
        }
    }

    private fun setupSlider() {
        sliderAdapter = TopObjectViewAdapter(bannerSlideCollection, binding.topObjectViewPagerSlider)
        binding.topObjectViewPagerSlider.adapter = sliderAdapter

        // ViewPager2 settings
        binding.topObjectViewPagerSlider.apply {
            offscreenPageLimit = 3 // seconds to switch
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
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

    override fun onDestroyView() {
        super.onDestroyView()
        sliderHandler.removeCallbacksAndMessages(null) // Stop all updates
    }


    private fun middleBanner() {
        homePageViewModel.viewObjectMiddle.observe(viewLifecycleOwner, Observer {
            binding.midRecyclerViewModel.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.midRecyclerViewModel.adapter = MiddleObjectViewAdapter(it)
        })

    }

    // whenever a user favourites a recipe, update the bottom banner
    private fun bottomBanner() {
        val placementBanners = listOf(emptyMeal, emptyMeal, emptyMeal, emptyMeal)

        favouriteMealAdapter = BottomObjectViewAdapter { meal ->
    //        val action = NavigationMealMenuDirections.actionNavigationMealMenuToNavigationMealDetailFragment(meal)
      //      findNavController().navigate(action)

        }

        binding.bottomObjectViewRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.bottomObjectViewRecycler.adapter = favouriteMealAdapter

        favouriteMealAdapter.submitList(placementBanners)

        appFoodViewModel.favouriteMealList.observe(viewLifecycleOwner) { recipes ->
            for (each in recipes){
                println("${each.strMeal}, ${each.strMealThumb}")
            }
            favouriteMealAdapter.submitList(recipes)
        }
    }



}
