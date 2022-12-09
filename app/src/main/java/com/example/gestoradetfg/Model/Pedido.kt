package com.example.gestoradetfg.Model

import com.example.gestoradetfg.R
import java.io.Serializable

data class Pedido(var id: String,
                  var usuario: String,
                  var proveedor: String,
                  var direccionDeEnvio: String,
                  var productos: ArrayList<Producto>,
                  var precioFinal : Double,
                  var tiempoEnvio: Long,
                  var recibido: Boolean): Serializable {


    override fun toString(): String {

        var msg : String
        var msg2 : String
        msg = R.string.msgPedidoA.toString() +" "+this.direccionDeEnvio +"\r"

        for (i in 0..productos.size-1) {
            msg2 = msg+productos[i].nombre + " Cantidad: " + productos[i].cantidad.toString()+"\r"
            msg = msg2
        }

        msg2 = msg + R.string.msgDespedidaPed.toString()+"\r" + R.string.msgDespedidaPed

        return msg2

    }
}