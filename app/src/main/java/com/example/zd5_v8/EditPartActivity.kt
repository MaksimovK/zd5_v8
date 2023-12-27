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

class EditPartActivity : AppCompatActivity() {

    private lateinit var spinnerPartType: Spinner
    private lateinit var etPartName: EditText
    private lateinit var etPartWeight: EditText
    private lateinit var etPartMaterial: EditText
    private lateinit var etPartDiameter: EditText
    private lateinit var etPartPhoto: EditText
    private lateinit var btnSavePart: Button
    private var partId: Int = -1
    private lateinit var partTypes: List<PartType>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_part)

        spinnerPartType = findViewById(R.id.spinnerPartType)
        etPartName = findViewById(R.id.etPartName)
        etPartWeight = findViewById(R.id.etPartWeight)
        etPartMaterial = findViewById(R.id.etPartMaterial)
        etPartDiameter = findViewById(R.id.etPartDiameter)
        etPartPhoto = findViewById(R.id.etPartPhoto)
        btnSavePart = findViewById(R.id.btnSavePart)

        partId = intent.getIntExtra("PART_ID", -1)

        loadPartTypes()
        if (partId != -1) {
            loadPartDetails(partId)
        }

        btnSavePart.setOnClickListener {
            updatePartDetails()
        }
    }

    private fun loadPartTypes() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            partTypes = db.partTypeDao().getAllPartTypes()
            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(this@EditPartActivity, android.R.layout.simple_spinner_dropdown_item, partTypes.map { it.partName })
                spinnerPartType.adapter = adapter
            }
        }
    }

    private fun loadPartDetails(partId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val part = db.partDao().getPartById(partId)
            withContext(Dispatchers.Main) {
                part?.let {
                    etPartName.setText(it.name)
                    etPartWeight.setText(it.weight.toString())
                    etPartMaterial.setText(it.material)
                    etPartDiameter.setText(it.diameter.toString())
                    etPartPhoto.setText(it.photo)
                    spinnerPartType.setSelection(partTypes.indexOfFirst { type -> type.partTypeId == it.partTypeId })
                }
            }
        }
    }

    private fun updatePartDetails() {
        val name = etPartName.text.toString()
        val weight = etPartWeight.text.toString().toDoubleOrNull() ?: 0.0
        val material = etPartMaterial.text.toString()
        val diameter = etPartDiameter.text.toString().toDoubleOrNull() ?: 0.0
        val photo = etPartPhoto.text.toString()

        if (name.isEmpty() || weight == 0.0 || material.isEmpty() || diameter == 0.0) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        } else {
            if (partId != -1) {
                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(applicationContext)
                    val selectedPartType = partTypes[spinnerPartType.selectedItemPosition]
                    val updatedPart = Part(
                        partId = partId,
                        partTypeId = selectedPartType.partTypeId,
                        name = name,
                        weight = weight,
                        material = material,
                        diameter = diameter,
                        photo = photo
                    )
                    db.partDao().update(updatedPart)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditPartActivity, "Деталь обновлена", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}
