package com.group35.nutripath.ui.database

import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.coroutines.flow.Flow

/*
 * This module defines the one to many relationship between food (one) and consumption (many)
 */
data class FoodWithConsumption(
    @Embedded val food: FoodItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "foodId"
    )
    val consumption: Flow<List<Consumption>>
)