package com.group35.nutripath.api

import com.group35.nutripath.api.edamam.Recipe
import com.group35.nutripath.ui.database.FoodItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RecipeItemRepository(private val recipeItemDao: RecipeItemDao) {
    val allRecipeItems: Flow<List<Recipe>> = recipeItemDao.getAllRecipes()

    fun insert(recipe: Recipe){
        CoroutineScope(IO).launch{
            recipeItemDao.insertRecipe(recipe)
        }
    }

    fun deleteAll(){
        CoroutineScope(IO).launch {
            recipeItemDao.deleteAllRecipes()
        }
    }


}