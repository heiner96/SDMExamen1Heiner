package com.example.appexamen1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appexamen1.databinding.ActivityAcercaDeBinding
import com.example.appexamen1.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AcercaDeActivity : AppCompatActivity()
{
    private lateinit var  binding: ActivityAcercaDeBinding


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAcercaDeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegresar.setOnClickListener { regresar() }
        val profileName=intent.getStringExtra("name")
        binding.txtNombreEstudiante.text = "HEINER ANTONIO URENA ZUNIGA"

    }

    private fun regresar()
    {
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
    }

}