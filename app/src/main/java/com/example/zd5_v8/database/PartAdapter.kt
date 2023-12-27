package com.example.zd5_v8.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_v8.R
import com.squareup.picasso.Picasso

class PartAdapter(
    private var partsList: List<Part>,
    private val onItemClick: (Part) -> Unit,
    private val onItemLongClick: (Part) -> Unit
) : RecyclerView.Adapter<PartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoImageView: ImageView = view.findViewById(R.id.parts_photo)
        val nameTextView: TextView = view.findViewById(R.id.parts_name)
        val materialTextView: TextView = view.findViewById(R.id.parts_material)
        val diameterTextView: TextView = view.findViewById(R.id.parts_diameter)
        val weightTextView: TextView = view.findViewById(R.id.parts_weight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_parts, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val part = partsList[position]
        holder.nameTextView.text = part.name
        holder.materialTextView.text = "Материал: ${part.material}"
        holder.diameterTextView.text = "Диаметр: ${part.diameter}"
        holder.weightTextView.text = "Вес: ${part.weight}"

        holder.itemView.setOnLongClickListener {
            onItemLongClick(part)
            true
        }
        holder.itemView.setOnClickListener { onItemClick(part) }


        if (part.photo.isNotEmpty()) {
            Picasso.get()
                .load(part.photo)
                .fit()
                .centerCrop()
                .into(holder.photoImageView)
        } else {
            holder.photoImageView.setImageResource(R.drawable.no_image)
        }
    }

    override fun getItemCount() = partsList.size

    fun updateParts(parts: List<Part>) {
        this.partsList = parts
        notifyDataSetChanged()
    }
}
