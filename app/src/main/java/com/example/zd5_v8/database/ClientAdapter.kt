package com.example.zd5_v8

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_v8.database.Client

class ClientAdapter(
    private var clients: List<Client>,
    private val onEditClick: (Client) -> Unit,
    private val onDeleteClick: (Client) -> Unit
) : RecyclerView.Adapter<ClientAdapter.ViewHolder>() {

    class ViewHolder(view: ViewGroup, onDelete: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvClientName)
        val emailTextView: TextView = view.findViewById(R.id.tvClientEmail)
        val editButton: Button = view.findViewById(R.id.btnEdit)

        init {
            itemView.setOnClickListener {
                onDelete(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_client, parent, false) as ViewGroup
        return ViewHolder(view, { pos -> onDeleteClick(clients[pos]) })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val client = clients[position]
        holder.nameTextView.text = client.name
        holder.emailTextView.text = client.email
        holder.editButton.setOnClickListener { onEditClick(client) }
    }

    override fun getItemCount(): Int = clients.size

    fun updateData(newClients: List<Client>) {
        clients = newClients
        notifyDataSetChanged()
    }
}
