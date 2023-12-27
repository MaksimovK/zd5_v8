package com.example.zd5_v8.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val clientId: Int = 0,
    val email: String,
    val password: String,
    val role: String = "Клиент",
    val name: String,
    val discount: Double
)