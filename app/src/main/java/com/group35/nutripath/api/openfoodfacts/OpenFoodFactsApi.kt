import com.group35.nutripath.api.openfoodfacts.ResponseFoodProducts
import com.group35.nutripath.api.themealdb.ResponseMeal
import com.group35.nutripath.api.themealdb.ResponseMealInformation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path


/*
API build for OpenFoodFacts API
 */
interface OpenFoodFactsApi {
    @GET("api/v2/product/{barcode}")
    fun getProductByBarcode(@Path("barcode") barcode: String): Call<ResponseFoodProducts>
}

// API Caller
object RetroFitCallerOpenFoodFacts{
    private const val URL = "https://world.openfoodfacts.net/"
    val api: OpenFoodFactsApi by lazy{
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
    }
}



