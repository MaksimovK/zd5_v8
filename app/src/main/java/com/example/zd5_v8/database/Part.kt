package com.example.zd5_v8.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parts")
data class Part(
    @PrimaryKey(autoGenerate = true) val partId: Int = 0,
    val partTypeId: Int,
    val name: String,
    val weight: Double,
    val material: String,
    val diameter: Double,
    val photo: String
)