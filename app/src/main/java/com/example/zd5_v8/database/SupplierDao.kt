package com.example.zd5_v8.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SupplierDao {
    @Insert
    fun insert(supplier: Supplier)

    @Update
    fun update(supplier: Supplier)

    @Delete
    fun delete(supplier: Supplier)

    @Query("SELECT * FROM suppliers")
    fun getAllSuppliers(): List<Supplier>

    @Query("SELECT * FROM suppliers WHERE email = :email")
    fun findSupplierByEmail(email: String): Supplier?

    @Query("SELECT * FROM suppliers WHERE supplierId = :supplierId")
    fun getSupplierById(supplierId: Int): Supplier?
}