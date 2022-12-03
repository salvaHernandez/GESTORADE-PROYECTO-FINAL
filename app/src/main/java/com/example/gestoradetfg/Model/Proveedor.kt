package com.example.gestoradetfg.Model


import java.io.Serializable

data class Proveedor(var id : String,
                     var nombre: String,
                     var direccion:String,
                     var email: String,
                     var telefono: Long,
                     var tiempoEnvio: Long,
                     var valoracion: Long,
                     var observaciones: String,
                     var productos: ArrayList<Producto>): Serializable
