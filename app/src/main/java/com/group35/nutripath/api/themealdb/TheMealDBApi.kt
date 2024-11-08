import com.group35.nutripath.api.themealdb.ResponseMeal
import com.group35.nutripath.api.themealdb.ResponseMealInformation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*
The Meal DB API provides meal data.
Reference: https://www.themealdb.com/api.php

 */
interface TheMealDBApi {
    // search meal by ingredient
    @GET("search.php?s=")
    suspend fun getMealsByIngredient(@Query("i") ingredient: String): ResponseMeal


    @GET("lookup.php?s=")
    suspend fun getMealInformation(@Query("i") mealId: String): ResponseMealInformation

    @GET("filter.php?s=")
    suspend fun getMealsByCategory(@Query("c") category: String): ResponseMeal
}

// API Caller
object RetroFitCallerTheMealDB{
    private const val URL = "https://www.themealdb.com/api/json/v1/1/"
    val api: TheMealDBApi by lazy{
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMealDBApi::class.java)
    }
}

/*
Search meal by name:                www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
List all meals by first letter:     www.themealdb.com/api/json/v1/1/search.php?f=a
Lookup full meal details by id:     www.themealdb.com/api/json/v1/1/lookup.php?i=52772
Lookup a single random meal:        www.themealdb.com/api/json/v1/1/random.php
List all meal categories:           www.themealdb.com/api/json/v1/1/categories.php
Filter by main ingredient:          www.themealdb.com/api/json/v1/1/filter.php?i=chicken_breast
Filter by Category:                 www.themealdb.com/api/json/v1/1/filter.php?c=Seafood
Filter by Area:                     www.themealdb.com/api/json/v1/1/filter.php?a=Canadian

List all Categories, Area, Ingredients
    www.themealdb.com/api/json/v1/1/list.php?c=list
    www.themealdb.com/api/json/v1/1/list.php?a=list
    www.themealdb.com/api/json/v1/1/list.php?i=list

Meal Thumbnail Images - Add /preview to the end of the meal image URL
        /images/media/meals/llcbn01574260722.jpg/preview

Ingredient Thumbnail Images
        www.themealdb.com/images/ingredients/Lime.png
        www.themealdb.com/images/ingredients/Lime-Small.png

 */



