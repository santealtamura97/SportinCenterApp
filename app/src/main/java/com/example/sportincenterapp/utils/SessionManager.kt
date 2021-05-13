package com.example.sportincenterapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.sportincenterapp.R


/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (context: Context){
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
        const val USERNAME = "username"
        const val EMAIL = "email"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchUserName(): String? {
        return prefs.getString(USERNAME, null)
    }

    fun fetchUserEmail(): String? {
        return prefs.getString(EMAIL, null)
    }

    fun saveUserId(id: String) {
        val editor = prefs.edit()
        editor.putString(USER_ID, id)
        editor.apply()
    }

    fun saveUsername(userName: String) {
        val editor = prefs.edit()
        editor.putString(USERNAME, userName)
        editor.apply()
    }

    fun saveUserEmail(email:String) {
        val editor = prefs.edit()
        editor.putString(EMAIL, email)
        editor.apply()
    }

}