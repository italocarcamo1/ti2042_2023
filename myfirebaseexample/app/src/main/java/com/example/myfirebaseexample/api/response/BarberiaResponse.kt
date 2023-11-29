package com.example.myfirebaseexample.api.response

import com.google.gson.annotations.SerializedName

data class BarberiaResponse(
    @SerializedName("00_Id") var id: String,
    @SerializedName("01_Servicio") var service: String,
    @SerializedName("01_Descuento") var discount: Long,
    @SerializedName("14_Genero") var gender: String,
    @SerializedName("16_Costo") var cost: Long
)