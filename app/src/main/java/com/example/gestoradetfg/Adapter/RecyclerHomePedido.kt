package com.example.gestoradetfg.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoradetfg.Model.Pedido
import com.example.gestoradetfg.R
import com.example.gestoradetfg.Utils.AuxiliarDB

class RecyclerHomePedido (var context: AppCompatActivity, var listaPedidos:ArrayList<Pedido>) : RecyclerView.Adapter<RecyclerHomePedido.ViewHolder>(){


    override fun getItemCount(): Int {
        return this.listaPedidos?.size!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.pedido_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaPedidos[position]
        holder.bind(item, context)
    }



    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val precioFinal = view.findViewById<TextView>(R.id.t_ped_precio_final)
        val diaEntrega = view.findViewById<TextView>(R.id.t_ped_dia_entrega)
        val direccion = view.findViewById<TextView>(R.id.t_ped_direccion_card)
        val proveedor = view.findViewById<TextView>(R.id.t_ped_proveedor_card)


        fun bind(p: Pedido, context: AppCompatActivity) {

            direccion.text = p.direccionDeEnvio
            precioFinal.text = p.precioFinal.toString()
            proveedor.text = getNombreProv(p.proveedor)
            

            // Si clicka hacer...
            itemView.setOnClickListener {


            }

        }
        private fun getNombreProv(idProvedoor: String): String {

            for (i in 0..AuxiliarDB.listaProveedores.size-1) {
                if (AuxiliarDB.listaProveedores[i].id == idProvedoor) {
                    return AuxiliarDB.listaProveedores[i].nombre
                }
            }
            return ""
        }

    }
}