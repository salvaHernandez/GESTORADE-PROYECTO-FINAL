package com.example.gestoradetfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.gestoradetfg.Utils.AuxiliarDB.getDirecciones
import com.example.gestoradetfg.Utils.AuxiliarDB.getPedidos
import com.example.gestoradetfg.Utils.AuxiliarDB.getProveedores
import com.example.gestoradetfg.Utils.AuxiliarDB.initListas
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    companion object {
        val db = FirebaseFirestore.getInstance()
        val REQUEST_CODE_LOCATION = 100
        var idUsuarioActivo = ""
    }

    lateinit var telefono : EditText
    lateinit var codigo : EditText
    private var RC_SIGN_IN = 1
    private lateinit var auth : FirebaseAuth
    var storedVerificationId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        auth = Firebase.auth
        telefono = findViewById(R.id.etTelefono)
        codigo = findViewById(R.id.etCodigo)

        idUsuarioActivo = "+34673034299"




        //Con esto lanzamos eventos personalizados a GoogleAnalytics que podemos ver en nuestra consola de FireBase.
        val analy: FirebaseAnalytics= FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","Integración completada")
        analy.logEvent("InitScreen",bundle)

    }


    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuairo")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog= builder.create()
        dialog.show()
    }


    //*********************************************************************************
    private fun irLogin(){

        val intent : Intent
        intent = Intent(this,UsuarioActivity::class.java)

        startActivity(intent)


    }

    fun loginTelefono (view:View) {

        if (view == btnEnviar && telefono.text.isNotEmpty()) {
            val options = PhoneAuthOptions.newBuilder(auth)
        //        .setPhoneNumber("+34673292203")       // Phone number to verify
                .setPhoneNumber("+34"+etTelefono.text.toString().trim())       // Phone number to verify
                .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
//        auth.setLanguageCode(Locale.getDefault().language)
            PhoneAuthProvider.verifyPhoneNumber(options)

        } else if (view == btnVerificar && codigo.text.isNotEmpty()) {
            val codigo = codigo.text.toString().trim()
            val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, codigo)
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val user = task.result?.user
                    var idUser = user!!.phoneNumber.toString()
                    idUsuarioActivo = idUser

                    db.collection(COLECCION_USUARIO).document(idUser)
                        .get().addOnSuccessListener {  result ->

                            if (result.exists()) {
                                initListas()
                                getPedidos()
                                getProveedores()
                                getDirecciones()
                                irLogin()
                            } else {
                                val intent : Intent
                                intent = Intent(this,RegistroActivity::class.java)
                                intent.putExtra("id", user!!.phoneNumber)
                                startActivity(intent)
                            }
                        }
                } else {


                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Error al registrarnos", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    }

    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
          //  signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.w("Pepe","entra en el invalid")

            } else if (e is FirebaseTooManyRequestsException) {
                Log.w("Pepe","entra en el tooMany" + e.toString())

                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken) {
            Log.w("Pepe","OnCOde"+ verificationId)

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
        //    resendToken = token
        }
    }



}
