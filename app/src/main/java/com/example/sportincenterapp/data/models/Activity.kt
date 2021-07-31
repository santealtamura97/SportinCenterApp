package com.example.sportincenterapp.data.models

import com.google.gson.annotations.SerializedName

data class Activity(
    @SerializedName("id")
    var id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("descr")
    var descr: String
)
