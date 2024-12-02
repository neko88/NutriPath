package com.group35.nutripath.api.edamam

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.io.File

class EdamamViewModel : ViewModel(){

    val repository : EdamamRepository = EdamamRepository()
    val recipesLiveData = MutableLiveData<List<Recipe>>()
    val errorLiveData = MutableLiveData<String>()

    val _randomFoodTagList = MutableLiveData<List<String>>()
    val randomFoodTagList: LiveData<List<String>> = _randomFoodTagList


    init{
        _randomFoodTagList.value = listOf("Sushi", "Dumplings", "Bao", "Pho", "Rice", "Tteokbokki", "Bibimbap","Ramen")
    }

    fun fetchRecipes(query: String) {
        viewModelScope.launch {
            val result = repository.searchRecipes(query, "yourAppId", "yourAppKey")
            result.fold(
                onSuccess = { recipes ->
                    recipesLiveData.postValue(recipes)
                },
                onFailure = { error ->
                    errorLiveData.postValue(error.message ?: "Unknown error")
                }
            )
        }
    }

    fun getRandomFoodTagList(): String? {
        val random = _randomFoodTagList.value?.random()
        println("[Edamam @ ViewModel]: Random recipe term: ${random}")
        return random
    }

    fun readObjectsFromJsonFile(context: Context, fileName: String): List<Object> {
        val gson = Gson()
        val file = File(context.filesDir, fileName)
        return if (file.exists()) {
            val jsonString = file.readText()
            val type = object : TypeToken<List<Object>>() {}.type
            gson.fromJson(jsonString, type)
        } else {
            emptyList()
        }
    }

    fun serializeRecipeToJson(recipe: Recipe): String {
        val gson = Gson()
        return gson.toJson(recipe)
    }

    fun saveRecipeToJson(context: Context, recipe: Recipe) {
        val jsonContent = serializeRecipeToJson(recipe)
        val fileName = recipe.label
        val resolver = context.contentResolver
        val uri = MediaStore.Files.getContentUri("external")
        val cursor = resolver.query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${MediaStore.MediaColumns.RELATIVE_PATH} = ?",
            arrayOf(fileName, Environment.DIRECTORY_DOCUMENTS + "/NutriPath"),
            null)
        val fileExists = cursor?.use { it.moveToFirst() } == true

        if (fileExists) {
            println("File already exists: $fileName")
            return
        }
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName) // File name
            put(MediaStore.MediaColumns.MIME_TYPE, "application/json") // File type
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOCUMENTS + "/NutriPath"
            )
        }
        val fileUri = resolver.insert(uri, contentValues)
        if (fileUri != null) {
            resolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonContent.toByteArray())
                println("File saved successfully.") }
            println("File URI: $uri")
            val absolutePath = "/storage/emulated/0/Documents/NutriPath/$fileName"
            println("Potential file path: $absolutePath")
        } else {
            println("Failed to save file.")
        }
    }
}