package com.example.gestoradetfg

import android.os.Bundle
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
import com.example.gestoradetfg.databinding.ActivityUsuarioBinding

class UsuarioActivity : AppCompatActivity() {
    companion object {
        lateinit var conLoginAdmin : AppCompatActivity
        lateinit var listaPedidos : ArrayList<Pedido>
        lateinit var listaProveedores : ArrayList<Proveedor>
        lateinit var listaProductos : ArrayList<Producto>
    }

    private lateinit var binding: ActivityUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        conLoginAdmin = this
        listaPedidos = arrayListOf()
        listaProveedores = arrayListOf()
        listaProductos = arrayListOf()
//        Auxiliar.getPedidos(false)

        // Codigo generado por los fragments
        binding=ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        

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