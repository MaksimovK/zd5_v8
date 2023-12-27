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
import com.example.zd5_v8.database.Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManageClientsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var clientAdapter: ClientAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_manage_clients, container, false)

        recyclerView = view.findViewById(R.id.rvClients)
        recyclerView.layoutManager = LinearLayoutManager(context)
        clientAdapter = ClientAdapter(listOf(), onEditClick = { client ->
            val intent = Intent(context, EditClientActivity::class.java)
            intent.putExtra("CLIENT_ID", client.clientId)
            startActivity(intent)
        }, onDeleteClick = { client ->
            showDeleteDialog(client)
        })

        recyclerView.adapter = clientAdapter
        loadClientsFromDb()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadClientsFromDb()
    }

    private fun loadClientsFromDb() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(requireContext())
            val clients = db.clientDao().getAllClients()
            withContext(Dispatchers.Main) {
                clientAdapter.updateData(clients)
            }
        }
    }

    private fun showDeleteDialog(client: Client) {
        AlertDialog.Builder(requireContext())
            .setTitle("Подтвердите действие")
            .setMessage("Вы уверены, что хотите удалить этого клиента?")
            .setPositiveButton("Удалить") { _, _ -> deleteClient(client) }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun deleteClient(client: Client) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(requireContext())
            db.clientDao().delete(client)
            loadClientsFromDb()
        }
    }
}
