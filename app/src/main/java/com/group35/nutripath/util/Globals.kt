package com.group35.nutripath.util

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar
import java.util.Date

class Globals {
    private lateinit var sharedPreferences: SharedPreferences

    // returns the time interval of current day as a timestamp
    fun getDateInterval(day: Long): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = day

        // Set to start of the day
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        // Set to end of the day
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.timeInMillis

        return Pair(startOfDay, endOfDay)
    }
    // returns the time interval of the current month as a timestamp
    fun getMonthInterval(month: Long): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        val end = calendar.timeInMillis

        return Pair(start, end)
    }

    private fun getBMR(gender: String, weight: String, height: String, age: String, activityLevel: String): Double {
        var bmr = if (gender == "Male") {
            10 * weight.toDouble() + 6.25 * height.toDouble() - 5 * age.toInt() + 5
        } else {
            10 * weight.toDouble() + 6.25 * height.toDouble() - 5 * age.toInt() - 161
        }

        bmr *= when (activityLevel) {
            "Sedentary" -> 1.2
            "Lightly Active" -> 1.375
            "Moderately Active" -> 1.550
            "Very Active" -> 1.725
            else -> 1.90 // "Extra Active"
        }

        return bmr
    }

    fun getBudget(context: Context): Double {
        sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

        val budget = sharedPreferences.getString("budget", "")
        return budget!!.toDouble()
    }


    fun getCalories(context: Context): Double {
        sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

        val gender = sharedPreferences.getString("gender", "").toString()
        val age = sharedPreferences.getString("age", "").toString()
        val height = sharedPreferences.getString("height", "").toString()
        val weight = sharedPreferences.getString("weight", "").toString()
        val activityLevel = sharedPreferences.getString("activityLevel", "").toString()

        val bmr = getBMR(gender, weight, height, age, activityLevel)

        val healthGoal = sharedPreferences.getString("healthGoal", "")

        val calories = when (healthGoal) {
            "Weight Loss" -> bmr * 0.85
            "Muscle Gain" -> bmr + 500
            else -> bmr // "Maintenance"
        }

        return calories
    }

    fun getCarbs(context: Context): Double {
        val calories = getCalories(context)

        return calories * 0.4
    }

    fun getProtein(context: Context): Double {
        val calories = getCalories(context)

        sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val healthGoal = sharedPreferences.getString("healthGoal", "")

        val protein = when (healthGoal) {
            "Weight Loss" -> calories * 0.4
            else -> calories * 0.3
        }

        return protein
    }

    fun getFats(context: Context): Double {
        val calories = getCalories(context)

        sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val healthGoal = sharedPreferences.getString("healthGoal", "")

        val fats = when (healthGoal) {
            "Weight Loss" -> calories * 0.2
            else -> calories * 0.3
        }

        return fats
    }

    fun getSugars(context: Context): Double {
        val calories = getCalories(context)

        return calories * 0.025
    }
}