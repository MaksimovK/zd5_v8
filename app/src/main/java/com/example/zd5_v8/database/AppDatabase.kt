package com.example.zd5_v8.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Order::class, FurnitureType::class, Furniture::class, PartType::class, Part::class, Supplier::class, Client::class, Worker::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun furnitureTypeDao(): FurnitureTypeDao
    abstract fun furnitureDao(): FurnitureDao
    abstract fun partTypeDao(): PartTypeDao
    abstract fun partDao(): PartDao
    abstract fun supplierDao(): SupplierDao
    abstract fun clientDao(): ClientDao
    abstract fun workerDao(): WorkerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
