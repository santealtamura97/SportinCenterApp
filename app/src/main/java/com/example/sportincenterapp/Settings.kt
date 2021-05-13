package com.example.sportincenterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class Settings : Fragment() {

    // Communicator instance
    private lateinit var communicator: Communicator

    //Default value
    val default_language : String = "Italiano"
    val default_email : String = "andrea.forino@edu.unito.it"
    val default_password : String = "********"
    val default_version : String = "1.0.0"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //settings view
        val v = inflater.inflate(R.layout.fragment_settings, container, false)

        //Components of the view
        val language = v.findViewById<TextView>(R.id.settings_language)
        val email = v.findViewById<TextView>(R.id.settings_email)
        val password = v.findViewById<TextView>(R.id.settings_password)
        val version = v.findViewById<TextView>(R.id.settings_version)

        //default value assign
        language.text = default_language
        email.text = default_email
        password.text = default_password
        version.text = default_version

        //Communicator passData example (not already used)
        communicator = activity as Communicator
        communicator.user_email_update(default_email)

        return v
    }

}