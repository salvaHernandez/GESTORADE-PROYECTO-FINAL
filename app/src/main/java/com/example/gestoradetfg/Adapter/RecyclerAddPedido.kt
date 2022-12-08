package com.example.gestoradetfg.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.R

class RecyclerAddPedido(var context : AppCompatActivity, var list_prod_prov:List<Producto>): RecyclerView.Adapter<RecyclerAddPedido.ViewHolder>() {

    override fun getItemCount(): Int {
        return this.list_prod_prov?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.add_pedido_card,parent,false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list_prod_prov[position]
        holder.bind(item, context, this)

    }

    fun updateProductos (listFiltrada : List<Producto>) {
        this.list_prod_prov = ArrayList(listFiltrada)
        notifyDataSetChanged()
    }




    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val idPedido = view.findViewById<TextView>(R.id.t_add_ped_id_card)


        fun bind(p: Producto, context: AppCompatActivity, adaptador: RecyclerAddPedido) {
            idPedido.text = p.nombre
        }

    }


}