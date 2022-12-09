package com.example.gestoradetfg.Adapter

import android.app.AlertDialog
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoradetfg.*
import com.example.gestoradetfg.Model.Proveedor
import com.example.gestoradetfg.UsuarioActivity.Companion.conUsuarioActivity
import com.example.gestoradetfg.Utils.AuxiliarDB.borrarProveedor
import com.example.gestoradetfg.Utils.AuxiliarDB.modProveedor

private lateinit var form_prov_nombre : EditText
private lateinit var form_prov_direccion : EditText
private lateinit var form_prov_email : EditText
private lateinit var form_prov_telefono : EditText
private lateinit var form_prov_observaciones : EditText
private lateinit var form_prov_tiempoEnvio : EditText

class RecyclerProveedor (var context: AppCompatActivity, var listaProveedores:ArrayList<Proveedor>): RecyclerView.Adapter<RecyclerProveedor.ViewHolder>() {


    override fun getItemCount(): Int {
        return this.listaProveedores?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.proveedor_card,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaProveedores[position]
        holder.bind(item, context, this)

    }


    fun updateProveedores (listFiltrada : List<Proveedor>) {
        this.listaProveedores = ArrayList(listFiltrada)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val nombre = view.findViewById<TextView>(R.id.txtCardProvNombre)
        val dir = view.findViewById<TextView>(R.id.txtCardProvDir)
        val telefono = view.findViewById<TextView>(R.id.txtCardProvTelefono)
        val envio = view.findViewById<TextView>(R.id.txtCardProvEnvio)
        val obs = view.findViewById<TextView>(R.id.txtCardProvObservaciones)





        fun bind(p: Proveedor, context: AppCompatActivity, adaptador: RecyclerProveedor) {


            nombre.text = p.nombre.uppercase()
            dir.text = p.direccion
            telefono.text = p.telefono.toString()
            envio.text = p.tiempoEnvio.toString()
            obs.text = p.observaciones



            itemView.setOnClickListener{
                modificarProv(context, p)
            }

            itemView.setOnLongClickListener {
                dialogBorrarProveedor(context, adaptador, p)

                true
            }
        }

        private fun modificarProv(context : AppCompatActivity, p : Proveedor) {
            val builder = androidx.appcompat.app.AlertDialog.Builder(conUsuarioActivity)
            val view =  LayoutInflater.from(context).inflate(R.layout.formulario_proveedor, null)
            builder.setTitle("Modificar Proveedor")
            builder.setView(view)


            rellenarCampos(p, view)

            builder.setPositiveButton(R.string.modificar ) {view, _  ->

                if (validaCamposDialogProv()) {
                    modificaValor(p)
                    modProveedor(p, conUsuarioActivity)
                } else {
                    Toast.makeText(conUsuarioActivity, R.string.err_camposVacios, Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton(R.string.cancelar) { view, _ ->

            }
            builder.show()


        }

        private fun modificaValor(p: Proveedor) {
            p.nombre = form_prov_nombre.text.toString()
            p.email = form_prov_email.text.toString()
            p.telefono = form_prov_telefono.text.toString().toLong()
            p.direccion = form_prov_direccion.text.toString()
            p.tiempoEnvio = form_prov_tiempoEnvio.text.toString().toLong()
            p.observaciones = form_prov_observaciones.text.toString()
        }

        private fun rellenarCampos(p: Proveedor, view: View) {
            form_prov_nombre = view.findViewById(R.id.f_et_prov_nombre)
            form_prov_direccion = view.findViewById(R.id.f_et_prov_direccion)
            form_prov_email = view.findViewById(R.id.f_et_prov_email)
            form_prov_telefono = view.findViewById(R.id.f_et_prov_telefono)
            form_prov_observaciones = view.findViewById(R.id.f_et_prov_observaciones)
            form_prov_tiempoEnvio = view.findViewById(R.id.f_et_prov_tiempoEnvio)

            form_prov_nombre.text = Editable.Factory.getInstance().newEditable(p.nombre)
            form_prov_direccion.text = Editable.Factory.getInstance().newEditable(p.direccion)
            form_prov_email.text = Editable.Factory.getInstance().newEditable(p.email)
            form_prov_telefono.text = Editable.Factory.getInstance().newEditable(p.telefono.toString())
            form_prov_observaciones.text = Editable.Factory.getInstance().newEditable(p.observaciones)
            form_prov_tiempoEnvio.text = Editable.Factory.getInstance().newEditable(p.tiempoEnvio.toString())
        }

        fun validaCamposDialogProv(): Boolean {
            var camposValidos = false

            if (form_prov_nombre.text.toString().trim() != "" &&
                form_prov_direccion.text.toString().trim() != "" &&
                form_prov_email.text.toString().trim() != "" &&
                form_prov_telefono.text.toString().trim() != "") {
                camposValidos = true
            }
            return camposValidos
        }

        fun dialogBorrarProveedor (context: AppCompatActivity, adaptador: RecyclerProveedor, p: Proveedor) {

            AlertDialog.Builder(context)
                .setTitle(R.string.Atencion)
                .setMessage(R.string.msgBorrarProveedor)
                .setPositiveButton(R.string.aceptar) { view, _ ->
                    // Hacer el borrado
                    borrarProveedor(p, context)
                    adaptador.notifyItemRemoved(adapterPosition)
                    view.dismiss()
                }
                .setNegativeButton(R.string.cancelar) { view, _ ->
                    view.dismiss()
                }
                .setCancelable(true)
                .create()
                .show()
        }



    }




}
