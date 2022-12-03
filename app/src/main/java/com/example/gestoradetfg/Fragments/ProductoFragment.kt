package com.example.gestoradetfg.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestoradetfg.Adapter.RecyclerProducto
import com.example.gestoradetfg.Model.Pedido
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.Model.Proveedor
import com.example.gestoradetfg.R
import com.example.gestoradetfg.UsuarioActivity
import com.example.gestoradetfg.UsuarioActivity.Companion.conUsuarioActivity
import com.example.gestoradetfg.Utils.Auxiliar
import com.example.gestoradetfg.Utils.Auxiliar.listaProductos
import com.example.gestoradetfg.databinding.FragmentProductoBinding
import com.example.gestoradetfg.databinding.FragmentProveedorBinding
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_producto.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1="param1"
private const val ARG_PARAM2="param2"
private lateinit var bindingProd: FragmentProductoBinding
private lateinit var listaFiltradaProductos : List<Producto>

/**
 * A simple [Fragment] subclass.
 * Use the [ProductoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String?=null
    private var param2: String?=null
    private var seleccionTipoVenta = 0
    private var filtroCalidad : Double= 0.0
    private var filtroNombre : String= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1=it.getString(ARG_PARAM1)
            param2=it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingProd = FragmentProductoBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return bindingProd.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        ArrayAdapter.createFromResource(conUsuarioActivity, R.array.arrayTipoVentas, android.R.layout.simple_spinner_item).also {
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bindingProd.fSpinnerTipoProducto.adapter = adapter
        }


        recyclerProducto.setHasFixedSize(true)
        recyclerProducto.layoutManager = LinearLayoutManager(view.context)
        Auxiliar.adapterProducto = RecyclerProducto (conUsuarioActivity, listaProductos)
        recyclerProducto.adapter =Auxiliar.adapterProducto

        bindingProd.btnAddProducto.setOnClickListener{

            val builder = AlertDialog.Builder(conUsuarioActivity)
            val view = layoutInflater.inflate(R.layout.formulario_producto, null)
            builder.setTitle("Registro Producto")

            form_nombre = view.findViewById(R.id.et_prov_nombre)
            form_direccion = view.findViewById(R.id.et_prov_direccion)
            form_email = view.findViewById(R.id.et_prov_email)
            form_telefono = view.findViewById(R.id.et_prov_telefono)
            form_observaciones = view.findViewById(R.id.et_prov_observaciones)


        }

        bindingProd.rbCalidadProd.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            filtroCalidad = bindingProd.rbCalidadProd.rating.toDouble()
            filtraLista(seleccionTipoVenta)
        }

        bindingProd.etFilterProducto.addTextChangedListener { userFilter ->
            filtroNombre = userFilter.toString()
            filtraLista(seleccionTipoVenta)
        }


        bindingProd.fSpinnerTipoProducto.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                seleccionTipoVenta = position
                filtraLista(seleccionTipoVenta)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }


    private fun filtraLista (tipoVenta : Int) {


        when (tipoVenta) {
            0 -> filtraVacio()
            1 -> filtraPorUnidad()
            2 -> filtraPorPeso()
            else -> Toast.makeText(conUsuarioActivity, "Ha fallado el filtro", Toast.LENGTH_SHORT).show()
        }

        Auxiliar.adapterProducto.updateProductos(listaFiltradaProductos)
    }


    private fun filtraVacio () {
        listaFiltradaProductos = listaProductos.filter { producto ->
            producto.nombre.lowercase().contains(bindingProd.etFilterProducto.text.toString().lowercase()) &&
                    producto.calidad >= filtroCalidad
        }
    }

    private fun filtraPorUnidad () {
        listaFiltradaProductos = listaProductos.filter { producto ->
            producto.nombre.lowercase().contains(bindingProd.etFilterProducto.text.toString().lowercase()) &&
                    producto.calidad >= filtroCalidad &&
                    producto.tipoVenta
        }
    }

    private fun filtraPorPeso () {
        listaFiltradaProductos = listaProductos.filter { producto ->
            producto.nombre.lowercase().contains(bindingProd.etFilterProducto.text.toString().lowercase()) &&
                    producto.calidad >= filtroCalidad &&
                    !producto.tipoVenta
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String)=
            ProductoFragment().apply {
                arguments=Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}