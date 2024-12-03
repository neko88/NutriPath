package com.group35.nutripath

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group35.nutripath.api.edamam.EdamamRepository
import com.group35.nutripath.api.edamam.Recipe
import com.group35.nutripath.api.themealdb.Meal
import kotlinx.coroutines.launch

class NutriPathFoodViewModel (val application: NutriPathApplication) : ViewModel() {
    private val _userFoodTags = MutableLiveData<MutableList<String>>()
    private val _fetchedFoodList = MutableLiveData<MutableList<Recipe>>()
    private val _fetchedFoodListNames = MutableLiveData<MutableList<String>>()
    private val _favouriteMealList = MutableLiveData<MutableList<Meal>>()
    val userFoodTags : LiveData<MutableList<String>> = _userFoodTags        // TODO: set up some observer
    val fetchedFoodList : LiveData <MutableList<Recipe>> = _fetchedFoodList
    val fetchedFoodListNames : LiveData<MutableList<String>> = _fetchedFoodListNames
    val favouriteMealList : LiveData<MutableList<Meal>> = _favouriteMealList

    private val callInterval = 60000L
    private val repository = EdamamRepository()
    private lateinit var handler : Handler
    private lateinit var apiRunnable: Runnable


    init{
        val initialFoodList = mutableListOf<String>("Rice","Sushi","Ramen","Laksa","Curry","Onigiri","Chinese","Dumplings","Bao","Shrimp",
            "Pho","Spring Rolls","Buns","Pasta","Korean","Vietnamese","Banh Mi","Thai","Coconut","Bubble Tea","Mango")
        _userFoodTags.value = initialFoodList
    //    startApiRunnable()
    }

//    private fun startApiRunnable() {
//        handler = Handler(Looper.getMainLooper())
//        apiRunnable = object : Runnable {
//            override fun run() {
//                viewModelScope.launch {
//                    fetchRandomRecipe { recipe ->
//                        addRecipeToFetchedFoodsList(recipe)
//                    }
//                    handler.postDelayed(apiRunnable, callInterval) // schedule next Edamam call in 60 seconds
//                }
//            }
//        }
//        handler.post(apiRunnable)
//    }

    fun addFavouriteMeal(meal: Meal){
        try {
            val currentList = _favouriteMealList.value ?: mutableListOf()
            currentList.add(meal)
            _favouriteMealList.postValue(currentList)
            Log.d("NutriPathFoodModel", "Saved a favourite meal: ${meal.strMeal}")
        }catch(e: Exception){
            Log.d("NutriPathFoodModel", "Couldn't save a favourite meal.")
        }
    }
    fun removeFavouriteMeal(meal: Meal){
        val currentList = _favouriteMealList.value ?: mutableListOf()
        currentList.remove(meal)
        _favouriteMealList.postValue(currentList)
    }

    fun getFetchedFoodList(): MutableList<Recipe>? {
        return _fetchedFoodList.value
    }

    fun getFavouriteRecipes(): MutableList<Meal>? {
        return _favouriteMealList.value
    }

    fun addUserFoodTag(tag: String){
        try{
        _userFoodTags.value?.add(tag)
        Log.d("[NutriPathFoodViewModel]", "Added a new user food tag: ${tag}")
    } catch (e: Exception){
        Log.d("[NutriPathFoodViewModel]", "Couldn't add tag to user's tag list: ${e}")
        }
    }

//    fun addRecipeToFetchedFoodsList(recipe: Recipe) {
//        try {
//            val currentList = _fetchedFoodList.value ?: mutableListOf()
//            _fetchedFoodList.postValue(currentList)
//            val nameList = _fetchedFoodListNames.value ?: mutableListOf()
//            nameList.add(recipe.label) // Update the names list
//            _fetchedFoodListNames.postValue(nameList)
//            Log.e("HomeMenuFragment", "Successfully saved recipe to fetched food list: ${recipe.label}")
//        } catch (e: Exception) {
//            Log.e("HomeMenuFragment", "Didn't save fetched recipe: ${e}")
//        }
//    }


  //  private val appId = "d45fcd57"
  //  private val appKey = "2f4ad82db15fbca9481a538196db68dd"
    fun fetchRandomRecipe(onSuccess: (Recipe) -> Unit) {
//        if (randomFoodQuery != null) {
//            viewModelScope.launch {
//                try {
//                    val result = repository.searchRecipes(
//                        query = randomFoodQuery,
//                        appId = appId,
//                        appKey = appKey,
//                        from = 0,
//                        to = 1
//                    )
//                    result.fold(onSuccess = { recipes ->
//                        if (recipes.isNotEmpty()) {
//                            val recipe = recipes.first()
//                            onSuccess(recipe)
//                            Log.e("HomeMenuFragment", "Successfully fetched recipe: ${recipe.label}") }
//                    }, onFailure = { error ->
//                        Log.e("HomeMenuFragment", "Error fetching recipe: ${error.message}")
//                    })
//                } catch (e: Exception) {
//                    Log.e("HomeMenuFragment", "Unexpected error: ${e.message}")
//                }
//            }
//        }
    }
}