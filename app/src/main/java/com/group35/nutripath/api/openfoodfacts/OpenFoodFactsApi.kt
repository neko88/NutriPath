// Replace with the correct import path for ProductResponse
import com.group35.nutripath.api.openfoodfacts.ProductResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import android.util.Log

// Define API service interface
interface OpenFoodFactsService {
    @GET("product/{barcode}.json")
    suspend fun getProductByBarcode(@Path("barcode") barcode: String): Response<ProductResponse>
}

// Create Retrofit instance
object RetrofitInstance {
    private const val BASE_URL = "https://world.openfoodfacts.org/api/v2/"

    val api: OpenFoodFactsService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsService::class.java)
    }
}

// Repository to fetch data from API
class ProductRepository {
    private val api = RetrofitInstance.api

    suspend fun getProductByBarcode(barcode: String): Response<ProductResponse> {
        return withContext(Dispatchers.IO) {
            try {
                api.getProductByBarcode(barcode)
            } catch (e: HttpException) {
                Log.e("ProductRepository", "HTTP error: ${e.message}")
                throw e
            } catch (e: Exception) {
                Log.e("ProductRepository", "Unexpected error: ${e.message}")
                throw e
            }
        }
    }
}



/*
Notes:
1. ProductResponse is assumed to be a data class that represents the JSON response from the OpenFoodFacts API.
2. Handle permissions and network errors accordingly when integrating into your Android project.
*/
