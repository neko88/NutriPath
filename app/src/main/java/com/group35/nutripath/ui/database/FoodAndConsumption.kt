package com.group35.nutripath.ui.database

import androidx.room.Embedded
import androidx.room.Relation

data class FoodAndConsumption(
    @Embedded
    val food: FoodItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "food_id"
    )
    val consumption: List<Consumption>
)