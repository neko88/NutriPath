import com.group35.nutripath.api.meal.Meal
import com.group35.nutripath.api.meal.ReturnedMeal
import com.group35.nutripath.api.meal.ReturnedMealInformation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// The Meal DB API provides meal data
interface TheMealDBApi {
    @GET("filter.php")
    fun getMealsByIngredient(@Query("i") ingredient: String): Call<ReturnedMeal>

    @GET("lookup.php")
    fun getMealInformation(@Query("i") mealId: String): Call<ReturnedMealInformation>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") category: String): Call<ReturnedMeal>
}

// API Caller
object RetroFitCaller{
    private const val URL = "https://www.themealdb.com/api/json/v1/1/"
    val api: TheMealDBApi by lazy{
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMealDBApi::class.java)
    }
}



