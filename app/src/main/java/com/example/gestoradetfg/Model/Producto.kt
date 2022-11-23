package com.example.gestoradetfg.Model

import java.io.Serializable

data class Producto(var id: String, var proveedor: Proveedor, var productos: ArrayList<Producto>,
                    var precios : ArrayList<Double>, var cantidad : ArrayList<Double>, var admin: Boolean,
                    var verificado: Boolean): Serializable
