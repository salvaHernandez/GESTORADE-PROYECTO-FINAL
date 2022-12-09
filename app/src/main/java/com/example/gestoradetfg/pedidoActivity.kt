package com.example.gestoradetfg


import com.example.gestoradetfg.R
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gestoradetfg.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestoradetfg.Adapter.RecyclerAddPedido
import com.example.gestoradetfg.Model.Pedido
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.Utils.AuxiliarDB.adapterAddPedido
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProductos
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProveedores
import com.example.gestoradetfg.databinding.ActivityPedidoBinding
import kotlinx.android.synthetic.main.activity_pedido.*


private lateinit var binding : ActivityPedidoBinding
private lateinit var listaProductosProv : List<Producto>
private var filtroNombre : String= ""
private lateinit var listaFiltradaProductos : List<Producto>

class pedidoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setTitle(R.string.act_add_pedido)

        val bundle = intent.extras
        val idProv = bundle?.getString("id")
        val direccion = bundle?.getString("dir")

        filtraProd(idProv!!)


        recyclerAddPedido.setHasFixedSize(true)
        recyclerAddPedido.layoutManager = LinearLayoutManager(this)
        adapterAddPedido = RecyclerAddPedido(this, listaProductosProv)
        recyclerAddPedido.adapter = adapterAddPedido


        binding.etFilterProductoPedido.addTextChangedListener { userFilter ->
            filtroNombre = userFilter.toString()
            filtraLista()
            adapterAddPedido.updateProductos(listaFiltradaProductos)

        }


        binding.floatBtnCesta.setOnClickListener {

        }

    }

    private fun filtraLista() {
        listaFiltradaProductos = listaProductosProv.filter { producto ->
            producto.nombre.lowercase().contains(binding.etFilterProductoPedido.text.toString().lowercase())
        }
    }

    fun filtraProd(idProv : String) {
        listaProductosProv = listaProductos.filter { producto ->
            producto.idProvedoor == idProv
        }
    }


    private fun enviarPorWhastsapp(p: Pedido) {

        var telefono = getTelefonoProv(p.proveedor)
        val intent = Intent(Intent.ACTION_VIEW)
        var msg : String
        msg = "whatsapp://send?phone=34"+telefono+"&text=*Pedido:*\r $p"

        intent.setData(Uri.parse(msg))

        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
            Toast.makeText(this, com.example.gestoradetfg.R.string.err_whatsappNoInstalado , Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTelefonoProv(id : String): String {
        for (i in 0..listaProveedores.size-1) {
            if (listaProveedores[i].id == id)
                return listaProveedores[i].telefono.toString()
        }
        return ""
    }


}