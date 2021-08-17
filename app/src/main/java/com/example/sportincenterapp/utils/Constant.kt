package com.example.sportincenterapp.utils

object Constant {

    //Base URL
    const val AUTH_BASE_URL = "http://192.168.1.8:8080"
    const val GATEWAY_BASE_URL = "http://192.168.1.8:5555"
    const val CALENDAR_BASE_URL = "http://192.168.1.8:9003"

    //Authentication's endpoints
    const val LOGIN_URL = "/api/auth/signin"
    const val SIGN_UP_URL = "/api/auth/signup"
    const val VALIDATE_USER_CODE_URL = "/api/auth/validateUserCode"
    const val USER_INFO_URL = "/api/auth/userbyid"

    //Services endpoints
    const val CALENDAR_SERVICE = "/calendar-service"
    const val ACTIVITY_SERVICE = "/activity-service"
    const val SUBSCRIPTION_SERVICE = "/subscription-service"
}