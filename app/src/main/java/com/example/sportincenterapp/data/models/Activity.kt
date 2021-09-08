package com.example.sportincenterapp.data.models

import com.google.gson.annotations.SerializedName

data class Activity(
    @SerializedName("id")
    var id: String,

    @SerializedName("nameIta")
    var nameIta: String,

    @SerializedName("descrIta")
    var descrIta: String,

    @SerializedName("nameEng")
    var nameEng: String,

    @SerializedName("descrEng")
    var descrEng: String
)
