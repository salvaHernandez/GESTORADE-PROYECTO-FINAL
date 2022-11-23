package com.example.gestoradetfg.Model


import java.io.Serializable

data class Proveedor(var nombre: String, var direccion:String, var productos: ArrayList<Producto>, var tiempoEnvio: Int,
                     var telefono: Int, var emai: String, var precios: ArrayList<Double>, var admin: Boolean, var valoracion: Int): Serializable
