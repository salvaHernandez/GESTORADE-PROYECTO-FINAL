package com.example.gestoradetfg.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestoradetfg.*
import com.example.gestoradetfg.Adapter.RecyclerHomePedido
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.UsuarioActivity.Companion.conUsuarioActivity
import com.example.gestoradetfg.Utils.AuxiliarDB.adapterPedido
import com.example.gestoradetfg.Utils.AuxiliarDB.direcciones
import com.example.gestoradetfg.Utils.AuxiliarDB.listaPedidos
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProveedores
import com.example.gestoradetfg.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1="param1"
private const val ARG_PARAM2="param2"
private lateinit var bindingPedido : FragmentHomeBinding
private lateinit var form_proveedores : Spinner
private lateinit var form_direcciones : Spinner
private lateinit var form_metodoEnvio : Spinner

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String?=null
    private var param2: String?=null
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewFormulario : View
    private lateinit var listaProductosProv : List<Producto>
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
        bindingPedido = FragmentHomeBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        return bindingPedido.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.w("Pepe","tamaño lista pedidos: "+ listaPedidos.size)
        Log.w("Pepe","lista pedidos: "+ listaPedidos.toString())

        recyclerHome.setHasFixedSize(true)
        recyclerHome.layoutManager = LinearLayoutManager(view.context)
        adapterPedido= RecyclerHomePedido (conUsuarioActivity, listaPedidos)
        recyclerHome.adapter =adapterPedido


        bindingPedido.btnCrearPedido.setOnClickListener {

            val builder = AlertDialog.Builder(conUsuarioActivity)
            builder.setTitle("Realizar pedido a:")
            viewFormulario = layoutInflater.inflate(R.layout.seleccionar_prov, null)
            inicializaForm(viewFormulario)
            builder.setView(viewFormulario)


            builder.setPositiveButton(R.string.crear ) {view, _  ->

                if (form_proveedores.selectedItemPosition != 0 && form_metodoEnvio.selectedItemPosition != 0) {

                    var intentPedido = Intent(conUsuarioActivity, pedidoActivity::class.java)

                    intentPedido.putExtra("id", getIdProv())
                    intentPedido.putExtra("direccion", form_direcciones.selectedItem.toString())
                    intentPedido.putExtra("tiempoEnvio", getTiempoEnvioProv())
                    intentPedido.putExtra("metodo", form_metodoEnvio.selectedItemPosition)

                    startActivity(intentPedido)


                } else {
                    Toast.makeText(conUsuarioActivity, R.string.err_camposVacios, Toast.LENGTH_SHORT).show()
                }
            }

            builder.setNegativeButton(R.string.cancelar) { view, _ ->

            }
            builder.show()




        }

    }

    private fun inicializaForm(view: View) {
        form_proveedores = view.findViewById(R.id.f_sp_elije_proveedor)
        form_direcciones = view.findViewById(R.id.f_sp_elije_direccion)
        form_metodoEnvio = view.findViewById(R.id.f_sp_metodo_envio)
        rellenaComboProv()
        rellenaComboDir()
        rellenaComboMetodoEnvio()

    }

    private fun getIdProv(): String {
        return listaProveedores[form_proveedores.selectedItemPosition - 1].id
    }

    private fun getTiempoEnvioProv() : Long {
        return listaProveedores[form_proveedores.selectedItemPosition - 1].tiempoEnvio
    }

    fun rellenaComboProv() {

        nombresProv = arrayListOf()
        nombresProv.add("")
        for (i in 0 until listaProveedores.size) {
            nombresProv.add(listaProveedores[i].nombre)
        }

        val aaProveedores = ArrayAdapter<String> (conUsuarioActivity, android.R.layout.simple_spinner_dropdown_item)
        aaProveedores.addAll(nombresProv)
        form_proveedores.adapter = aaProveedores

    }


    fun rellenaComboMetodoEnvio() {

        ArrayAdapter.createFromResource(conUsuarioActivity, R.array.arrayTipoEnvio, android.R.layout.simple_spinner_item).also {
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            form_metodoEnvio.adapter = adapter
        }

        /*

        var envio : ArrayList<Int>
        envio = arrayListOf()
        envio.add(R.drawable.ic_whatsapp)
        envio.add(R.drawable.ic_mail)

        val aaEnvio = ArrayAdapter<Int> (conUsuarioActivity,android.R.layout.simple_spinner_dropdown_item)
        aaEnvio.add(envio[0])
        aaEnvio.add(envio[1])

        form_metodoEnvio.adapter = aaEnvio
         */

    }

    fun rellenaComboDir() {

        val aaDirecciones = ArrayAdapter<String> (conUsuarioActivity, android.R.layout.simple_spinner_dropdown_item)
        aaDirecciones.addAll(direcciones)
        form_direcciones.adapter = aaDirecciones
        Log.e("Salva", "direcciones:  "+aaDirecciones.toString())

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String)=
            HomeFragment().apply {
                arguments=Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}