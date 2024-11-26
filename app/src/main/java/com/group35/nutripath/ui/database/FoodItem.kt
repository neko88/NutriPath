package com.group35.nutripath.ui.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/*
 * id: Primary Key
 *
 */
@Entity(tableName = "food_table")
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "calories")
    var cals: Double = 0.0,

    @ColumnInfo(name = "protein")
    var protein: Double = 0.0,

    @ColumnInfo(name = "carbs")
    var carbs: Double = 0.0,

    @ColumnInfo(name = "fats")
    var fats: Double = 0.0,

    @ColumnInfo(name = "sugars")
    var sugars: Double = 0.0,

    @ColumnInfo(name = "price")
    var price: Double = 0.0
)