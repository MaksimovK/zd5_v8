package com.example.zd5_v8

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.Part
import com.example.zd5_v8.database.PartType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPartActivity : AppCompatActivity() {

    private lateinit var spinnerPartType: Spinner
    private lateinit var etPartWeight: EditText
    private lateinit var etPartName: EditText
    private lateinit var etPartMaterial: EditText
    private lateinit var etPartDiameter: EditText
    private lateinit var etPartPhoto: EditText
    private lateinit var partTypes: List<PartType>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_part)

        spinnerPartType = findViewById(R.id.spinnerPartType)
        etPartWeight = findViewById(R.id.etPartWeight)
        etPartName = findViewById(R.id.etPartName)
        etPartMaterial = findViewById(R.id.etPartMaterial)
        etPartDiameter = findViewById(R.id.etPartDiameter)
        etPartPhoto = findViewById(R.id.etPartPhoto)

        loadPartTypes()

        findViewById<Button>(R.id.btnSavePart).setOnClickListener {
            val selectedPosition = spinnerPartType.selectedItemPosition
            if (selectedPosition >= 0) {
                val selectedPartType = partTypes[selectedPosition]
                val name = etPartName.text.toString()
                val weightText = etPartWeight.text.toString()
                val material = etPartMaterial.text.toString()
                val diameterText = etPartDiameter.text.toString()
                val photo = etPartPhoto.text.toString()

                // Проверяем, что поля введены
                if (name.isEmpty() || weightText.isEmpty() || material.isEmpty() || diameterText.isEmpty()) {
                    Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Конвертируем в числа с проверкой
                val weight = weightText.toDoubleOrNull()
                val diameter = diameterText.toDoubleOrNull()

                if (weight == null || diameter == null) {
                    Toast.makeText(this, "Пожалуйста, введите корректные числовые значения", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val part = Part(
                    partTypeId = selectedPartType.partTypeId,
                    name = name,
                    weight = weight,
                    material = material,
                    diameter = diameter,
                    photo = photo
                )
                savePart(part)
            } else {
                Toast.makeText(this, "Выберите тип детали", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadPartTypes() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@AddPartActivity)
            partTypes = db.partTypeDao().getAllPartTypes()
            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(this@AddPartActivity, android.R.layout.simple_spinner_item, partTypes.map { it.partName })
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerPartType.adapter = adapter
            }
        }
    }

    private fun savePart(part: Part) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@AddPartActivity)
            db.partDao().insert(part)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddPartActivity, "Деталь добавлена", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
