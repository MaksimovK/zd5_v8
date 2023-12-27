package com.example.zd5_v8.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suppliers")
data class Supplier(
    @PrimaryKey(autoGenerate = true) val supplierId: Int = 0,
    val email: String,
    val password: String,
    val role: String = "Поставщик",
    val name: String,
    val address: String
)
