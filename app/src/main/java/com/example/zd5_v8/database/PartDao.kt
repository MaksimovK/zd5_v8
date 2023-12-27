package com.example.zd5_v8.database

import androidx.room.*

@Dao
interface PartDao {
    @Insert
    fun insert(part: Part): Long

    @Update
    fun update(part: Part)

    @Delete
    fun delete(part: Part)

    @Query("SELECT * FROM parts")
    fun getAllParts(): List<Part>

    @Query("SELECT * FROM parts WHERE partTypeId = :partTypeId")
    fun getPartsByType(partTypeId: Int): List<Part>

    @Query("SELECT * FROM parts WHERE partId = :partId")
    fun getPartById(partId: Int): Part?

    @Insert
    fun insertType(partType: PartType): Long

    @Query("SELECT * FROM part_types")
    fun getAllPartTypes(): List<PartType>
}