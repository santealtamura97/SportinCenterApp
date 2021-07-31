package com.example.sportincenterapp.data.models

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("id")
    var id: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("dataFine")
    var dataFine: String,

    @SerializedName("inizio")
    var inizio: String,

    @SerializedName("number")
    var number: Long
)
