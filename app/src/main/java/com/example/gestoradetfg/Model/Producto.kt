package com.example.gestoradetfg.Model

import android.graphics.Bitmap
import java.io.Serializable

// Añadir despues la imagen
data class Producto(var id: String,
                    var nombre : String,
                    var precio : Double,
                    var calidad : Double,
                    // True = Unidad : False = Peso
                    var tipoVenta : Boolean,
                    var cantidad : Double,
                    var idProvedoor : String): Serializable
