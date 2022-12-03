package com.example.gestoradetfg.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.Model.Proveedor
import com.example.gestoradetfg.R

class RecyclerProducto (var context: AppCompatActivity, var listaProducto:ArrayList<Producto>): RecyclerView.Adapter<RecyclerProducto.ViewHolder>() {


    override fun getItemCount(): Int {
        return this.listaProducto?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.producto_card,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaProducto[position]
        holder.bind(item, context, this)
    }

    fun updateProductos (listFiltrada : List<Producto>) {
        this.listaProducto = ArrayList(listFiltrada)
        notifyDataSetChanged()
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nombre = view.findViewById<TextView>(R.id.txtCard_prod_nombre)


        fun bind(p: Producto, context: AppCompatActivity, adaptador: RecyclerProducto) {
            nombre.text = p.nombre

        }
    }
}