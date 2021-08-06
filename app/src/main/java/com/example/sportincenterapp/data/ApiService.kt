package com.example.sportincenterapp.data

import com.example.sportincenterapp.data.models.Activity
import com.example.sportincenterapp.data.requests.LoginRequest
import com.example.sportincenterapp.data.requests.SignUpRequest
import com.example.sportincenterapp.data.responses.LoginResponse
import com.example.sportincenterapp.data.responses.SignUpResponse
import com.example.sportincenterapp.data.responses.SubscriptionResponse
import com.example.sportincenterapp.data.responses.UserCodeResponse
import com.example.sportincenterapp.utils.Constant
import retrofit2.Call
import retrofit2.http.*

/**
 * Interface for defining REST request functions
 */
interface ApiService {

    @POST(Constant.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST(Constant.SIGN_UP_URL)
    fun signUp(@Body request: SignUpRequest) : Call<SignUpResponse>

    @POST(Constant.VALIDATE_USER_CODE_URL)
    fun validateUserCode(@Body request: String) : Call<UserCodeResponse>

    @GET(Constant.SUBSCRIPTION_SERVICE + "/user/getSubfromid/{idAbbonamento}")
    fun getSubfromid(@Path(value = "idAbbonamento") idAbbonamento: String) : Call<SubscriptionResponse>

    @GET(Constant.ACTIVITY_SERVICE + "/all/activities")
    fun getSportActivities(): Call <List<Activity>>

}