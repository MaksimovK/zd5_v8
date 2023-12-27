package com.example.zd5_v8

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.FurnitureType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddFurnitureTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_furniture_type)

        val etFurnitureName = findViewById<EditText>(R.id.etFurnitureName)
        val btnSaveFurniture = findViewById<Button>(R.id.btnSaveFurniture)

        btnSaveFurniture.setOnClickListener {
            val typeName = etFurnitureName.text.toString().trim()

            if (typeName.isNotEmpty()) {
                val furnitureType = FurnitureType(0, typeName)
                saveFurnitureType(furnitureType)
            } else {
                Toast.makeText(this, "Введите название типа мебели", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveFurnitureType(furnitureType: FurnitureType) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@AddFurnitureTypeActivity)

            val existingTypes = db.furnitureTypeDao().getAllFurnitureTypes()

            val exists = existingTypes.any { it.typeName.equals(furnitureType.typeName, ignoreCase = true) }

            if (!exists) {
                db.furnitureTypeDao().insert(furnitureType)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddFurnitureTypeActivity, "Тип мебели добавлен", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddFurnitureTypeActivity, "Тип мебели уже существует", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
