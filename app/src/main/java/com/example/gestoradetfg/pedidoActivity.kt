package com.example.gestoradetfg

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestoradetfg.Adapter.RecyclerAddPedido
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.Utils.AuxiliarDB.adapterAddPedido
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProductoProveedor
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


        val bundle = intent.extras
        val idProv = bundle?.getString("id")

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

}