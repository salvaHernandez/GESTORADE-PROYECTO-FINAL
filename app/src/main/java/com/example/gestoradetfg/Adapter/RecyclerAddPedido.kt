package com.example.gestoradetfg.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoradetfg.Model.Pedido
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.R
import java.math.RoundingMode
import java.text.DecimalFormat
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProductoPedido
import com.example.gestoradetfg.Utils.AuxiliarDB.listaProductoProveedor

class RecyclerAddPedido(var context : AppCompatActivity, var list_prod_prov:ArrayList<Producto>,var pedido: Pedido): RecyclerView.Adapter<RecyclerAddPedido.ViewHolder>() {

    override fun getItemCount(): Int {
        return this.list_prod_prov?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.add_pedido_card,parent,false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list_prod_prov[position]
        holder.bind(item, context, this, pedido)

    }

    fun updateProductos (listFiltrada : List<Producto>) {
        this.list_prod_prov = ArrayList(listFiltrada)
        notifyDataSetChanged()
    }




    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val nombre = view.findViewById<TextView>(R.id.t_add_ped_nombre_card)
        val btnAddProducto = view.findViewById<ImageButton>(R.id.btn_add_prod_pedido)
        val cantidad = view.findViewById<NumberPicker>(R.id.numCantidadProd)
        val precio = view.findViewById<TextView>(R.id.t_add_ped_precio)



        fun bind(p: Producto, context: AppCompatActivity, adaptador: RecyclerAddPedido, pedido: Pedido) {
            nombre.text = p.nombre
            cantidad.minValue = 1
            cantidad.maxValue = 50
            precio.text = p.precio.toString()+"€"


            cantidad.setOnValueChangedListener { picker, oldVal, newVal ->
                precio.text = formatNum((newVal * p.precio)).toString()+"€"

            }

            btnAddProducto.setOnClickListener {

                p.cantidad = cantidad.value.toDouble()

                p.precio = formatNum(formatoNumero(precio.text.toString()))
                pedido.precioFinal += p.precio
                pedido.productos.add(p)
                listaProductoPedido.add(p)
                listaProductoProveedor.remove(p)
                adaptador.notifyDataSetChanged()
                cantidad.value = 1


            }
        }

        fun formatNum(valor : Double): Double{

            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.DOWN
            val numFormateado = df.format(valor)
            return numFormateado.toDouble()

        }

        fun formatoNumero(numTexto : String): Double{
            var texto = numTexto.substring(0, (numTexto.length-1))
            return texto.toDouble()
        }
    }

}


