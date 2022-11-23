package com.example.gestoradetfg.Model

import java.io.Serializable

data class Pedido(var id: String, var proveedor: Proveedor, var productos: ArrayList<Producto>, var cantidad: ArrayList<Double>,
                  var tiempoEnvio: Int, var precioFinal : Double, var direccionDeEnvio: String, var recibido: Boolean):
    Serializable