package com.example.gestoradetfg.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestoradetfg.Adapter.RecyclerProveedor
import com.example.gestoradetfg.R
import com.example.gestoradetfg.UsuarioActivity
import com.example.gestoradetfg.UsuarioActivity.Companion.conUsuarioActivity
import com.example.gestoradetfg.Utils.Auxiliar.listaProveedores
import com.example.gestoradetfg.Utils.Auxiliar.adapterProveedor
import com.example.gestoradetfg.databinding.FragmentProveedorBinding
import kotlinx.android.synthetic.main.fragment_proveedor.*
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1="param1"
private const val ARG_PARAM2="param2"
private lateinit var bindingProv: FragmentProveedorBinding
/**
 * A simple [Fragment] subclass.
 * Use the [ProveedorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProveedorFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String?=null
    private var param2: String?=null

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
        bindingProv = FragmentProveedorBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        return bindingProv.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerProveedor.setHasFixedSize(true)
        recyclerProveedor.layoutManager = LinearLayoutManager(view.context)
        adapterProveedor = RecyclerProveedor (conUsuarioActivity, listaProveedores)
        recyclerProveedor.adapter = adapterProveedor

        bindingProv.etFilterProveedor.addTextChangedListener { userFilter ->

            val proveedorFiltered = listaProveedores.filter { proveedor -> proveedor.nombre.lowercase().contains(userFilter.toString().lowercase()) }
            adapterProveedor.updateProveedores(proveedorFiltered)

        }



        bindingProv.btnAddProveedor.setOnClickListener {
            val builder = AlertDialog.Builder(conUsuarioActivity)
            val view = layoutInflater.inflate(R.layout.formulario_proveedor, null)

            builder.setView(view)
            val dialog = builder.create()
            dialog.show()

        }

    }


    private fun cambiaFoco(hasFocus: Boolean) {
        Toast.makeText(conUsuarioActivity, "Foco", Toast.LENGTH_SHORT).show()

        if (hasFocus) {

        } else {
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProveedorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String)=
            ProveedorFragment().apply {
                arguments=Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}