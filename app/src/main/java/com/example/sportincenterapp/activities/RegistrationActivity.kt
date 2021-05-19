package com.example.sportincenterapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.requests.SignUpRequest
import com.example.sportincenterapp.data.responses.SignUpResponse
import com.example.sportincenterapp.utils.ApplicationContextProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {
    private var displayName: EditText? = null
    private var registerEmail: EditText? = null
    private  var registerPassword: EditText? = null
    private var registerConfirmPassword: EditText? = null

    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        displayName = findViewById(R.id.display_name)
        registerEmail = findViewById(R.id.register_email)
        registerPassword = findViewById(R.id.register_password)
        registerConfirmPassword = findViewById(R.id.confirm_register_password)


        findViewById<View>(R.id.btnRegistration).setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val name = displayName!!.text.toString().trim { it <= ' ' }
        val email = registerEmail!!.text.toString().trim { it <= ' ' }
        val password = registerPassword!!.text.toString().trim { it <= ' ' }
        val confirmPassword = registerConfirmPassword!!.text.toString().trim { it <= ' ' }

        if (name.isEmpty()) {
            displayName!!.error = resources.getString(R.string.missing_display_name_message)
            displayName!!.requestFocus()
            return
        } else if (email.isEmpty()) {
            registerEmail!!.error = resources.getString(R.string.missing_email_message)
            registerEmail!!.requestFocus()
            return
        } else if (password.isEmpty()) {
            registerPassword!!.error = resources.getString(R.string.missing_password_message)
            registerPassword!!.requestFocus()
            return
        }else if (confirmPassword.isEmpty()) {
            registerConfirmPassword!!.error = resources.getString(R.string.missing_confirm_password_message)
            registerConfirmPassword!!.requestFocus()
            return
        } else if (password != confirmPassword) {
            registerConfirmPassword!!.error = resources.getString(R.string.password_not_equal_confirm_password_message)
            registerConfirmPassword!!.requestFocus()
        }

        apiClient = ApiClient()
        apiClient.getApiService().signUp(SignUpRequest(name,email,password,confirmPassword,enabled = false))
                .enqueue(object : Callback<SignUpResponse> {
                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.signup_error), Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.signup_success), Toast.LENGTH_LONG).show()
                            intent = Intent(ApplicationContextProvider.getContext(), LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                })
    }
}

