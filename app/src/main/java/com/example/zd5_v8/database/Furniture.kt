package com.example.zd5_v8.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "furnitures")
data class Furniture(
    @PrimaryKey(autoGenerate = true) val furnitureId: Int = 0,
    val typeId: Int,
    val name: String,
    val description: String,
    val cost: Double,
    val photo: String
)