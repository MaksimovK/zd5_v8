package com.example.zd5_v8.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FurnitureTypeDao {
    @Insert
    fun insert(furnitureType: FurnitureType)

    @Update
    fun update(furnitureType: FurnitureType)

    @Delete
    fun delete(furnitureType: FurnitureType)

    @Query("SELECT * FROM furniture_types")
    fun getAllFurnitureTypes(): List<FurnitureType>
}