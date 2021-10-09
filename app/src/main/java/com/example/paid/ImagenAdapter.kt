package com.example.paid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class ImagenAdapter(var context: Context): RecyclerView.Adapter<ImagenAdapter.ViewHolder>() {

    var dataList = emptyList<Imagen>()

    // Guarda el id del objeto en el que se hace click para asignarla al objeto en ElegirImagenCuenta
    var onItemClick: (Int) -> Unit = {}

    internal fun setDataList(dataList: List<Imagen>) {
        this.dataList = dataList
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imagen: ImageView = itemView.findViewById(R.id.imagen)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagenAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_layout, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.imagen.setImageResource(data.img)
        // Pasa el valor del id a la var onItemClick
        holder.imagen.setOnClickListener {
            onItemClick(data.img)
        }
    }
    override fun getItemCount() = dataList.size
}

