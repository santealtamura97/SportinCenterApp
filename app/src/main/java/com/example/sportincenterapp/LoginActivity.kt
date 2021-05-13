package com.example.sportincenterapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.requests.LoginRequest
import com.example.sportincenterapp.data.responses.LoginResponse
import com.example.sportincenterapp.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private var etUsername: EditText? = null
    private  var etPassword: EditText? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    var context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById<EditText>(R.id.etUserName)
        etPassword = findViewById<EditText>(R.id.etPassword)

        findViewById<View>(R.id.btnLogin).setOnClickListener { loginUser() }
    }

    private fun loginUser() {
        val email = etUsername!!.text.toString().trim { it <= ' ' }
        val password = etPassword!!.text.toString().trim { it <= ' ' }

        if (email.isEmpty()) {
            etUsername!!.error = "Inserisci l'Username"
            etUsername!!.requestFocus()
            return
        } else if (password.isEmpty()) {
            etPassword!!.error = "Inserisci la Password"
            etPassword!!.requestFocus()
            return
        }

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        apiClient.getApiService().login(LoginRequest(email,password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Error logging in
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()

                    if (loginResponse?.statusCode == 0 && loginResponse.user != null) {
                        sessionManager.saveAuthToken(loginResponse.accessToken)
                        sessionManager.saveUserId(loginResponse.user.id)
                        sessionManager.saveUsername(loginResponse.user.displayName)
                        sessionManager.saveUserEmail(loginResponse.user.email)
                        intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Error logging in
                    }
                }
            })
    }
}