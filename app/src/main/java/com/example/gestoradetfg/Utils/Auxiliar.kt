package com.example.gestoradetfg.Utils

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.gestoradetfg.*
import com.example.gestoradetfg.Adapter.RecyclerHomePedido
import com.example.gestoradetfg.Adapter.RecyclerProducto
import com.example.gestoradetfg.Adapter.RecyclerProveedor
import com.example.gestoradetfg.MainActivity.Companion.db
import com.example.gestoradetfg.Model.Pedido
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.Model.Proveedor
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.HashMap

object Auxiliar {
    lateinit var adapterPedido: RecyclerHomePedido
    lateinit var adapterProveedor: RecyclerProveedor
    lateinit var adapterProducto: RecyclerProducto
    lateinit var listaPedidos: ArrayList<Pedido>
    lateinit var listaProveedores: ArrayList<Proveedor>

    lateinit var listaProductos: ArrayList<Producto>
    lateinit var listaProductoProveedor: ArrayList<Producto>
    lateinit var listaProductoPedido: ArrayList<Producto>


    fun getPedidos(idUsuario: String) {
        var pedido: Pedido
        var producto: Producto
        listaPedidos.clear()
        listaProductoPedido.clear()

        // LLamada a la bd para recoger los pedidos de un usuario
        db.collection(COLECCION_PEDIDO)
            .whereEqualTo(PED_USUARIO, idUsuario)
            .get()
            .addOnSuccessListener { r_pedidos ->

                // Bucle que recorre cada pedido
                for (doc_pedido in r_pedidos) {

                    // Llamada a la bd que recoge todos los productos de el pedido
                    db.collection(COLECCION_PEDIDO).document(doc_pedido.id)
                        .collection(COLECCION_PEDIDO_PRODUCTO)
                        .get()
                        .addOnSuccessListener { r_producto ->

                            for (doc_producto in r_producto) {

                                Log.e("Salva", "Producto:  " + doc_producto.id)
                                Log.e("Salva", doc_producto.get(PROD_NOMBRE) as String)
                                Log.e("Salva", doc_producto.get(PROD_PRECIO).toString())
                                Log.e("Salva", doc_producto.get(PROD_CALIDAD).toString())
                                Log.e("Salva", doc_producto.get(PROD_TIPO_VENTA).toString())
                                Log.e("Salva", doc_producto.get(PROD_PED_CANTIDAD).toString())


                                // Creamos el objeto producto
                                producto=Producto(
                                    doc_producto.id,
                                    doc_producto.get(PROD_NOMBRE) as String,
                                    doc_producto.get(PROD_PRECIO) as Double,
                                    doc_producto.get(PROD_CALIDAD) as Long,
                                    doc_producto.get(PROD_TIPO_VENTA) as Boolean,
                                    doc_producto.get(PROD_PED_CANTIDAD) as Double
                                )
                                // Lo añadimos a la lista
                                listaProductoPedido.add(producto)
                            }
                            Log.e("Salva", "Pedido:  " + doc_pedido.id)
                            Log.e("Salva", doc_pedido.get(PED_USUARIO) as String)
                            Log.e("Salva", doc_pedido.get(PED_PROVEEDOR) as String)
                            Log.e("Salva", doc_pedido.get(PED_DIRECCION) as String)
                            Log.e("Salva", doc_pedido.get(PED_PRECIO).toString())
                            Log.e("Salva", doc_pedido.get(PED_TIEMPO_ENVIO).toString())
                            Log.e("Salva", doc_pedido.get(PED_RECIBIDO).toString())

                            // Creamos el pedido
                            pedido=Pedido(
                                doc_pedido.id,
                                doc_pedido.get(PED_USUARIO) as String,
                                doc_pedido.get(PED_PROVEEDOR) as String,
                                doc_pedido.get(PED_DIRECCION) as String,
                                listaProductoPedido,
                                doc_pedido.get(PED_PRECIO) as Double,
                                doc_pedido.get(PED_TIEMPO_ENVIO) as Long,
                                doc_pedido.get(PED_RECIBIDO) as Boolean
                            )
                            // Añadimos el pedido a la lista
                            listaPedidos.add(pedido)
                        }.addOnFailureListener { exception ->
                            Log.w(
                                ContentValues.TAG,
                                "Coleccion: " + COLECCION_PEDIDO_PRODUCTO + " Error: ",
                                exception
                            )
                        }
                }
            }.addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Coleccion: " + COLECCION_PEDIDO + " Error: ", exception)
            }
        Log.e("Salva", listaPedidos.toString())
    }


    fun getProveedoresFromUsuario(idUsuario: String) {
        var proveedor: Proveedor
        var producto: Producto

        listaProveedores.clear()

        // Llamada a la bd que recoge todos los proveedores de el usuario
        db.collection(COLECCION_USUARIO).document(idUsuario)
            .collection(COLECCION_PROVEEDOR).get().addOnSuccessListener { r_proveedor ->

                // Bucle que recorre cada proveedor
                for (doc_proveedor in r_proveedor) {

                    // Llamada a la bd que recoge todos los productos de el proveedor
                    db.collection(COLECCION_USUARIO).document(idUsuario)
                        .collection(COLECCION_PROVEEDOR).document(doc_proveedor.id)
                        .collection(COLECCION_PRODUCTO).get()
                        .addOnSuccessListener { r_productos ->

                            // Bucle que recorre cada producto
                            for (doc_producto in r_productos) {

                                Log.e("Salva", "Producto del Proveedor:  " + doc_producto.id)
                                Log.e("Salva", doc_producto.get(PROD_NOMBRE) as String)
                                Log.e("Salva", doc_producto.get(PROD_PRECIO).toString())
                                Log.e("Salva", doc_producto.get(PROD_CALIDAD).toString())
                                Log.e("Salva", doc_producto.get(PROD_TIPO_VENTA).toString())
                                Log.e("Salva", doc_producto.get(PROD_PED_CANTIDAD).toString())


                                // Creamos el producto
                                producto=Producto(
                                    doc_producto.id,
                                    doc_producto.get(PROD_NOMBRE) as String,
                                    doc_producto.get(PROD_PRECIO) as Double,
                                    doc_producto.get(PROD_CALIDAD) as Long,
                                    doc_producto.get(PROD_TIPO_VENTA) as Boolean,
                                    0.0
                                )
                                // Guardamos el producto en una lista
                                listaProductoProveedor.add(producto)
                            }

                            Log.e("Salva", "Proveedor:  " + doc_proveedor.id)
                            Log.e("Salva", doc_proveedor.get(PROV_NOMBRE) as String)
                            Log.e("Salva", doc_proveedor.get(PROV_DIRECCION) as String)
                            Log.e("Salva", doc_proveedor.get(PROV_EMAIL) as String)
                            Log.e("Salva", doc_proveedor.get(PROV_TELEFONO).toString())
                            Log.e("Salva", doc_proveedor.get(PROV_TIEMPO_ENVIO).toString())
                            Log.e("Salva", doc_proveedor.get(PROV_VALORACION).toString())
                            Log.e("Salva", doc_proveedor.get(PROV_OBSERVACIONES).toString())
                            Log.e("Salva", listaProductoProveedor.toString())

                            // Creamos el proveedor y le añadimos su lista de productos anteriormente creada
                            proveedor=Proveedor(
                                doc_proveedor.id,
                                doc_proveedor.get(PROV_NOMBRE) as String,
                                doc_proveedor.get(PROV_DIRECCION) as String,
                                doc_proveedor.get(PROV_EMAIL) as String,
                                doc_proveedor.get(PROV_TELEFONO) as Long,
                                doc_proveedor.get(PROV_TIEMPO_ENVIO) as Long,
                                doc_proveedor.get(PROV_VALORACION) as Long,
                                doc_proveedor.get(PROV_OBSERVACIONES) as String,
                                listaProductoProveedor
                            )
                            // Añadimos el proveedor a la lista de proveedores
                            listaProveedores.add(proveedor)
                        }.addOnFailureListener { exception ->
                            Log.w(
                                ContentValues.TAG,
                                "Coleccion: " + COLECCION_PRODUCTO + " Error: ",
                                exception
                            )
                        }
                }
            }.addOnFailureListener { exception ->
                Log.w(
                    ContentValues.TAG,
                    "Coleccion: " + COLECCION_PROVEEDOR + " Error: ",
                    exception
                )
            }


    }


    fun addPedido(pedido: Pedido, context: Context) {

        var pedidoData=hashMapOf(
            PED_DIRECCION to pedido.direccionDeEnvio,
            PED_PRECIO to pedido.precioFinal,
            PED_PROVEEDOR to pedido.proveedor,
            PED_TIEMPO_ENVIO to pedido.tiempoEnvio,
            PED_USUARIO to pedido.usuario,
            PED_RECIBIDO to false
        )

        db.collection(COLECCION_PEDIDO).add(pedidoData).addOnSuccessListener {
            Toast.makeText(context, R.string.msgPedidoCrearSucc, Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(context, R.string.msgPedidoCrearNoSucc, Toast.LENGTH_SHORT).show()
        }

    }


    fun modProveedor(proveedor: Proveedor, context: Context) {
        var proveedorData=hashMapOf(
            PROV_DIRECCION to proveedor.direccion,
            PROV_EMAIL to proveedor.emai,
            PROV_NOMBRE to proveedor.nombre,
            PROV_OBSERVACIONES to proveedor.observaciones,
            PROV_TELEFONO to proveedor.telefono,
            PROV_TIEMPO_ENVIO to proveedor.tiempoEnvio,
            PROV_VALORACION to proveedor.valoracion
        )
        db.collection(COLECCION_PROVEEDOR).document(proveedor.id).set(proveedorData)
            .addOnSuccessListener {
                Toast.makeText(context, R.string.msgProveedorModSucc, Toast.LENGTH_SHORT).show()
                adapterProveedor.notifyDataSetChanged()

            }.addOnFailureListener {
            Toast.makeText(context, R.string.msgProveedorModNoSucc, Toast.LENGTH_SHORT).show()
        }
    }


    fun addProveedor(proveedor: Proveedor, context: Context) {

        var proveedorData=hashMapOf(
            PROV_DIRECCION to proveedor.direccion,
            PROV_EMAIL to proveedor.emai,
            PROV_NOMBRE to proveedor.nombre,
            PROV_OBSERVACIONES to proveedor.observaciones,
            PROV_TELEFONO to proveedor.telefono,
            PROV_TIEMPO_ENVIO to proveedor.tiempoEnvio,
            PROV_VALORACION to proveedor.valoracion
        )

        db.collection(COLECCION_PROVEEDOR).add(proveedorData).addOnSuccessListener {
            Toast.makeText(context, R.string.msgProveedorCrearSucc, Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(context, R.string.msgProveedorCrearNoSucc, Toast.LENGTH_SHORT).show()
        }

    }


    fun modProducto(producto: Producto, context: Context) {

        var productoData=hashMapOf(
            PROD_TIPO_VENTA to producto.tipoVenta,
            PROD_CALIDAD to producto.calidad,
            PROD_PRECIO to producto.precio,
            PROD_NOMBRE to producto.nombre,
            PROD_PED_CANTIDAD to null,
        )
        db.collection(COLECCION_PRODUCTO).document(producto.id).set(productoData)
            .addOnSuccessListener {
                Toast.makeText(context, R.string.msgProductoModSucc, Toast.LENGTH_SHORT).show()
                adapterProveedor.notifyDataSetChanged()

            }.addOnFailureListener {
            Toast.makeText(context, R.string.msgProductoModNoSucc, Toast.LENGTH_SHORT).show()
        }

    }


    fun addProducto(producto: Producto, context: Context) {

        var productoData=hashMapOf(
            PROD_TIPO_VENTA to producto.tipoVenta,
            PROD_CALIDAD to producto.calidad,
            PROD_PRECIO to producto.precio,
            PROD_NOMBRE to producto.nombre,
            PROD_PED_CANTIDAD to null,
        )

        db.collection(COLECCION_PEDIDO).add(productoData).addOnSuccessListener {
            Toast.makeText(context, R.string.msgProductoCrearSucc, Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(context, R.string.msgProductoCrearNoSucc, Toast.LENGTH_SHORT).show()
        }

    }


    private fun mapPedido(pedido: Pedido)  {

        var pedidoData=hashMapOf(
            PED_DIRECCION to pedido.direccionDeEnvio,
            PED_PRECIO to pedido.precioFinal,
            PED_PROVEEDOR to pedido.proveedor,
            PED_TIEMPO_ENVIO to pedido.tiempoEnvio,
            PED_USUARIO to pedido.usuario,
            PED_RECIBIDO to false
        )
    }


}