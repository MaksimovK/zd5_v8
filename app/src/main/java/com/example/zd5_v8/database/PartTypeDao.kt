package com.example.zd5_v8.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PartTypeDao {
    @Insert
    fun insert(partType: PartType)

    @Update
    fun update(partType: PartType)

    @Delete
    fun delete(partType: PartType)

    @Query("SELECT * FROM part_types")
    fun getAllPartTypes(): List<PartType>
}
