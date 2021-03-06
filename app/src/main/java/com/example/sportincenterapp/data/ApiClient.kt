package com.example.sportincenterapp.data

import android.content.Context
import com.example.sportincenterapp.utils.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * This class initialize the Retrofit client instance
 */
class ApiClient {

    //lateinit allows initializing a not-null property outside of a constructor
    private lateinit var apiService: ApiService

    fun getApiServiceAuth(context: Context): ApiService {


        //Initialize ApiService if not initialize yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constant.AUTH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }

    fun getApiServiceGateway(context: Context): ApiService {
        //Initialize ApiService if not initialize yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constant.GATEWAY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }

    fun getApiServiceCalendar(context: Context): ApiService {
        //Initialize ApiService if not initialize yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constant.CALENDAR_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }

    fun getApiServiceActivity(context: Context): ApiService {
        //Initialize ApiService if not initialize yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constant.ACTIVITY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .build()
    }



}