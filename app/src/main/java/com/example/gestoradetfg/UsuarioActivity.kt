package com.example.gestoradetfg

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gestoradetfg.Model.Pedido
import com.example.gestoradetfg.Model.Producto
import com.example.gestoradetfg.Model.Proveedor
import com.example.gestoradetfg.Utils.Auxiliar
import com.example.gestoradetfg.Utils.Auxiliar.getPedidos
import com.example.gestoradetfg.Utils.Auxiliar.listaPedidos
import com.example.gestoradetfg.Utils.Auxiliar.listaProductoProveedor
import com.example.gestoradetfg.Utils.Auxiliar.listaProductos
import com.example.gestoradetfg.Utils.Auxiliar.listaProveedores
import com.example.gestoradetfg.databinding.ActivityUsuarioBinding

class UsuarioActivity : AppCompatActivity() {
    companion object {
        lateinit var conLoginAdmin : AppCompatActivity
    }

    private lateinit var binding: ActivityUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("Salva", "Usuario activityyyyyyyyyyyyyyyy   ")

        conLoginAdmin = this
        listaPedidos = arrayListOf()
        listaProveedores = arrayListOf()
        listaProductos = arrayListOf()
        listaProductoProveedor = arrayListOf()

        // Codigo generado por los fragments
        binding=ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("Salva", "Pasa el binding   ")

        

        val navView: BottomNavigationView=binding.navView

        val navController=findNavController(R.id.nav_host_fragment_activity_usuario)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration=AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_proveedor, R.id.navigation_producto
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)






    }
}