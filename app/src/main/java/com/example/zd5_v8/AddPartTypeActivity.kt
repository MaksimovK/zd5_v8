package com.example.zd5_v8

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.PartType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPartTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_part_type)

        val etPartTypeName = findViewById<EditText>(R.id.etPartTypeName)
        val btnSavePartType = findViewById<Button>(R.id.btnSavePartType)

        btnSavePartType.setOnClickListener {
            val typeName = etPartTypeName.text.toString().trim()

            if (typeName.isNotEmpty()) {
                val partType = PartType(0, typeName)
                savePartType(partType)
            } else {
                Toast.makeText(this, "Введите название типа детали", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePartType(partType: PartType) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@AddPartTypeActivity)

            val existingTypes = db.partTypeDao().getAllPartTypes()

            val exists = existingTypes.any { it.partName.equals(partType.partName, ignoreCase = true) }

            if (!exists) {
                db.partTypeDao().insert(partType)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddPartTypeActivity, "Тип детали добавлен", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddPartTypeActivity, "Тип детали уже существует", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
