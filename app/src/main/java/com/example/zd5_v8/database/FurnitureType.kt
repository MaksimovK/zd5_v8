package com.example.zd5_v8.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "furniture_types")
data class FurnitureType(
    @PrimaryKey(autoGenerate = true) val typeId: Int,
    val typeName: String
)
