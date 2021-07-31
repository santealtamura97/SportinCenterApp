package com.example.sportincenterapp.utils

object Constant {

    //Base URL
    const val AUTH_BASE_URL = "http://192.168.1.8:8080"
    const val GATEWAY_BASE_URL = "http://192.168.1.8:5555"

    //Authentication's endpoints
    const val LOGIN_URL = "/api/auth/signin"
    const val SIGN_UP_URL = "/api/auth/signup"

    //Services endpoints
    const val CALENDAR_SERVICE = "/calendar-service"
    const val ACTIVITY_SERVICE = "/activity-service"
    const val SUBSCRIPTION_SERVICE = "/subscription-service"
}