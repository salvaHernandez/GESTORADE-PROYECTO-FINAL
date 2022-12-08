package com.example.gestoradetfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.gestoradetfg.Model.ProviderType
import com.example.gestoradetfg.Utils.AuxiliarDB.getProveedores
import com.example.gestoradetfg.Utils.AuxiliarDB.initListas
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore





class MainActivity : AppCompatActivity() {
    companion object {
        val db = FirebaseFirestore.getInstance()
        val REQUEST_CODE_LOCATION = 0
        var idUsuarioActivo = ""

    }
    private var RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()


        idUsuarioActivo = "Correo"
        initListas()
        getProveedores()
        irLogin("", ProviderType.GOOGLE, true, true)

        //Con esto lanzamos eventos personalizados a GoogleAnalytics que podemos ver en nuestra consola de FireBase.
        val analy: FirebaseAnalytics= FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","Integración completada")
        analy.logEvent("InitScreen",bundle)

    }


    fun LoginGoogle (view: View) {

        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.request_id_token)) //Esto se encuentra en el archivo google-services.json: client->oauth_client -> client_id
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this,googleConf) //Este será el cliente de autenticación de Google.
        googleClient.signOut() //Con esto salimos de la posible cuenta  de Google que se encuentre logueada.
        val signInIntent = googleClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Si la respuesta de esta activity se corresponde con la inicializada es que viene de la autenticación de Google.
        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account=task.getResult(ApiException::class.java)!!
                Log.e("Salva", "La cuenta que recoge es:" +account.id)

                if (account != null) {
                    val credential: AuthCredential= GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                            if (it.isSuccessful) {
                                db.collection(COLECCION_USUARIO).document(account.email.toString()).get()
                                    .addOnSuccessListener {

                                    }
                                irLogin(account.email ?: "", ProviderType.GOOGLE, false, false)
                            } else showAlert()
                        }
                }
            } catch (e: ApiException) {
                Log.w("Salva", "Google sign in failed", e)
                showAlert()
            }
        }
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
    private fun irLogin(email:String, provider: ProviderType, isAdmin : Boolean, verificado : Boolean){

/*
        txtUser.text.clear()
        txtPass.text.clear()

        usuario.verificado = verificado
        usuario.admin = isAdmin
        usuario.email = email


*/
        val intent : Intent
        intent = Intent(this,UsuarioActivity::class.java)

        //   intent.putExtra("user", usuario)
        startActivity(intent)


    }

}
