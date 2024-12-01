package com.group35.nutripath.api

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File



class JsonFileManager {

    fun saveObjectsToJsonFile(context: Context, objects: List<Object>, fileName: String) {
        val gson = Gson()
        val jsonString = gson.toJson(objects)

        val file = File(context.filesDir, fileName)
        file.writeText(jsonString)

        println("JSON saved to: ${file.absolutePath}")
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
}
