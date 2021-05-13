package com.example.sportincenterapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView


class Settings : Fragment() {

    // Communicator instance
    private lateinit var communicator: Communicator

    //Default value
    val default_language : String = "Italiano"
    val default_email : String = "andrea.forino@edu.unito.it"
    val default_password : String = "SportingCenter99"
    val default_version : String = "1.0.0"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //settings view
        val v = inflater.inflate(R.layout.fragment_settings, container, false)

        //Components of the view
        val language = v.findViewById<TextView>(R.id.settings_language)
        val email = v.findViewById<EditText>(R.id.settings_email)
        val password = v.findViewById<EditText>(R.id.settings_password)
        val version = v.findViewById<TextView>(R.id.settings_version)

        //default value assign
        language.text = default_language
        email.setText(default_email)
        password.setText(default_password)
        version.text = default_version

        //Communicator passData example (not already used)
        communicator = activity as Communicator
        communicator.user_email_update(default_email)

        //Listener for email edit text
        email.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                communicator.user_email_update(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}
        })

        return v
    }

}