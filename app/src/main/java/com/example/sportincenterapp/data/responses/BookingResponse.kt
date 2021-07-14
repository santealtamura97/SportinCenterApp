package com.example.sportincenterapp.data.responses

import com.google.gson.annotations.SerializedName

class BookingResponse {
    @SerializedName("id")
    private var id: Int? = null

    @SerializedName("title")
    private var title: String? = null

    @SerializedName("dataFine")
    private val dataFine: String? = null

    @SerializedName("inizio")
    private var inizio: String? = null

    @SerializedName("number")
    private var number: Any? = null

    constructor(id: Int,title: String?, inizio: String?, number: Any?) {
        this.id = id
        this.title = title
        this.inizio = inizio
        this.number = number
    }

    fun getId(): Int? {
        return id
    }

    fun getTitle(): String? {
        return title
    }

    fun getInizio(): String? {
        return inizio
    }
}