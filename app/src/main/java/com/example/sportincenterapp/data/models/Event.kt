package com.example.sportincenterapp.data.models

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("id")
    var id: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("data")
    var data: String,

    @SerializedName("oraInizio")
    var oraInizio: String,

    @SerializedName("oraFine")
    var oraFine: String,

    @SerializedName("number")
    var number: Long,

    @SerializedName("activityId")
    var activityId: String,

    @SerializedName("isSelectable")
    var isSelectable: Boolean = false,

    @SerializedName("selected")
    var selected: Boolean = false
)
