package com.example.gestoradetfg.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestoradetfg.Adapter.RecyclerProveedor
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.Model.Proveedor
import com.example.gestoradetfg.R
import com.example.gestoradetfg.UsuarioActivity.Companion.conUsuarioActivity
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProveedores
import com.example.gestoradetfg.Utils.AuxiliarDB.adapterProveedor
import com.example.gestoradetfg.Utils.AuxiliarDB.addProveedor
import com.example.gestoradetfg.databinding.FormularioProveedorBinding
import com.example.gestoradetfg.databinding.FragmentProveedorBinding
import kotlinx.android.synthetic.main.fragment_proveedor.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1="param1"
private const val ARG_PARAM2="param2"
private lateinit var bindingProv: FragmentProveedorBinding
private lateinit var bindingFormulario: FormularioProveedorBinding
private lateinit var filtroTxt : String
private lateinit var listaFiltradaProveedores : List<Proveedor>

private lateinit var form_nombre : EditText
private lateinit var form_direccion : EditText
private lateinit var form_email : EditText
private lateinit var form_telefono : EditText
private lateinit var form_observaciones : EditText
private lateinit var form_tiempoEnvio : EditText
/**
 * A simple [Fragment] subclass.
 * Use the [ProveedorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProveedorFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String?=null
    private var param2: String?=null
    private var filtroStar : Double= 0.0



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
        filtroTxt = ""
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
            filtroTxt = userFilter.toString()
            filtraLista()
        }

        bindingProv.provCheckEnvio.setOnClickListener {
            filtraLista()
        }

        bindingProv.provCheckFav.setOnClickListener {
            filtraLista()
        }

        bindingProv.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            filtroStar = bindingProv.ratingBar.rating.toDouble()
            filtraLista()
        }


        bindingProv.btnAddProveedor.setOnClickListener {

            val builder = AlertDialog.Builder(conUsuarioActivity)
            val view = layoutInflater.inflate(R.layout.formulario_proveedor, null)
            builder.setTitle("Registro Proveedor")

            form_nombre = view.findViewById(R.id.f_et_prov_nombre)
            form_direccion = view.findViewById(R.id.f_et_prov_direccion)
            form_email = view.findViewById(R.id.f_et_prov_email)
            form_telefono = view.findViewById(R.id.f_et_prov_telefono)
            form_observaciones = view.findViewById(R.id.f_et_prov_observaciones)
            form_tiempoEnvio = view.findViewById(R.id.f_et_prov_tiempoEnvio)
            builder.setView(view)


            builder.setPositiveButton(R.string.crear ) {view, _  ->

                if (validaCamposDialogProv()) {
                    addProveedor(creaProveedor(), conUsuarioActivity)

                } else {
                    Toast.makeText(conUsuarioActivity, R.string.err_camposVacios, Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton(R.string.cancelar) { view, _ ->

            }
            builder.show()

        }

    }


    fun validaCamposDialogProv(): Boolean {
        var camposValidos = false

        if (form_nombre.text.toString().trim() != "" &&
            form_direccion.text.toString().trim() != "" &&
            form_email.text.toString().trim() != "" &&
            form_telefono.text.toString().trim() != "" &&
            form_tiempoEnvio.text.toString().trim() != "") {
            camposValidos = true
        }
        return camposValidos
    }

    private fun creaProveedor() : Proveedor {

        var list: ArrayList<Producto>
        list = arrayListOf()

        return Proveedor("1", form_nombre.text.toString(), form_direccion.text.toString(), form_email.text.toString(),
            form_telefono.text.toString().toLong() , form_tiempoEnvio.text.toString().toLong(), 0, form_observaciones.text.toString(), list )

    }

    private fun filtraLista () {

        val opcion = determinaFiltro()

        when (opcion) {
            1 -> filtraPorFav()
            2 -> filtraPorEnvio()
            3 -> filtraTodo()
            4 -> filtraTxt()
            else -> Toast.makeText(conUsuarioActivity, "Ha fallado el filtro", Toast.LENGTH_SHORT).show()
        }
        adapterProveedor.updateProveedores(listaFiltradaProveedores)
    }

    private fun filtraTxt() {

        listaFiltradaProveedores = listaProveedores.filter { proveedor ->
            proveedor.nombre.lowercase().contains(filtroTxt.lowercase()) &&
            proveedor.valoracion >= filtroStar
        }
    }

    private fun filtraTodo() {
        listaFiltradaProveedores = listaProveedores.filter { proveedor ->
            proveedor.nombre.lowercase().contains(filtroTxt.lowercase()) &&
                    proveedor.valoracion >= filtroStar &&
                    (bindingProv.provCheckEnvio.isChecked && proveedor.tiempoEnvio < 3) &&
                    (bindingProv.provCheckFav.isChecked && proveedor.tiempoEnvio < 3)

        }
    }

    private fun filtraPorEnvio() {


        listaFiltradaProveedores = listaProveedores.filter { proveedor ->
            proveedor.nombre.lowercase().contains(filtroTxt.lowercase()) &&
                    proveedor.valoracion >= filtroStar &&
                    (bindingProv.provCheckEnvio.isChecked && proveedor.tiempoEnvio < 3)

        }
    }

    private fun filtraPorFav() {
        listaFiltradaProveedores = listaProveedores.filter { proveedor ->
            proveedor.nombre.lowercase().contains(filtroTxt.lowercase()) &&
                    proveedor.valoracion >= filtroStar &&
                    (bindingProv.provCheckFav.isChecked && proveedor.tiempoEnvio < 3)

        }
    }

    private fun determinaFiltro(): Int {

        val f_fav = bindingProv.provCheckFav.isChecked
        val f_envio = bindingProv.provCheckEnvio.isChecked
        var resultado = 0

        if (f_fav && f_envio) {
            resultado = 3

        } else if (f_envio) {
            resultado = 2
        } else if (f_fav) {
            resultado = 1
        } else {
            resultado = 4
        }
        return resultado
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