package com.example.gestoradetfg

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gestoradetfg.Utils.Auxiliar.listaPedidos
import com.example.gestoradetfg.Utils.Auxiliar.listaProductoPedido
import com.example.gestoradetfg.Utils.Auxiliar.listaProductoProveedor
import com.example.gestoradetfg.Utils.Auxiliar.listaProductos
import com.example.gestoradetfg.Utils.Auxiliar.listaProveedores
import com.example.gestoradetfg.databinding.ActivityUsuarioBinding

class UsuarioActivity : AppCompatActivity() {
    companion object {
        lateinit var conUsuarioActivity : AppCompatActivity
    }

    private lateinit var binding: ActivityUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        conUsuarioActivity = this


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