package com.example.zd5_v8

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_v8.database.Supplier

class SupplierAdapter(
    private var suppliers: List<Supplier>,
    private val onEditClick: (Supplier) -> Unit,
    private val onDeleteClick: (Supplier) -> Unit
) : RecyclerView.Adapter<SupplierAdapter.ViewHolder>() {

    class ViewHolder(view: View, onEdit: (Int) -> Unit, onDelete: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvSupplierName)
        val emailTextView: TextView = view.findViewById(R.id.tvSupplierEmail)
        val editButton: Button = view.findViewById(R.id.btnEdit)

        init {
            itemView.setOnClickListener { onDelete(adapterPosition) }
            editButton.setOnClickListener { onEdit(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_supplier, parent, false)
        return ViewHolder(view, { pos -> onEditClick(suppliers[pos]) }, { pos -> onDeleteClick(suppliers[pos]) })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val supplier = suppliers[position]
        holder.nameTextView.text = supplier.name
        holder.emailTextView.text = supplier.email
    }

    override fun getItemCount(): Int = suppliers.size

    fun updateData(newSuppliers: List<Supplier>) {
        suppliers = newSuppliers
        notifyDataSetChanged()
    }
}
