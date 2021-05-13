package com.example.sportincenterapp.data.responses

import com.example.sportincenterapp.data.models.User
import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @SerializedName("status_code")
    var statusCode: Int,

    @SerializedName("accessToken")
    var accessToken: String,

    @SerializedName("user")
    var user: User
)
