package com.example.zd5_v8.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkerDao {
    @Insert
    fun insert(worker: Worker)

    @Update
    fun update(worker: Worker)

    @Delete
    fun delete(worker: Worker)

    @Query("SELECT * FROM workers")
    fun getAllWorkers(): List<Worker>

    @Query("SELECT * FROM workers WHERE email = :email")
    fun findWorkerByEmail(email: String): Worker?
}