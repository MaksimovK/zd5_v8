package com.example.zd5_v8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.Furniture
import com.example.zd5_v8.database.FurnitureAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClientActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var furnitureAdapter: FurnitureAdapter
    private lateinit var categorySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        recyclerView = findViewById(R.id.rvFurniture)
        recyclerView.layoutManager = LinearLayoutManager(this)

        categorySpinner = findViewById(R.id.categorySpinner)
        loadCategoriesIntoSpinner()

        furnitureAdapter = FurnitureAdapter(listOf(), this::onItemClick, {})
        recyclerView.adapter = furnitureAdapter

        loadFurnitureFromDb()
    }
    private fun onItemClick(furniture: Furniture) {
        AlertDialog.Builder(this)
            .setTitle("Подтверждение")
            .setMessage("Вы хотите купить этот товар?")
            .setPositiveButton("Да") { _, _ -> deleteFurniture(furniture) }
            .setNegativeButton("Нет", null)
            .show()
    }
    private fun deleteFurniture(furniture: Furniture) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            db.furnitureDao().delete(furniture)
            loadFurnitureFromDb()
        }
    }

    private fun loadCategoriesIntoSpinner() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@ClientActivity)
            val furnitureTypes = db.furnitureTypeDao().getAllFurnitureTypes()
            val categories = listOf("Все категории") + furnitureTypes.map { it.typeName }
            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(
                    this@ClientActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    categories
                )
                categorySpinner.adapter = adapter
                categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                        if (position == 0) {
                            loadFurnitureFromDb()
                        } else {
                            filterFurnitureByCategory(furnitureTypes[position - 1].typeId)
                        }
                    }
                    override fun onNothingSelected(adapterView: AdapterView<*>) {}
                }
            }
        }
    }

    private fun filterFurnitureByCategory(typeId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@ClientActivity)
            val filteredFurniture = db.furnitureDao().getFurnitureByType(typeId)
            withContext(Dispatchers.Main) {
                furnitureAdapter.updateData(filteredFurniture)
            }
        }
    }

    private fun loadFurnitureFromDb() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@ClientActivity)
            val furniture = db.furnitureDao().getAllFurnitures()
            withContext(Dispatchers.Main) {
                furnitureAdapter.updateData(furniture)
            }
        }
    }
}