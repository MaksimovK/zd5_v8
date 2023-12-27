package com.example.zd5_v8.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_v8.R
import com.squareup.picasso.Picasso

class FurnitureAdapter(
    private var furnitureList: List<Furniture>,
    private val onItemClick: (Furniture) -> Unit,
    private val onItemLongClick: (Furniture) -> Unit
) : RecyclerView.Adapter<FurnitureAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.furniture_name)
        val descriptionTextView: TextView = view.findViewById(R.id.furniture_description)
        val costTextView: TextView = view.findViewById(R.id.furniture_cost)
        val photoImageView: ImageView = view.findViewById(R.id.furniture_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_furniture, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val furniture = furnitureList[position]

        holder.itemView.setOnClickListener { onItemClick(furniture) }
        holder.itemView.setOnLongClickListener { onItemLongClick(furniture); true }

        holder.nameTextView.text = furniture.name
        holder.descriptionTextView.text = furniture.description
        holder.costTextView.text = "${furniture.cost} Руб"

        if (!furniture.photo.isNullOrEmpty()) {
            Picasso.get()
                .load(furniture.photo)
                .fit()
                .centerCrop()
                .into(holder.photoImageView)
        } else {
            holder.photoImageView.setImageResource(R.drawable.no_image)
        }
    }

    override fun getItemCount(): Int = furnitureList.size

    fun updateData(newFurnitureList: List<Furniture>) {
        furnitureList = newFurnitureList
        notifyDataSetChanged()
    }
}

