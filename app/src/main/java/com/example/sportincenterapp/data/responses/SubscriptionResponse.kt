package com.example.sportincenterapp.data.responses

import com.google.gson.annotations.SerializedName

data class SubscriptionResponse(
    @SerializedName("name")
    var name: String,

    @SerializedName("fitness")
    var fitness: Boolean,

    @SerializedName("nuoto")
    var nuoto: Boolean
)
