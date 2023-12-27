package com.example.zd5_v8.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ClientDao {
    @Insert
    fun insert(client: Client)

    @Update
    fun update(client: Client)

    @Delete
    fun delete(client: Client)

    @Query("SELECT * FROM clients")
    fun getAllClients(): List<Client>

    @Query("SELECT * FROM clients WHERE email = :email")
    fun findClientByEmail(email: String): Client?

    @Query("SELECT * FROM clients WHERE clientId = :clientId")
    fun getClientById(clientId: Int): Client?
}
