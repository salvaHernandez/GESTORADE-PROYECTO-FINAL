package com.example.gestoradetfg.Adapter

import android.app.AlertDialog
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoradetfg.Fragments.*
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.R
import com.example.gestoradetfg.UsuarioActivity.Companion.conUsuarioActivity
import com.example.gestoradetfg.Utils.AuxiliarDB.borrarProducto
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProveedores
import com.example.gestoradetfg.Utils.AuxiliarDB.modProducto


private lateinit var form_prod_nombre : EditText
private lateinit var form_prod_proveedor : Spinner
private lateinit var form_prod_precio : EditText
private lateinit var form_prod_tipoVenta : Spinner
private lateinit var form_prod_calidad : RatingBar

class RecyclerProducto (var context: AppCompatActivity, var listaProducto:ArrayList<Producto>): RecyclerView.Adapter<RecyclerProducto.ViewHolder>() {


    override fun getItemCount(): Int {
        return this.listaProducto?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.producto_card,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaProducto[position]
        holder.bind(item, context, this)
    }

    fun updateProductos (listFiltrada : List<Producto>) {
        this.listaProducto = ArrayList(listFiltrada)
        notifyDataSetChanged()
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nombre = view.findViewById<TextView>(R.id.txtCard_prod_nombre)
        val prov = view.findViewById<TextView>(R.id.txtCard_prod_proveedor)
        val precio = view.findViewById<TextView>(R.id.txtCard_prod_precio)
        val calidad = view.findViewById<RatingBar>(R.id.rtbCardProd)


        fun bind(p: Producto, context: AppCompatActivity, adaptador: RecyclerProducto) {
            nombre.text = p.nombre

            prov.text = getNombreProv(p.idProvedoor)

            if (p.tipoVenta) {
                precio.text = ("Precio ud: "+p.precio)
            } else {
                precio.text = ("Precio kg: "+p.precio)
            }
            calidad.rating = p.calidad.toFloat()

            itemView.setOnClickListener{
                modificarProducto(context, p)
            }

            itemView.setOnLongClickListener {
                dialogBorrarProducto(context,  p)

                true
            }

        }

        private fun getNombreProv(idProvedoor: String): String {

            for (i in 0..listaProveedores.size-1) {
                if (listaProveedores[i].id == idProvedoor) {
                    return listaProveedores[i].nombre
                }
            }
            return ""
        }

        private fun modificarProducto(context: AppCompatActivity, p: Producto) {
            val builder = androidx.appcompat.app.AlertDialog.Builder(conUsuarioActivity)
            val view =  LayoutInflater.from(context).inflate(R.layout.formulario_producto, null)
            builder.setTitle("Modificar Producto")
            builder.setView(view)

            var oldProducto = p.copy()


            rellenarCampos(p, view)

            builder.setPositiveButton(R.string.modificar ) {view, _  ->
                if (validaCamposProd()) {
                    modificaValores(p)
                    modProducto(p,oldProducto, context)
                }
            }
            builder.setNegativeButton(R.string.cancelar) { view, _ ->

            }
            builder.show()



        }

        private fun validaCamposProd(): Boolean {
            var camposValidos = false

            if (form_prod_nombre.text.toString().trim() != "" &&
                form_prod_precio.text.toString().trim() != "" &&
                form_prod_tipoVenta.selectedItemPosition != 0 &&
                form_prod_proveedor.selectedItemPosition != 0 ) {
                camposValidos = true
            }

            return camposValidos
        }

        fun dialogBorrarProducto (context: AppCompatActivity, p: Producto) {

            AlertDialog.Builder(context)
                .setTitle(R.string.Atencion)
                .setMessage(R.string.msgBorrarProducto)
                .setPositiveButton(R.string.aceptar) { view, _ ->
                    // Hacer el borrado

                    borrarProducto(p,context)
                    view.dismiss()
                }
                .setNegativeButton(R.string.cancelar) { view, _ ->
                    view.dismiss()
                }
                .setCancelable(true)
                .create()
                .show()
        }


        private fun rellenarCampos(p: Producto, view: View) {
            form_prod_nombre = view.findViewById(R.id.f_et_prod_nombre)
            form_prod_proveedor = view.findViewById(R.id.f_sp_prod_proveedor)
            form_prod_precio = view.findViewById(R.id.f_et_prod_precio)
            form_prod_tipoVenta = view.findViewById(R.id.f_sp_prod_tipoVenta)
            form_prod_calidad = view.findViewById(R.id.f_rat_prod_calidad)

            form_prod_proveedor.isEnabled = false

            form_prod_nombre.text = Editable.Factory.getInstance().newEditable(p.nombre)
            form_prod_precio.text = Editable.Factory.getInstance().newEditable(p.precio.toString())
            form_prod_calidad.rating = Editable.Factory.getInstance().newEditable(p.calidad.toString()).toString().toFloat()



            if (p.tipoVenta) {
                form_prod_tipoVenta.setSelection(1)
            } else {
                form_prod_tipoVenta.setSelection(2)
            }
            rellenaComboProv()



            for (i in 0..listaProveedores.size -1) {
                if (listaProveedores[i].id.equals(p.idProvedoor)) {
                    form_prod_proveedor.setSelection(i+1)
                }
            }

        }

        fun rellenaComboProv() {
            val aaProveedores = ArrayAdapter<String> (conUsuarioActivity,android.R.layout.simple_spinner_dropdown_item)
            aaProveedores.addAll(nombresProv)
            form_prod_proveedor.adapter = aaProveedores

        }

        private fun modificaValores(p: Producto) {
            p.nombre = form_prod_nombre.text.toString()
            p.precio = form_prod_precio.text.toString().toDouble()
            p.calidad = form_prod_calidad.rating.toDouble()

            if (form_prod_tipoVenta.selectedItemPosition == 1) {
                p.tipoVenta = true
            } else {
                p.tipoVenta = false
            }

        }
    }
}