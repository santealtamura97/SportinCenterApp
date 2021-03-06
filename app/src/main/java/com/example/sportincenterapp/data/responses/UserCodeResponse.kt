package com.example.sportincenterapp.data.responses

import com.google.gson.annotations.SerializedName

data class UserCodeResponse(
    @SerializedName("displayName")
    var displayName : String,

    @SerializedName("idAbbonamento")
    var idAbbonamento : String,

    @SerializedName("scadenzaAbbonamento")
    var scadenzaAbbonamento: String,

    @SerializedName("ingressi")
    var ingressi: Long

    )

