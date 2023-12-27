package com.example.zd5_v8

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.Supplier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManageSuppliersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var supplierAdapter: SupplierAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_manage_suppliers, container, false)

        recyclerView = view.findViewById(R.id.rvSuppliers)
        recyclerView.layoutManager = LinearLayoutManager(context)
        supplierAdapter = SupplierAdapter(listOf(), onEditClick = { supplier ->
            val intent = Intent(context, EditSupplierActivity::class.java)
            intent.putExtra("SUPPLIER_ID", supplier.supplierId)
            startActivity(intent)
        }, onDeleteClick = { supplier ->
            showDeleteDialog(supplier)
        })

        recyclerView.adapter = supplierAdapter
        loadSuppliersFromDb()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadSuppliersFromDb()
    }

    private fun loadSuppliersFromDb() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(requireContext())
            val suppliers = db.supplierDao().getAllSuppliers()
            withContext(Dispatchers.Main) {
                supplierAdapter.updateData(suppliers)
            }
        }
    }

    private fun showDeleteDialog(supplier: Supplier) {
        AlertDialog.Builder(requireContext())
            .setTitle("Подтвердите действие")
            .setMessage("Вы уверены, что хотите удалить этого поставщика?")
            .setPositiveButton("Удалить") { _, _ -> deleteSupplier(supplier) }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun deleteSupplier(supplier: Supplier) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(requireContext())
            db.supplierDao().delete(supplier)
            loadSuppliersFromDb()
        }
    }
}
