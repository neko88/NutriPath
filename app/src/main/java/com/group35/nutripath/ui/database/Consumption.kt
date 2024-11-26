package com.group35.nutripath.ui.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/*
 * TODO: Fix foreign key constraints; currently removed because they broke the whole thing
 */

@Entity(
    tableName = "consumption_table",
    indices = [Index(value=["foodId"])],
    foreignKeys = [ForeignKey(entity = FoodItem::class, parentColumns = ["id"], childColumns = ["foodId"])]
)
data class Consumption(
    @PrimaryKey(autoGenerate = true)
    var consumptionId: Long = 0,

    val foodId: Long,

    val date: Long // type may change
)
