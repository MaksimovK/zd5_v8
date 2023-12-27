package com.example.zd5_v8.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workers")
data class Worker(
    @PrimaryKey(autoGenerate = true) val workerId: Int = 0,
    val email: String,
    val password: String,
    val name: String
)