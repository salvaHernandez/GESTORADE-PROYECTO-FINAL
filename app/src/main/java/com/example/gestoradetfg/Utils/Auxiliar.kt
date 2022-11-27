package com.example.gestoradetfg.Utils

import android.content.ContentValues
import android.util.Log
import com.example.gestoradetfg.*
import com.example.gestoradetfg.Adapter.RecyclerHomePedido
import com.example.gestoradetfg.Adapter.RecyclerProducto
import com.example.gestoradetfg.Adapter.RecyclerProveedor
import com.example.gestoradetfg.MainActivity.Companion.db
import com.example.gestoradetfg.Model.Pedido
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.Model.Proveedor

object Auxiliar {
    lateinit var miAdapterPedido : RecyclerHomePedido
    lateinit var miAdapterProveedor : RecyclerProveedor
    lateinit var miAdapterProducto : RecyclerProducto
    lateinit var listaPedidos : ArrayList<Pedido>
    lateinit var listaProveedores : ArrayList<Proveedor>
    lateinit var listaProductos : ArrayList<Producto>
    lateinit var listaProductoProveedor : ArrayList<Producto>



    fun addProducto (p:Producto) {
        

    }

    fun getPedidos(todos : Boolean) {



    }

    fun getProveedores() {
        var proveedor : Proveedor
        var producto : Producto

        listaProveedores.clear()

         db.collection(COLECCION_USUARIO)
            .get()
            .addOnSuccessListener { r_usuario ->

                for (documentUsuario in r_usuario) {

                    db.collection(COLECCION_USUARIO).document(documentUsuario.id).collection(COLECCION_PROVEEDOR).get().addOnSuccessListener { r_proveedor ->

                        for (documentProv in r_proveedor) {

                            db.collection(COLECCION_USUARIO).document(documentUsuario.id).collection(COLECCION_PROVEEDOR).document(documentProv.id)
                                .collection(COLECCION_PRODUCTO).get().addOnSuccessListener { r_productos ->

                                    for (documentProd in r_productos) {
                                        producto = Producto (
                                            documentProd.id,
                                            documentProd.get(PROD_NOMBRE) as String,
                                            documentProd.get(PROD_PRECIO) as Double,
                                            documentProd.get(PROD_CALIDAD) as Long,
                                            documentProd.get(PROD_TIPO_VENTA) as Boolean
                                        )
                                        listaProductoProveedor.add(producto)
                                    }
                                    proveedor = Proveedor(
                                        documentProv.id,
                                        documentProv.get(PROV_NOMBRE) as String,
                                        documentProv.get(PROV_DIRECCION) as String,
                                        documentProv.get(PROV_EMAIL) as String,
                                        documentProv.get(PROV_TELEFONO) as Long,
                                        documentProv.get(PROV_TIEMPO_ENVIO) as Long,
                                        documentProv.get(PROV_VALORACION) as Long,
                                        documentProv.get(PROV_OBSERVACIONES) as String,
                                        listaProductoProveedor
                                    )
                                    listaProveedores.add(proveedor)
                                }
                        }
                    }.addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Sub coleccion Error: ", exception)
                    }
                    miAdapterProveedor.notifyDataSetChanged()
                }

            }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Coleccion Error: ", exception)
            }
    }


}