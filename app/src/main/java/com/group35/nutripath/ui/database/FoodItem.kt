package com.group35.nutripath.ui.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * id: Primary Key
 *
 */
@Entity(tableName = "food_table")
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "calories")
    val cals: Double = 0.0,

    @ColumnInfo(name = "protein")
    val protein: Double = 0.0,

    @ColumnInfo(name = "carbs")
    val carbs: Double = 0.0,

    @ColumnInfo(name = "fats")
    val fats: Double = 0.0,

    @ColumnInfo(name = "sugars")
    val sugars: Double = 0.0,

    @ColumnInfo(name = "price")
    val price: Double = 0.0
)