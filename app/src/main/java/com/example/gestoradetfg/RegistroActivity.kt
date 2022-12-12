package com.example.gestoradetfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.gestoradetfg.Model.Usuario
import com.example.gestoradetfg.Utils.AuxiliarDB.addUsuario
import com.example.gestoradetfg.databinding.ActivityRegistroBinding


private lateinit var binding : ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {
    lateinit var id : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setTitle(R.string.registroUser)
        val bundle = intent.extras
        id = bundle?.getString("id")!!

    }

    fun registrar (view: View) {

        if (camposValidos()) {
            addUsuario(creaUsuario(), this)
            onBackPressed()
        } else {
            Toast.makeText(this, R.string.err_camposVacios, Toast.LENGTH_SHORT).show()
        }
    }

    private fun camposValidos(): Boolean {
        var campos = false

        if (binding.tRegistroNombre.text.toString().trim() != "" &&
            binding.tRegistroDireccion.text.toString().trim() != "" &&
            binding.tRegistroEmail.text.toString().trim() != "") {
            campos = true
        }
        return campos
    }

    private fun creaUsuario(): Usuario {

        var direcciones : ArrayList<String> = arrayListOf()
        direcciones.add(binding.tRegistroDireccion.text.toString())

        return Usuario(
            binding.tRegistroEmail.text.toString(),
            binding.tRegistroNombre.text.toString(),
            id,
            direcciones,
            binding.tRegistroNif.text.toString()
        )
    }
}