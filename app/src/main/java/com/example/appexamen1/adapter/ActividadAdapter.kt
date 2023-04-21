package com.example.appexamen1.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.appexamen1.R
import com.example.appexamen1.model.Actividad
import com.example.appexamen1.network.ApiActividades
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ActividadAdapter(val context: Context) :  RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>()
{
    val client by lazy { ApiActividades.create() }
    var actividades: ArrayList<Actividad> = ArrayList()

    init { refreshActividades() }

    class ActividadViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val txtId = view.findViewById<TextView>(R.id.txtIdActividad)
        val txtName = view.findViewById<TextView>(R.id.txtNombre)
        //val txtDescripcion = view.findViewById<TextView>(R.id.txtDescripcion)
        val txtCosto = view.findViewById<TextView>(R.id.txtCosto)
        //val txtCantidad = view.findViewById<TextView>(R.id.txtCantidad)//no esta de mas tenerlos identificados por aquello de una update
        //val txtPorcentaje = view.findViewById<TextView>(R.id.txtPorcentaje)
        val btnDelete = view.findViewById<TextView>(R.id.btnDelete)
        val btnEdit = view.findViewById<TextView>(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ActividadAdapter.ActividadViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lista_actividades, parent, false)

        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        //holder.txtId.text = actividades[position].idActividad.toString();
        holder.txtName.text = actividades[position].name;
        holder.txtCosto.text = actividades[position].costo.toString();
        holder.btnDelete.setOnClickListener { showDeleteDialog(holder, actividades[position]) }
        holder.btnEdit.setOnClickListener { showUpdateDialog(holder, actividades[position]) }
    }

    override fun getItemCount() = actividades.size

    fun refreshActividades() {
        client.getActividades()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.d("Pretty Printed JSON :", result.toString())

                    actividades.clear()
                    actividades.addAll(result)
                    notifyDataSetChanged()

                },
                { error ->
                    Toast.makeText(context, "Error refrescando: ${error.message}", Toast.LENGTH_LONG).show()
                    Log.e("Errores", error.message.toString())
                }
            )
    }

    fun updateActividades(actividad: Actividad) {
        actividad.idActividad?.let {
            client.updateActividad(it, actividad)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshActividades() }, { throwable ->
                    Toast.makeText(context, "Error Actualizando: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )
        }
    }

    fun addActividad(actividad: Actividad) {
        Log.d("Crear", actividad.toString())
        client.addActividad(actividad)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ refreshActividades() }, { throwable ->
                Log.d("Error Crear", "Error Crear: ${throwable.message}")
                Toast.makeText(context, "Error Crear: ${throwable.message}", Toast.LENGTH_LONG).show()
            }
            )
    }

    fun deleteActividad(actividad: Actividad) {

        actividad.idActividad?.let {
            client.deleteActividad(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshActividades() }, { throwable ->
                    Toast.makeText(context, "Error Eliminar: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )
        }

    }

    fun showUpdateDialog(holder:ActividadViewHolder, actividad: Actividad) {
        //val dialogBuilder = AlertDialog.Builder(this)
        val myLayout: LayoutInflater = LayoutInflater.from(holder.view.context)
        val builder = AlertDialog.Builder(holder.view.context)
        val view = myLayout.inflate(R.layout.formulario, null)
        //Obtener entradas
        val txtId = view.findViewById<TextView>(R.id.txtIdActividad)
        val txtName = view.findViewById<TextView>(R.id.txtNombre)
        val txtDescripcion = view.findViewById<TextView>(R.id.txtDescripcion)
        val txtCosto = view.findViewById<TextView>(R.id.txtCosto)
        val txtCantidad = view.findViewById<TextView>(R.id.txtCantidad)//no esta de mas tenerlos identificados por aquello de una update
        val txtPorcentaje = view.findViewById<TextView>(R.id.txtPorcentaje)

        //Asignar varlores a mostrar
        txtId.setText(actividad.idActividad.toString())
        txtName.setText(actividad.name)
        txtDescripcion.setText(actividad.descripcion)
        txtCosto.setText(actividad.costo.toString())
        txtCantidad.setText(actividad.cantidad.toString())
        txtPorcentaje.setText(actividad.porcentaje.toString())
        //Crear dialogo
        val myDialog = builder.setView(view)
            // Actualizar contacto
            .setPositiveButton("Actualizar") { dialog, whichButton ->
                val ctxtId=txtId.text.toString().toString().toInt()
                val ctxtName=txtName.text.toString().toString()
                val ctxtDescripcion=txtDescripcion.text.toString().toString()
                val ctxtCosto=txtCosto.text.toString().toDouble()
                val ctxtCantidad=txtCantidad.text.toString().toInt()
                val ctxtPorcentaje=txtPorcentaje.text.toString().toInt()

                updateActividades(Actividad(ctxtId, ctxtName,ctxtDescripcion,ctxtCosto, ctxtCantidad, ctxtPorcentaje ))
            }
            .setNegativeButton("Cancelar") { dialog, whichButton ->
                Toast.makeText(holder.view.context, "Actualización cancelada", Toast.LENGTH_LONG).show()
            }


        val b = builder.create()
        b.show()
    }

    fun showDeleteDialog(holder: ActividadViewHolder, actividad: Actividad) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)
        dialogBuilder.setTitle("Eliminar")
        dialogBuilder.setMessage("Confirma que se desea Eliminar la actividad?")
        dialogBuilder.setPositiveButton("Eliminar") { dialog, whichButton ->
            deleteActividad(actividad)
        }
        dialogBuilder.setNegativeButton("Cancelar") { dialog, whichButton ->
            dialog.cancel()
        }
        val b = dialogBuilder.create()
        b.show()
    }


}