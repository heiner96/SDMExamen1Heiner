package com.example.appexamen1.model

import com.google.gson.annotations.SerializedName


data class Actividad(
    @SerializedName("idActividad") var idActividad      : Int?     = null,
    @SerializedName("nombre"    ) var name    : String?  = null,
    @SerializedName("descripcion"   ) var descripcion   : String?  = null,
    @SerializedName("costo" ) var costo : Double?  = 0.0,
    @SerializedName("cantidadPersonasDescuento"  ) var cantidad  : Int? = 0,
    @SerializedName("porcentajeDescuento"  ) var porcentaje  : Int? = 0,
)

