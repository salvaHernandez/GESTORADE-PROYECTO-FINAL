package com.example.gestoradetfg.Utils

import com.example.gestoradetfg.Adapter.RecyclerHomePedido
import com.example.gestoradetfg.Adapter.RecyclerProducto
import com.example.gestoradetfg.Adapter.RecyclerProveedor
import com.example.gestoradetfg.Model.Pedido
import com.example.gestoradetfg.Model.Proveedor
import com.example.gestoradetfg.UsuarioActivity.Companion.listaPedidos

object Auxiliar {
    lateinit var miAdapterPedido : RecyclerHomePedido
    lateinit var miAdapterProveedor : RecyclerProveedor
    lateinit var miAdapterProducto : RecyclerProducto


    fun getPedidos(todos : Boolean) {

        var p : Pedido?= null

        listaPedidos.clear()
        listaPedidos.add(p!!)
        //Coger todos los elementos de la colección.


    }
}