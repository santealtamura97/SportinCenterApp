package com.example.sportincenterapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.requests.LoginRequest
import com.example.sportincenterapp.data.responses.LoginResponse
import com.example.sportincenterapp.fragments.CalendarFragment
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private var etUsername: EditText? = null
    private  var etPassword: EditText? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUserName)
        etPassword = findViewById(R.id.etPassword)

        findViewById<View>(R.id.btnLogin).setOnClickListener { loginUser() }
        findViewById<TextView>(R.id.registration_link).setOnClickListener {
            intent = Intent(ApplicationContextProvider.getContext(), RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = etUsername!!.text.toString().trim { it <= ' ' }
        val password = etPassword!!.text.toString().trim { it <= ' ' }

        if (email.isEmpty()) {
            etUsername!!.error = resources.getString(R.string.missing_email_message)
            etUsername!!.requestFocus()
            return
        } else if (password.isEmpty()) {
            etPassword!!.error = resources.getString(R.string.missing_password_message)
            etPassword!!.requestFocus()
            return
        }

        apiClient = ApiClient()
        sessionManager = SessionManager(ApplicationContextProvider.getContext())

        apiClient.getApiServiceAuth(this).login(LoginRequest(email,password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.login_error), Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()

                    if (loginResponse?.statusCode == 0 && loginResponse.user != null) {
                        sessionManager.saveAuthToken(loginResponse.accessToken)
                        sessionManager.saveUserId(loginResponse.user.id)
                        sessionManager.saveUsername(loginResponse.user.displayName)
                        println(loginResponse.user.displayName)
                        sessionManager.saveUserEmail(loginResponse.user.email)
                        intent = Intent(ApplicationContextProvider.getContext(), MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.login_error), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
}