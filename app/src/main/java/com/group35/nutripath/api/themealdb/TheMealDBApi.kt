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
    @GET("search.php?s=")
    fun getMealsByIngredient(@Query("i") ingredient: String): Call<ResponseMeal>

    @GET("lookup.php?s=")
    fun getMealInformation(@Query("i") mealId: String): Call<ResponseMealInformation>

    @GET("filter.php?s=")
    fun getMealsByCategory(@Query("c") category: String): Call<ResponseMeal>
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



