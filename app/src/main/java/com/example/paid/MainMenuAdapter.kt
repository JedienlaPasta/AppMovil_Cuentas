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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class MainMenuAdapter(var context: Context): RecyclerView.Adapter<MainMenuAdapter.ViewHolder>()
{
    private var dataList = emptyList<MainMenu>()

    var onItemClick: (Int) -> Unit = {}

    internal fun setDataList(dataList: List<MainMenu>) {
        this.dataList = dataList
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imagen: ImageView = itemView.findViewById(R.id.imagen)
        val titulo: TextView = itemView.findViewById(R.id.titulo)
        val descripcion: TextView = itemView.findViewById(R.id.descripcion)
        val cardview: CardView = itemView.findViewById(R.id.cardview)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMenuAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_menu_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.titulo.text = data.texto
        holder.descripcion.text = data.descripcion
        holder.imagen.setImageResource(data.img)

        holder.cardview.setOnClickListener {
            onItemClick(position)
        }
    }
    override fun getItemCount() = dataList.size
}