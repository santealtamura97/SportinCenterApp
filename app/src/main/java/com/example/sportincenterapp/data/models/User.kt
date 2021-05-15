package com.example.sportincenterapp.data.models

import com.google.gson.annotations.SerializedName

data class User (

    @SerializedName("id")
    var id: String,

    @SerializedName("displayName")
    var displayName: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("roles")
    private var roles: MutableList<String?>? = null
)