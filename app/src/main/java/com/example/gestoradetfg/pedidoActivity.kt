package com.example.gestoradetfg


import android.app.AlertDialog
import com.example.gestoradetfg.R
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestoradetfg.Adapter.RecyclerAddPedido
import com.example.gestoradetfg.MainActivity.Companion.idUsuarioActivo
import com.example.gestoradetfg.Model.Pedido
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.Utils.AuxiliarDB
import com.example.gestoradetfg.Utils.AuxiliarDB.adapterAddPedido
import com.example.gestoradetfg.Utils.AuxiliarDB.addPedido
import com.example.gestoradetfg.Utils.AuxiliarDB.listaPedidos
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProductoPedido
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProductoProveedor
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProductos
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProveedores
import com.example.gestoradetfg.databinding.ActivityPedidoBinding
import kotlinx.android.synthetic.main.activity_pedido.*


private lateinit var binding : ActivityPedidoBinding
private var filtroNombre : String= ""
private lateinit var listaFiltradaProductos : List<Producto>
private lateinit var vistaTitulo : TextView

class pedidoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setTitle(R.string.act_add_pedido)

        val bundle = intent.extras
        val idProv = bundle?.getString("id")!!
        val direccion = bundle?.getString("direccion")!!
        val tiempoEnvio = bundle?.getLong("tiempoEnvio")!!
        val metodoEnvio = bundle?.getInt("metodo")!!

        listaProductoProveedor.clear()

        var pedido = datosPedido(direccion, idProv, tiempoEnvio)
        pedido.productos.clear()
        listaProductoPedido.clear()
        getProductosProveedor(idProv!!)



        recyclerAddPedido.setHasFixedSize(true)
        recyclerAddPedido.layoutManager = LinearLayoutManager(this)
        adapterAddPedido = RecyclerAddPedido(this, listaProductoProveedor, pedido)
        recyclerAddPedido.adapter = adapterAddPedido


        binding.etFilterProductoPedido.addTextChangedListener { userFilter ->
            filtroNombre = userFilter.toString()
            filtraLista()
            adapterAddPedido.updateProductos(listaFiltradaProductos)

        }


        binding.floatBtnCesta.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pedido")
            var viewFormulario = layoutInflater.inflate(R.layout.model_pedido, null)


            incializaVista(viewFormulario, pedido)
            builder.setView(viewFormulario)

            builder.setPositiveButton(R.string.enviar ) {view, _  ->
                addPedido(pedido, this)
                if (metodoEnvio == 1) {
                    enviarPorWhastsapp(pedido)
                } else {
                    enviarPorMail(pedido)
                }
                onBackPressed()
            }

            builder.setNegativeButton(R.string.cancelar) { view, _ ->

            }

            builder.show()

        }

    }


    private fun incializaVista(view: View, p : Pedido) {

        vistaTitulo = view.findViewById(R.id.txt_pedido)
        vistaTitulo.text = p.sms()

    }

    private fun datosPedido(dir_envio : String, idProv: String, tiempoEnvio : Long): Pedido {
        return Pedido("",idUsuarioActivo, idProv, dir_envio, listaProductoProveedor,0.0, tiempoEnvio,false)
    }

    private fun filtraLista() {
        listaFiltradaProductos = listaProductoProveedor.filter { producto ->
            producto.nombre.lowercase().contains(binding.etFilterProductoPedido.text.toString().lowercase())
        }
    }

    fun getProductosProveedor(idProv : String) {
        listaProductoProveedor = listaProductos.filter { producto ->
            producto.idProvedoor == idProv
        } as ArrayList<Producto>
    }


    private fun enviarPorWhastsapp(p: Pedido) {

        var telefono = getTelefonoProv(p.proveedor)
        val intent = Intent(Intent.ACTION_VIEW)
        var msg : String
        msg = "whatsapp://send?phone=34"+telefono+"&text=*Pedido:*\r ${p.sms()}"

        intent.setData(Uri.parse(msg))

        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
            Toast.makeText(this, com.example.gestoradetfg.R.string.err_whatsappNoInstalado , Toast.LENGTH_SHORT).show()
        }
    }

    private fun enviarPorMail(p:Pedido) {

        var email : String = getMailProv(p.proveedor)


        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(Uri.parse("mailto:"))
        intent.putExtra(Intent.EXTRA_EMAIL, email)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido")
        intent.putExtra(Intent.EXTRA_TEXT, p.sms())

        startActivity(intent)
    }

    private fun getMailProv(id: String): String {
        for (i in 0..listaProveedores.size-1) {
            if (listaProveedores[i].id == id)
                return listaProveedores[i].email
        }
        return ""
    }

    private fun getTelefonoProv(id : String): String {
        for (i in 0..listaProveedores.size-1) {
            if (listaProveedores[i].id == id)
                return listaProveedores[i].telefono.toString()
        }
        return ""
    }


}