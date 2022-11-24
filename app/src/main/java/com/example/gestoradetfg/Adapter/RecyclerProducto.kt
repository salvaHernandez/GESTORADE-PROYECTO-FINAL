package com.example.gestoradetfg.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.R

class RecyclerProducto (var context: AppCompatActivity, var listaUser:ArrayList<Producto>): RecyclerView.Adapter<RecyclerProducto.ViewHolder>() {


    override fun getItemCount(): Int {
        return this.listaUser?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.producto_card,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaUser[position]
        holder.bind(item, context, this)
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {


        fun bind(p: Producto, context: AppCompatActivity, adaptador: RecyclerProducto) {


        }
    }
}