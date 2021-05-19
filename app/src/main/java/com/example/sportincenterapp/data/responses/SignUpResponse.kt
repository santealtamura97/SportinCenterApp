package com.example.sportincenterapp.data.responses

import com.google.gson.annotations.SerializedName

data class SignUpResponse (
        @SerializedName("success")
        var success: Boolean,

        @SerializedName("message")
        var message: String
)
