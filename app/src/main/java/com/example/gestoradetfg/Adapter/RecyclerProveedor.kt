package com.example.gestoradetfg.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoradetfg.Model.Proveedor
import com.example.gestoradetfg.R
import com.example.gestoradetfg.databinding.ProveedorCardBinding

private lateinit var bindingProv: ProveedorCardBinding

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

        val nombre = view.findViewById<TextView>(R.id.t_ped_proveedor_card)





        fun bind(p: Proveedor, context: AppCompatActivity, adaptador: RecyclerProveedor) {


            nombre.text = p.nombre

/*            verificado.isChecked = u.verificado


            itemView.setOnClickListener{
                if (!u.admin) {
                    if(verificado.isChecked) {
                        ModificarVerificacion(u, false)
                    } else {
                        ModificarVerificacion(u, true)
                    }
                } else {
                    Toast.makeText(context, R.string.smsSinPermisos, Toast.LENGTH_SHORT).show()
                }
            }

            itemView.setOnLongClickListener {
                if (!u.admin) {
                    BorrarUser(context, adaptador, u)
                } else {
                    Toast.makeText(context, R.string.smsSinPermisosB, Toast.LENGTH_SHORT).show()
                }
                true
            }
 */
        }


    }
}
