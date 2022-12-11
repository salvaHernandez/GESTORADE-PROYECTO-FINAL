package com.example.gestoradetfg.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gestoradetfg.Adapter.RecyclerProducto
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.R
import com.example.gestoradetfg.UsuarioActivity.Companion.conUsuarioActivity
import com.example.gestoradetfg.Utils.AuxiliarDB.adapterProducto
import com.example.gestoradetfg.Utils.AuxiliarDB.addProducto
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProductos
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProveedores
import com.example.gestoradetfg.databinding.FragmentProductoBinding
import kotlinx.android.synthetic.main.fragment_producto.*
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1="param1"
private const val ARG_PARAM2="param2"
private lateinit var bindingProd : FragmentProductoBinding
private lateinit var listaFiltradaProductos : List<Producto>

private lateinit var form_nombre : EditText
private lateinit var form_precio : EditText
private lateinit var form_proveedor : Spinner
private lateinit var form_tipoVenta : Spinner
private lateinit var form_calidad : RatingBar
lateinit var nombresProv : ArrayList<String>
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
    private lateinit var viewFormulario : View

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
        nombresProv = arrayListOf()

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
        recyclerProducto.layoutManager = GridLayoutManager(view.context, 2)
        adapterProducto = RecyclerProducto (conUsuarioActivity, listaProductos)
        recyclerProducto.adapter = adapterProducto

        bindingProd.btnAddProducto.setOnClickListener{

            val builder = AlertDialog.Builder(conUsuarioActivity)
            builder.setTitle("Registro Producto")
            viewFormulario = layoutInflater.inflate(R.layout.formulario_producto, null)
            inicializaForm(viewFormulario)
            rellenaComboProv()


            builder.setView(viewFormulario)

            builder.setPositiveButton(R.string.crear ) {view, _  ->

                if (validaCampos()) {

                    addProducto(creaProducto(), getIdProv(), conUsuarioActivity)

                } else {
                    Toast.makeText(conUsuarioActivity, R.string.err_camposVacios, Toast.LENGTH_SHORT).show()
                }
            }

            builder.setNegativeButton(R.string.cancelar) { view, _ ->

            }
            builder.show()
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

    private fun inicializaForm(view: View) {
        form_nombre = view.findViewById(R.id.f_et_prod_nombre)
        form_precio = view.findViewById(R.id.f_et_prod_precio)
        form_proveedor = view.findViewById(R.id.f_sp_prod_proveedor)
        form_tipoVenta = view.findViewById(R.id.f_sp_prod_tipoVenta)
        form_calidad = view.findViewById(R.id.f_rat_prod_calidad)
    }

    private fun getIdProv(): String {
        return listaProveedores[form_proveedor.selectedItemPosition - 1].id
    }

    private fun rellenaComboProv() {
        nombresProv = arrayListOf()
        nombresProv.add("")
        for (i in 0 until listaProveedores.size) {
            nombresProv.add(listaProveedores[i].nombre)
        }

        val aaProveedores = ArrayAdapter<String> (conUsuarioActivity,android.R.layout.simple_spinner_dropdown_item)
        aaProveedores.addAll(nombresProv)
        form_proveedor.adapter = aaProveedores

    }

    private fun creaProducto(): Producto {

        var tipoProducto = false

        if (form_tipoVenta.selectedItemPosition == 1) {
            tipoProducto = true
        }
        return Producto("1", form_nombre.text.toString(), form_precio.text.toString().toDouble(), form_calidad.rating.toDouble(), tipoProducto, 0.0, getIdProv())

    }

    private fun validaCampos(): Boolean {
        var camposValidos = false


        if (form_nombre.text.toString().trim() != "" &&
            form_precio.text.toString().trim() != "" &&
            form_tipoVenta.selectedItemPosition != 0 &&
            form_proveedor.selectedItemPosition != 0 ) {
            camposValidos = true
        }

        return camposValidos
    }


    private fun filtraLista (tipoVenta : Int) {


        when (tipoVenta) {
            0 -> filtraVacio()
            1 -> filtraPorUnidad()
            2 -> filtraPorPeso()
            else -> Toast.makeText(conUsuarioActivity, "Ha fallado el filtro", Toast.LENGTH_SHORT).show()
        }

        adapterProducto.updateProductos(listaFiltradaProductos)
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