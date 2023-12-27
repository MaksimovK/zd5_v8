package com.example.zd5_v8.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "part_types")
data class PartType(
    @PrimaryKey(autoGenerate = true) val partTypeId: Int = 0,
    val partName: String
)