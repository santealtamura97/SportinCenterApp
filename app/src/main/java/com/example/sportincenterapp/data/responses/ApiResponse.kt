package com.example.sportincenterapp.data.responses

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("success")
    var success: Boolean,

    @SerializedName("message")
    var message: String

)
