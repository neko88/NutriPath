package com.group35.nutripath.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.group35.nutripath.api.edamam.Recipe
import com.group35.nutripath.ui.database.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeItemDao {
    @Insert
    suspend fun insertRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipe_table")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("DELETE FROM recipe_table")
    fun deleteAllRecipes()

}