package com.example.gestoradetfg.Utils

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.gestoradetfg.*
import com.example.gestoradetfg.Adapter.RecyclerAddPedido
import com.example.gestoradetfg.Adapter.RecyclerHomePedido
import com.example.gestoradetfg.Adapter.RecyclerProducto
import com.example.gestoradetfg.Adapter.RecyclerProveedor
import com.example.gestoradetfg.MainActivity.Companion.db
import com.example.gestoradetfg.MainActivity.Companion.idUsuarioActivo
import com.example.gestoradetfg.Model.Pedido
import com.example.gestoradetfg.Model.Producto

import com.example.gestoradetfg.Model.Proveedor
import com.example.gestoradetfg.Model.Usuario
import com.firebase.ui.auth.data.model.User
import kotlinx.android.synthetic.main.fragment_producto.*
import kotlinx.coroutines.*

object AuxiliarDB {
    lateinit var adapterPedido: RecyclerHomePedido
    lateinit var adapterProveedor: RecyclerProveedor
    lateinit var adapterProducto: RecyclerProducto
    lateinit var adapterAddPedido: RecyclerAddPedido

    lateinit var listaPedidos: ArrayList<Pedido>
    lateinit var listaProveedores: ArrayList<Proveedor>
    lateinit var listaProductos: ArrayList<Producto>
    lateinit var listaProductoProveedor: ArrayList<Producto>
    lateinit var listaProductoPedido: ArrayList<Producto>
    lateinit var direcciones: ArrayList<String>


    fun initListas() {
        listaProveedores=arrayListOf()
        listaPedidos=arrayListOf()
        listaProductos=arrayListOf()
        listaProductoProveedor=arrayListOf()
        listaProductoPedido=arrayListOf()
        direcciones=arrayListOf()
    }


    fun getDirecciones() {
        db.collection(COLECCION_USUARIO).document(idUsuarioActivo).get()
            .addOnSuccessListener { result ->
                direcciones=result.get(PROV_DIRECCION) as ArrayList<String>
            }
    }



    fun getPedidos() {
        var pedido: Pedido
        var producto: Producto
        listaPedidos.clear()
        listaProductoPedido.clear()

        // LLamada a la bd para recoger los pedidos de un usuario
        db.collection(COLECCION_USUARIO).document(idUsuarioActivo).collection(COLECCION_PEDIDO)
            .whereEqualTo(PED_USUARIO, idUsuarioActivo)
            .get()
            .addOnSuccessListener { r_pedidos ->

                // Bucle que recorre cada pedido
                for (doc_pedido in r_pedidos) {

                    // Llamada a la bd que recoge todos los productos de el pedido
                    db.collection(COLECCION_PEDIDO).document(doc_pedido.id)
                        .collection(COLECCION_PRODUCTO_PEDIDO)
                        .get()
                        .addOnSuccessListener { r_producto ->

                            for (doc_producto in r_producto) {

                                // Creamos el objeto producto
                                producto=Producto(
                                    doc_producto.id,
                                    doc_producto.get(PROD_NOMBRE) as String,
                                    doc_producto.get(PROD_PRECIO) as Double,
                                    doc_producto.get(PROD_CALIDAD) as Double,
                                    doc_producto.get(PROD_TIPO_VENTA) as Boolean,
                                    doc_producto.get(PROD_PED_CANTIDAD) as Double,
                                    doc_producto.get(PROD_ID_PROVEEDOR) as String
                                )
                                // Lo añadimos a la lista que añadimos al proveedor
                                listaProductoPedido.add(producto)
                            }

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
                            adapterPedido.listaPedidos = listaPedidos
                            adapterPedido.notifyDataSetChanged()

                        }.addOnFailureListener { exception ->
                            Log.w(
                                ContentValues.TAG,
                                "Coleccion: " + COLECCION_PRODUCTO_PEDIDO + " Error: ",
                                exception
                            )
                        }
                }

            }.addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Coleccion: " + COLECCION_PEDIDO + " Error: ", exception)
            }
    }


    fun getProveedores() {
        var proveedor: Proveedor
        var producto: Producto

        listaProveedores.clear()

        // Llamada a la bd que recoge todos los proveedores de el usuario
        db.collection(COLECCION_USUARIO).document(idUsuarioActivo)
            .collection(COLECCION_PROVEEDOR).get().addOnSuccessListener { r_proveedor ->

                // Bucle que recorre cada proveedor
                for (doc_proveedor in r_proveedor) {

                    listaProductoProveedor.clear()

                    // Llamada a la bd que recoge todos los productos de el proveedor
                    db.collection(COLECCION_USUARIO).document(idUsuarioActivo)
                        .collection(COLECCION_PROVEEDOR).document(doc_proveedor.id)
                        .collection(COLECCION_PRODUCTO).get()
                        .addOnSuccessListener { r_productos ->

                            // Bucle que recorre cada producto
                            for (doc_producto in r_productos) {

                                // Creamos el producto
                                producto=Producto(
                                    doc_producto.id,
                                    doc_producto.get(PROD_NOMBRE) as String,
                                    doc_producto.get(PROD_PRECIO) as Double,
                                    doc_producto.get(PROD_CALIDAD) as Double,
                                    doc_producto.get(PROD_TIPO_VENTA) as Boolean,
                                    0.0,
                                    doc_proveedor.id
                                )

                                // Añadimos el producto a la lista de todos los productos
                                listaProductos.add(producto)

                                // Guardamos el producto en una lista
                                listaProductoProveedor.add(producto)
                            }

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

        db.collection(COLECCION_USUARIO).document(idUsuarioActivo).collection(COLECCION_PEDIDO)
            .add(pedidoData).addOnSuccessListener { result ->

            addProductosPedido(result.id)

            Toast.makeText(context, R.string.msgPedidoCrearSucc, Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(context, R.string.msgPedidoCrearNoSucc, Toast.LENGTH_SHORT).show()
        }

    }


    fun addProveedor(proveedor: Proveedor, context: Context) {

        var proveedorData=hashMapOf(
            PROV_DIRECCION to proveedor.direccion,
            PROV_EMAIL to proveedor.email,
            PROV_NOMBRE to proveedor.nombre,
            PROV_OBSERVACIONES to proveedor.observaciones,
            PROV_TELEFONO to proveedor.telefono,
            PROV_TIEMPO_ENVIO to proveedor.tiempoEnvio,
            PROV_VALORACION to proveedor.valoracion
        )

        db.collection(COLECCION_USUARIO).document(idUsuarioActivo).collection(COLECCION_PROVEEDOR)
            .add(proveedorData).addOnSuccessListener { result ->

                proveedor.id=result.id
                listaProveedores.add(proveedor)
                adapterProveedor.listaProveedores=listaProveedores
                adapterProveedor.notifyDataSetChanged()
                Toast.makeText(context, R.string.msgProveedorCrearSucc, Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(context, R.string.msgProveedorCrearNoSucc, Toast.LENGTH_SHORT).show()
            }

    }


    fun modProveedor(proveedor: Proveedor, context: Context) {
        var proveedorData=hashMapOf(
            PROV_DIRECCION to proveedor.direccion,
            PROV_EMAIL to proveedor.email,
            PROV_NOMBRE to proveedor.nombre,
            PROV_OBSERVACIONES to proveedor.observaciones,
            PROV_TELEFONO to proveedor.telefono,
            PROV_TIEMPO_ENVIO to proveedor.tiempoEnvio,
            PROV_VALORACION to proveedor.valoracion
        )
        db.collection(COLECCION_USUARIO).document(idUsuarioActivo).collection(COLECCION_PROVEEDOR)
            .document(proveedor.id).set(proveedorData)
            .addOnSuccessListener {

                adapterProveedor.notifyDataSetChanged()
                Toast.makeText(context, R.string.msgProveedorModSucc, Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(context, R.string.msgProveedorModNoSucc, Toast.LENGTH_SHORT).show()
            }
    }



    fun addUsuario(usuario: Usuario, context: Context) {

        var userData=hashMapOf(
            USUARIO_DIRECCION to usuario.direccion,
            USUARIO_NIF to usuario.nif,
            USUARIO_NOMBRE to usuario.nombre,
            USUARIO_TELEFONO to usuario.telefono,
            USUARIO_EMAIL to usuario.email,
        )

        db.collection(COLECCION_USUARIO).document(usuario.telefono)
            .set(userData).addOnSuccessListener {

                Toast.makeText(context, R.string.msgUsuarioCrearSucc, Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, R.string.msgUsuarioCrearNoSucc, Toast.LENGTH_SHORT).show()
            }
    }



    fun addProducto(producto: Producto, context: Context) {

        var productoData=hashMapOf(
            PROD_ID_PROVEEDOR to producto.idProvedoor,
            PROD_TIPO_VENTA to producto.tipoVenta,
            PROD_CALIDAD to producto.calidad,
            PROD_PRECIO to producto.precio,
            PROD_NOMBRE to producto.nombre,
            PROD_PED_CANTIDAD to null,
        )

        db.collection(COLECCION_USUARIO).document(idUsuarioActivo)
            .collection(COLECCION_PROVEEDOR).document(producto.idProvedoor)
            .collection(COLECCION_PRODUCTO).add(productoData).addOnSuccessListener { result ->

                producto.id=result.id
                listaProductos.add(producto)
                adapterProducto.listaProducto=listaProductos
                adapterProducto.notifyDataSetChanged()

                Toast.makeText(context, R.string.msgProductoCrearSucc, Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, R.string.msgProductoCrearNoSucc, Toast.LENGTH_SHORT).show()
            }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun borrarProveedor(p: Proveedor, context: AppCompatActivity) {
        db.collection(COLECCION_USUARIO).document(idUsuarioActivo).collection(
            COLECCION_PROVEEDOR
        ).document(p.id).delete().addOnSuccessListener {

            listaProveedores.remove(p)
            adapterProveedor.listaProveedores=listaProveedores
            adapterProveedor.notifyDataSetChanged()

            Log.e("Salva", "ListaProductos :" + listaProductos.toString())

            listaProductos.removeIf { prod -> (prod.idProvedoor == p.id) }
            Log.e("Salva", "ListaProductos :" + listaProductos.toString())
        }.addOnFailureListener {
            Toast.makeText(context, R.string.msgBorrarProveedorNoSucc, Toast.LENGTH_SHORT).show()
        }
    }


    fun borrarProducto(p: Producto, context: AppCompatActivity) {


        db.collection(COLECCION_USUARIO).document(idUsuarioActivo).collection(
            COLECCION_PROVEEDOR
        ).document(p.idProvedoor).collection(COLECCION_PRODUCTO).document(p.id).delete()
            .addOnSuccessListener {

                listaProductos.remove(p)
                adapterProducto.listaProducto=listaProductos
                adapterProducto.notifyDataSetChanged()

            }.addOnFailureListener {
                Toast.makeText(context, R.string.msgBorrarProveedorNoSucc, Toast.LENGTH_SHORT)
                    .show()
            }

    }


    fun modProducto(producto: Producto, oldProducto: Producto, context: Context) {


        var productoData=hashMapOf(
            PROD_ID_PROVEEDOR to producto.idProvedoor,
            PROD_TIPO_VENTA to producto.tipoVenta,
            PROD_CALIDAD to producto.calidad,
            PROD_PRECIO to producto.precio,
            PROD_NOMBRE to producto.nombre,
            PROD_PED_CANTIDAD to null,
        )


        db.collection(COLECCION_USUARIO).document(idUsuarioActivo).collection(COLECCION_PROVEEDOR)
            .document(producto.idProvedoor).collection(
                COLECCION_PRODUCTO
            ).document(producto.id).set(productoData)
            .addOnSuccessListener {

                adapterProducto.notifyDataSetChanged()
                Toast.makeText(context, R.string.msgProductoModSucc, Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(context, R.string.msgProductoModNoSucc, Toast.LENGTH_SHORT).show()
            }


    }


    private fun addProductosPedido(id: String) {

        for (i in 0..listaProductoPedido.size - 1) {

            var productoData=hashMapOf(
                PROD_ID_PROVEEDOR to listaProductoPedido[i].idProvedoor,
                PROD_TIPO_VENTA to listaProductoPedido[i].tipoVenta,
                PROD_CALIDAD to listaProductoPedido[i].calidad,
                PROD_PRECIO to listaProductoPedido[i].precio,
                PROD_NOMBRE to listaProductoPedido[i].nombre,
                PROD_PED_CANTIDAD to listaProductoPedido[i].cantidad,
            )

            db.collection(COLECCION_USUARIO).document(idUsuarioActivo)
                .collection(COLECCION_PEDIDO).document(id)
                .collection(COLECCION_PRODUCTO_PEDIDO).add(productoData).addOnSuccessListener {

                }

        }
    }

}