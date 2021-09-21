package com.example.sportincenterapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.requests.SignUpRequest
import com.example.sportincenterapp.data.responses.SignUpResponse
import com.example.sportincenterapp.data.responses.UserCodeResponse
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.RegistrationUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern


class RegistrationActivity : AppCompatActivity() {
    private var userCode: EditText? = null
    private var registerEmail: EditText? = null
    private  var registerPassword: EditText? = null
    private var registerConfirmPassword: EditText? = null

    private lateinit var apiClient: ApiClient
    private lateinit var displayName: String
    private lateinit var idSubscriptionUser: String
    private lateinit var scadenzaAbbonamento: String
    private var ingressi: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        userCode = findViewById(R.id.user_code)
        registerEmail = findViewById(R.id.register_email)
        registerPassword = findViewById(R.id.register_password)
        registerConfirmPassword = findViewById(R.id.confirm_register_password)

        registerEmail!!.isEnabled = false
        registerPassword!!.isEnabled = false
        registerConfirmPassword!!.isEnabled = false
        userCode?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length == 6) {
                    apiClient = ApiClient()
                    apiClient.getApiServiceAuth(applicationContext).validateUserCode(s.toString())
                        .enqueue(object : Callback<UserCodeResponse> {
                            override fun onFailure(call: Call<UserCodeResponse>, t: Throwable) {
                                userCode!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, R.drawable.ic_baseline_error_24, 0);
                            }

                            override fun onResponse(call: Call<UserCodeResponse>, response: Response<UserCodeResponse>) {
                                if (response.isSuccessful) {
                                    val userCodeResponse = response.body()
                                    println(userCodeResponse)
                                    displayName = userCodeResponse!!.displayName
                                    idSubscriptionUser = userCodeResponse!!.idAbbonamento
                                    var newUserCodeString = userCode!!.text.toString() + " (" + displayName + ")"
                                    userCode!!.setText(newUserCodeString)
                                    userCode!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, R.drawable.ic_baseline_done_all_24, 0);
                                    registerEmail!!.isEnabled = true
                                    registerEmail!!.requestFocus()
                                    userCode!!.isEnabled = false
                                    registerPassword!!.isEnabled = true
                                    registerConfirmPassword!!.isEnabled = true
                                    scadenzaAbbonamento = userCodeResponse!!.scadenzaAbbonamento
                                    ingressi = userCodeResponse!!.ingressi

                                }else {
                                    userCode!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, R.drawable.ic_baseline_error_24, 0);
                                }
                            }
                        })
                }
            }
        })

        findViewById<View>(R.id.btnRegistration).setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val email = registerEmail!!.text.toString().trim { it <= ' ' }
        val password = registerPassword!!.text.toString().trim { it <= ' ' }
        val confirmPassword = registerConfirmPassword!!.text.toString().trim { it <= ' ' }

        if (email.isEmpty() || !RegistrationUtils.isEmailValid(email)) {
            registerEmail!!.error = resources.getString(R.string.missing_email_message)
            registerEmail!!.requestFocus()
            return
        } else if (password.isEmpty() || !RegistrationUtils.isValidPassword(password)) {
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
        apiClient.getApiServiceAuth(this).signUp(SignUpRequest(displayName,email,password,confirmPassword,enabled = false, abbonamento = idSubscriptionUser,
            dataScadenza = scadenzaAbbonamento, ingressi = ingressi))
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

