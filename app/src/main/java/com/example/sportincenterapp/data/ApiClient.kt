package com.example.sportincenterapp.data

import com.example.sportincenterapp.utils.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * This class initialize the Retrofit client instance
 */
class ApiClient {

    //lateinit' allows initializing a not-null property outside of a constructor
    private lateinit var apiService: ApiService

    fun getApiService(): ApiService {


        //Initialize ApiService if not initialize yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }



}