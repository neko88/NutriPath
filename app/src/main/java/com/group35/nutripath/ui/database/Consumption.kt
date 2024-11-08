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
    val id: Long = 0,

    @ColumnInfo(name = "food_id")
    val foodId: Long,

    @ColumnInfo(name = "date")
    val date: Long // type may change






)
