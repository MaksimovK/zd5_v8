package com.example.zd5_v8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.Furniture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditFurnitureActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var costEditText: EditText
    private lateinit var photoEditText: EditText
    private var furnitureId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_furniture)

        nameEditText = findViewById(R.id.etFurnitureName)
        descriptionEditText = findViewById(R.id.etFurnitureDescription)
        costEditText = findViewById(R.id.etFurnitureCost)
        photoEditText = findViewById(R.id.etImage)

        furnitureId = intent.getIntExtra("FURNITURE_ID", -1)
        if (furnitureId != -1) {
            loadFurnitureDetails(furnitureId)
        }

        val btnSave = findViewById<Button>(R.id.btnSaveFurniture)
        btnSave.setOnClickListener {
            updateFurnitureDetails()
        }

    }

    private fun loadFurnitureDetails(furnitureId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val furniture = db.furnitureDao().getFurnitureById(furnitureId)
            withContext(Dispatchers.Main) {
                furniture?.let {
                    nameEditText.setText(it.name)
                    descriptionEditText.setText(it.description)
                    costEditText.setText(it.cost.toString())
                    photoEditText.setText(it.photo)
                }
            }
        }
    }

    private fun updateFurnitureDetails() {
        val name = nameEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val cost = costEditText.text.toString()
        if (name.isEmpty() || description.isEmpty() || cost.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        } else {
            if (furnitureId != -1) {
                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(applicationContext)
                    val cost = cost.toDoubleOrNull() ?: 0.0
                    val photo = photoEditText.text.toString()
                    val furniture = Furniture(
                        furnitureId = furnitureId,
                        typeId = 1,
                        name = name,
                        description = description,
                        cost = cost,
                        photo = photo
                    )
                    db.furnitureDao().update(furniture)
                    withContext(Dispatchers.Main) {
                        finish()
                    }
                }
            }
        }
    }
}
