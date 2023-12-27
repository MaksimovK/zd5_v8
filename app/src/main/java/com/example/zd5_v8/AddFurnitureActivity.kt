package com.example.zd5_v8

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.Furniture
import com.example.zd5_v8.database.FurnitureType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddFurnitureActivity : AppCompatActivity() {

    private lateinit var typeSpinner: Spinner
    private lateinit var furnitureTypes: List<FurnitureType>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_furniture)

        val nameEditText = findViewById<EditText>(R.id.etFurnitureName)
        val descriptionEditText = findViewById<EditText>(R.id.etFurnitureDescription)
        val costEditText = findViewById<EditText>(R.id.etFurnitureCost)
        val photoEditText = findViewById<EditText>(R.id.etFurniturePhoto) // URL or path of photo
        typeSpinner = findViewById(R.id.spinnerFurnitureType)

        loadFurnitureTypes()

        val btnSave = findViewById<Button>(R.id.btnSaveFurniture)
        btnSave.setOnClickListener {
            if (nameEditText.text.isEmpty() || descriptionEditText.text.isEmpty() || costEditText.text.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                if (typeSpinner.selectedItem != null) {
                    val selectedPosition = typeSpinner.selectedItemPosition
                    val selectedFurnitureType = furnitureTypes[selectedPosition]
                    val cost = costEditText.text.toString().toDoubleOrNull() ?: 0.0

                    val furniture = Furniture(
                        typeId = selectedFurnitureType.typeId,
                        name = nameEditText.text.toString(),
                        description = descriptionEditText.text.toString(),
                        cost = cost,
                        photo = photoEditText.text.toString()
                    )
                    saveFurniture(furniture)
                } else {
                    Toast.makeText(this, "Выберите тип мебели", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadFurnitureTypes() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@AddFurnitureActivity)
            furnitureTypes = db.furnitureTypeDao().getAllFurnitureTypes()
            withContext(Dispatchers.Main) {
                updateFurnitureTypesSpinner()
            }
        }
    }

    private fun updateFurnitureTypesSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, furnitureTypes.map { it.typeName })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = adapter
    }

    private fun saveFurniture(furniture: Furniture) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@AddFurnitureActivity)
            db.furnitureDao().insert(furniture)
            withContext(Dispatchers.Main) {
                finish()
            }
        }
    }
}
