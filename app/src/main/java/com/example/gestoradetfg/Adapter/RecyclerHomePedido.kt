package com.example.gestoradetfg.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoradetfg.Model.Pedido
import com.example.gestoradetfg.R

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

        val idPedido = view.findViewById<TextView>(R.id.t_ped_id_card)
        val direccion = view.findViewById<TextView>(R.id.t_ped_direccion_card)



        fun bind(p: Pedido, context: AppCompatActivity) {

            idPedido.text = p.id
            direccion.text = p.direccionDeEnvio
            itemView.isEnabled = p.recibido

            // Si clicka hacer...
            itemView.setOnClickListener {


            }

        }
    }
}