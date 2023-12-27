package com.example.zd5_v8.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FurnitureDao {
    @Insert
    fun insert(furniture: Furniture)

    @Update
    fun update(furniture: Furniture)

    @Delete
    fun delete(furniture: Furniture)

    @Query("SELECT * FROM furnitures")
    fun getAllFurnitures(): List<Furniture>

    @Query("SELECT * FROM furnitures WHERE furnitureId = :id")
    fun getFurnitureById(id: Int): Furniture?

    @Query("SELECT * FROM furnitures WHERE typeId = :typeId")
    fun getFurnitureByType(typeId: Int): List<Furniture>
}
