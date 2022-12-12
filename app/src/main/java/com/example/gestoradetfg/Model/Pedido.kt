package com.example.gestoradetfg.Model

import android.provider.Settings.Secure.getString
import android.util.Log
import com.example.gestoradetfg.R
import java.io.Serializable

data class Pedido(var id: String,
                  var usuario: String,
                  var proveedor: String,
                  var direccionDeEnvio: String,
                  var productos: ArrayList<Producto>,
                  var precioFinal : Double,
                  var tiempoEnvio: Long,
                  var recibido: Boolean,
                  var llegadaPedido:String): Serializable {


     fun sms(): String {


        var msg : String
        var msg2 : String

//        msg = R.string.msgPedidoA.toString() +" "+this.direccionDeEnvio +"\n"
        msg = "Buenas, enviame estos productos a la direccion " +this.direccionDeEnvio +"\n"


        for (i in 0..productos.size-1) {

            if (productos[i].tipoVenta) {
                msg2 = msg+ "\t\t\t\t"+productos[i].nombre +"\t "+ productos[i].cantidad.toInt().toString()+" Ud\n "
            } else {
                msg2 = msg+ "\t\t\t\t"+productos[i].nombre + "\t "+ productos[i].cantidad.toString()+" Kg\n "
            }
            msg = msg2
        }

        msg2 = msg + "Si hay algun problema contactame cuanto antes."+"\n" + "Gracias, un saludo"
//         msg2 = msg + R.string.msgDespedidaPed.toString()+"\n" + R.string.msgDespedidaPed

        return msg2

    }
}