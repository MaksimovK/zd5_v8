package com.example.zd5_v8

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.Furniture
import com.example.zd5_v8.database.FurnitureAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkerFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var furnitureAdapter: FurnitureAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_worker, container, false)

        recyclerView = view.findViewById(R.id.rvFurniture)
        recyclerView.layoutManager = LinearLayoutManager(context)

        furnitureAdapter = FurnitureAdapter(listOf(), this::onItemClick, this::onItemLongClick)
        recyclerView.adapter = furnitureAdapter

        loadFurnitureFromDb()

        view.findViewById<Button>(R.id.btnAddFurniture).setOnClickListener {
            val intent = Intent(requireContext(), AddFurnitureActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btnAddFurnitureType).setOnClickListener {
            val intent = Intent(requireContext(), AddFurnitureTypeActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadFurnitureFromDb()
    }

    private fun onItemClick(furniture: Furniture) {
        val intent = Intent(requireContext(), EditFurnitureActivity::class.java)
        intent.putExtra("FURNITURE_ID", furniture.furnitureId)
        startActivity(intent)
    }

    private fun onItemLongClick(furniture: Furniture) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Удаление")
            setMessage("Вы действительно хотите удалить?")
            setPositiveButton("Да") { _, _ ->
                deleteFurniture(furniture)
            }
            setNegativeButton("Нет", null)
            show()
        }
    }

    private fun deleteFurniture(furniture: Furniture) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(requireContext())
            db.furnitureDao().delete(furniture)
            withContext(Dispatchers.Main) {

                loadFurnitureFromDb()
            }
        }
    }

    private fun loadFurnitureFromDb() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(requireContext())
            val furniture = db.furnitureDao().getAllFurnitures()
            withContext(Dispatchers.Main) {
                furnitureAdapter.updateData(furniture)
            }
        }
    }
}