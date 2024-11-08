import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.group35.nutripath.R
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.group35.nutripath.api.themealdb.Meal

class MealAdapter : ListAdapter<Meal, MealAdapter.MealViewHolder>(MealDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var mealImageView : ImageView
        private lateinit var mealNameTextView : TextView
        private lateinit var mealDescriptionTextView : TextView
        private lateinit var ingredientImage1 : ImageView
        private lateinit var ingredientImage2 : ImageView
        private lateinit var ingredientImage3 : ImageView
        private lateinit var ingredientImage4 : ImageView
        private lateinit var ingredientImage5 : ImageView
        private lateinit var ingredientImage6 : ImageView

        fun bind(meal: Meal) {
            mealImageView = itemView.findViewById(R.id.mealImageView)
            mealNameTextView = itemView.findViewById(R.id.mealTitle)
            mealDescriptionTextView = itemView.findViewById(R.id.mealDescription)
            ingredientImage1 = itemView.findViewById(R.id.ingredientImage1)
            ingredientImage2 = itemView.findViewById(R.id.ingredientImage2)
            ingredientImage3 = itemView.findViewById(R.id.ingredientImage3)
            ingredientImage4 = itemView.findViewById(R.id.ingredientImage4)
            ingredientImage5 = itemView.findViewById(R.id.ingredientImage5)
            ingredientImage6 = itemView.findViewById(R.id.ingredientImage6)

            mealNameTextView.text = meal.strMeal
            mealDescriptionTextView.text = meal.strInstructions

            Glide.with(itemView.context)
                .load(meal.strMealThumb)
                .apply(RequestOptions().transform(RoundedCorners(50))) // 50dp corner radius
                .into(this.mealImageView)


            val imgIngredientUrl1 = "https://www.themealdb.com/images/ingredients/${meal.strIngredient1}.png"
            val urlEncoded1 = imgIngredientUrl1.replace(" ", "%20")
            Glide.with(itemView.context)
                .load(urlEncoded1)
                .into(this.ingredientImage1)
            val imgIngredientUrl2 = "https://www.themealdb.com/images/ingredients/${meal.strIngredient2}.png"
            val urlEncoded2 = imgIngredientUrl2.replace(" ", "%20")
            Glide.with(itemView.context)
                .load(urlEncoded2)
                .into(this.ingredientImage2)
            val imgIngredientUrl3 = "https://www.themealdb.com/images/ingredients/${meal.strIngredient3}.png"
            val urlEncoded3 = imgIngredientUrl3.replace(" ", "%20")
            Glide.with(itemView.context)
                .load(urlEncoded3)
                .into(this.ingredientImage3)
            val imgIngredientUrl4 = "https://www.themealdb.com/images/ingredients/${meal.strIngredient4}.png"
            val urlEncoded4 = imgIngredientUrl4.replace(" ", "%20")
            Glide.with(itemView.context)
                .load(urlEncoded4)
                .into(this.ingredientImage4)
            val imgIngredientUrl5 = "https://www.themealdb.com/images/ingredients/${meal.strIngredient5}.png"
            val urlEncoded5 = imgIngredientUrl5.replace(" ", "%20")
            Glide.with(itemView.context)
                .load(urlEncoded5)
                .into(this.ingredientImage5)
            val imgIngredientUrl6 = "https://www.themealdb.com/images/ingredients/${meal.strIngredient6}.png"
            val urlEncoded6 = imgIngredientUrl6.replace(" ", "%20")
            Glide.with(itemView.context)
                .load(urlEncoded6)
                .into(this.ingredientImage6)

        }
    }

    class MealDiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
}
