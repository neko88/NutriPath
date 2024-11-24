package com.group35.nutripath.ui.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "consumption_table",
    foreignKeys = [ForeignKey(entity = FoodItem::class, parentColumns = ["id"], childColumns = ["food_id"], onDelete = ForeignKey.CASCADE)]
)
data class Consumption(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "food_id")
    var foodId: Long = 0,

    @ColumnInfo(name = "date")
    var date: Long = 0 // type may change
)
