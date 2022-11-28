package com.example.gestoradetfg.Model

import java.io.Serializable

data class Pedido(var id: String,
                  var usuario: String,
                  var proveedor: String,
                  var direccionDeEnvio: String,
                  var productos: ArrayList<Producto>,
                  var precioFinal : Double,
                  var tiempoEnvio: Long,
                  var recibido: Boolean): Serializable