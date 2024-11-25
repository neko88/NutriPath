package com.group35.nutripath.ui.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/*
 * TODO: Fix foreign key constraints; currently removed because they broke the whole thing
 */

@Entity(
    tableName = "consumption_table"

)
data class Consumption(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "food_id")
    val foodId: Long,

    @ColumnInfo(name = "date")
    val date: Long // type may change
)
