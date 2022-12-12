package com.example.gestoradetfg.Model

import java.io.Serializable

data class Usuario(var email: String, var nombre: String, var telefono: String, var direccion: ArrayList<String>, var nif : String): Serializable
