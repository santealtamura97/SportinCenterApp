package com.example.sportincenterapp.data

import com.example.sportincenterapp.data.requests.LoginRequest
import com.example.sportincenterapp.data.responses.LoginResponse
import com.example.sportincenterapp.utils.Constant
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interface for defining REST request functions
 */
interface ApiService {

    @POST(Constant.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>

}