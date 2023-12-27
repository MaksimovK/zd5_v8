package com.example.zd5_v8

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.Part
import com.example.zd5_v8.database.PartAdapter
import com.example.zd5_v8.database.PartType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SupplierActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var partsAdapter: PartAdapter
    private lateinit var btnAddPart: Button
    private lateinit var btnAddPartType: Button
    private lateinit var partTypeSpinner: Spinner
    private var partTypes: List<PartType> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier)

        recyclerView = findViewById(R.id.rvParts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        partsAdapter = PartAdapter(
            emptyList(),
            onItemClick = { part -> onItemClick(part) },
            onItemLongClick = { part -> showDeleteDialog(part) }
        )
        recyclerView.adapter = partsAdapter

        partTypeSpinner = findViewById(R.id.categorySpinner)
        loadPartTypes()

        btnAddPart = findViewById(R.id.btnAddParts)
        btnAddPart.setOnClickListener {
            val intent = Intent(this, AddPartActivity::class.java)
            startActivity(intent)
        }

        btnAddPartType = findViewById(R.id.btnAddPartsType)
        btnAddPartType.setOnClickListener {
            val intent = Intent(this, AddPartTypeActivity::class.java)
            startActivity(intent)
        }

        loadParts()
    }

    override fun onResume() {
        super.onResume()
        loadParts()
    }

    private fun showDeleteDialog(part: Part) {
        AlertDialog.Builder(this).apply {
            setTitle("Удаление детали")
            setMessage("Вы уверены, что хотите удалить эту деталь?")
            setPositiveButton("Удалить") { _, _ -> deletePart(part) }
            setNegativeButton("Отмена", null)
            show()
        }
    }

    private fun deletePart(part: Part) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            db.partDao().delete(part)
            loadParts()
        }
    }

    private fun loadPartTypes() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            partTypes = db.partTypeDao().getAllPartTypes()
            val categories = listOf("Все категории") + partTypes.map { it.partName }
            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(this@SupplierActivity, android.R.layout.simple_spinner_item, categories)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                partTypeSpinner.adapter = adapter
                partTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        if (position == 0) {
                            loadParts()
                        } else {
                            filterPartsByType(partTypes[position - 1].partTypeId)
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
            }
        }
    }

    private fun filterPartsByType(partTypeId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val filteredParts = if (partTypeId == 0) {
                db.partDao().getAllParts()
            } else {
                db.partDao().getPartsByType(partTypeId)
            }
            withContext(Dispatchers.Main) {
                partsAdapter.updateParts(filteredParts)
            }
        }
    }


    private fun loadParts() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val parts = db.partDao().getAllParts()
            withContext(Dispatchers.Main) {
                partsAdapter.updateParts(parts)
            }
        }
    }
    private fun onItemClick(part: Part) {
        val intent = Intent(this, EditPartActivity::class.java)
        intent.putExtra("PART_ID", part.partId)
        startActivity(intent)
    }
}
