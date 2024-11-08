import androidx.lifecycle.ViewModel

class FoodViewModel : ViewModel() {

    suspend fun findFoodByBarcode(barcode: String) {
        val response = RetrofitInstance.api.getProductByBarcode(barcode)
        println(response)
    }
}





