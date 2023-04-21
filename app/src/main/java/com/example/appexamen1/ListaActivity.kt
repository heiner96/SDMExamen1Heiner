package com.example.appexamen1

import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appexamen1.adapter.ActividadAdapter
import com.example.appexamen1.databinding.ActivityListaBinding
import com.example.appexamen1.model.Actividad
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ListaActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityListaBinding
    lateinit var fab: FloatingActionButton;
    lateinit var recyclerView: RecyclerView;
    lateinit var actividadAdapter:ActividadAdapter;
    lateinit var menuItem:MenuItem;
    lateinit var doneTv:TextView;

    private val usuario = Firebase.auth.currentUser?.email.toString()


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding= ActivityListaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actividadAdapter = ActividadAdapter(this.baseContext);
        recyclerView= binding.rvItemList
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ListaActivity)
            adapter = actividadAdapter
        }

        fab=binding.fab
        fab.setOnClickListener{ showNewDialog() }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater

        inflater.inflate(R.menu.buttons, menu)
        menu.getItem(0).title = usuario;

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.btnCerrar -> {
            val intent = Intent(this, AcercaDeActivity::class.java)
            intent.putExtra("name",Firebase.auth.currentUser?.email.toString() )
            Firebase.auth.signOut()
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
    fun showNewDialog() {
        //val dialogBuilder = AlertDialog.Builder(this)
        val myLayout: LayoutInflater = LayoutInflater.from(this)
        val builder = AlertDialog.Builder(this)
        val view = myLayout.inflate(R.layout.formulario, null)
        //Obtener entradas
        val txtId=view.findViewById<EditText>(R.id.txtIdActividad);
        val txtName=view.findViewById<EditText>(R.id.txtNombre);
        val txtDescripcion=view.findViewById<EditText>(R.id.txtDescripcion);
        val txtCosto=view.findViewById<EditText>(R.id.txtCosto);
        val txtCantidad=view.findViewById<EditText>(R.id.txtCantidad);
        val txtPorcentaje=view.findViewById<EditText>(R.id.txtPorcentaje);
        //Crear dialogo
        val myDialog = builder.setView(view)
            // Guardar actividad
            .setPositiveButton("Guardar") { dialog, whichButton ->
                val ctxtName=txtName.text.toString().toString()
                val ctxtDescripcion=txtDescripcion.text.toString().toString()
                val ctxtCosto=txtCosto.text.toString().toDouble()
                val ctxCantidad=txtCantidad.text.toString().toInt()
                val ctxPorcentaje =txtPorcentaje.text.toString().toInt()
                actividadAdapter.addActividad(Actividad(0, ctxtName,ctxtDescripcion,ctxtCosto,ctxCantidad,ctxPorcentaje))
            }
            .setNegativeButton("Cancelar") { dialog, whichButton ->
                Toast.makeText(this, "Creacion cancelada", Toast.LENGTH_LONG).show()
            }


        val b = builder.create()
        b.show()
    }

}