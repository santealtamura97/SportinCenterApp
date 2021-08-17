package com.example.sportincenterapp.data.requests


import com.google.gson.annotations.SerializedName

data class SignUpRequest (
        @SerializedName("displayName")
        var displayName: String,

        @SerializedName("email")
        var email: String,

        @SerializedName("password")
        var password: String,

        @SerializedName("matchingPassword")
        var matchingPassword: String,

        @SerializedName("enabled")
        var enabled: Boolean,

        @SerializedName("abbonamento")
        var abbonamento: String,

        @SerializedName("dataScadenza")
        var dataScadenza: String,

        @SerializedName("ingressi")
        var ingressi: Long

)