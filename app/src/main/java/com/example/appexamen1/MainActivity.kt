package com.example.appexamen1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import com.example.appexamen1.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity()
{
    private lateinit var  binding: ActivityMainBinding
    private var isVisiblePassword = false

    //objeto para realizar la comunicacion
    private  lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //inicializo el objeto de autenticacion, realmente firebase
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        binding.btnAcceder.setOnClickListener { hacelogin() }
        binding.btnRegistrar.setOnClickListener { haceRegitro() }



    }

    private fun haceRegitro() {
        val  correo = binding.txtEmail.text.toString()
        val  clave = binding.txtPassword.text.toString()

        auth.createUserWithEmailAndPassword(correo,clave).addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                val user = auth.currentUser
                refresca(user)
            }
            else{ // si no hizo el registro....
                Toast.makeText(baseContext,getString(R.string.tv_fallo), Toast.LENGTH_LONG).show()
                refresca(null)

            }
        }
    }

    private fun refresca(user: FirebaseUser?) {
        if(user != null){
            // me paso a la pantalla principal .....
            val intent = Intent(this, ListaActivity::class.java)

            startActivity(intent)
        }
    }

    private fun hacelogin() {
        val  correo = binding.txtEmail.text.toString()
        val  clave = binding.txtPassword.text.toString()

        auth.signInWithEmailAndPassword(correo,clave).addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                val user = auth.currentUser
                refresca(user)
            }
            else{ // si no hizo el registro....
                Toast.makeText(baseContext,getString(R.string.tv_fallo), Toast.LENGTH_LONG).show()
                refresca(null)

            }
        }
    }


    override fun onStart() //se ejecuta cuando la app esta en la pantalla
    {//login automantico
        super.onStart()
        val usuario = auth.currentUser
        refresca(usuario)
    }




}