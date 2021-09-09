package com.example.sportincenterapp.data.models

import android.content.res.TypedArray
import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import java.io.ByteArrayInputStream
import java.io.InputStream

data class User (

    @SerializedName("id")
    var id: String,

    @SerializedName("displayName")
    var displayName: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("roles")
    private var roles: MutableList<String?>? = null,

    @SerializedName("idAbbonamento")
    var idAbbonamento: String,

    @SerializedName("scadenzaAbbonamento")
    var scadenzaAbbonamento: String,

    @SerializedName("ingressi")
    var ingressi: Long,

    @SerializedName("exipred")
    var expired: Boolean,

    @SerializedName("number")
    var number: String
)