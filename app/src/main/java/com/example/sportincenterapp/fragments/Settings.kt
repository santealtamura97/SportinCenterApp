package com.example.sportincenterapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.RadioGroup
import android.widget.RadioButton
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.R


class Settings : Fragment() {

    // Communicator instance
    private lateinit var communicator: Communicator

    //Default value
    val default_language : String = "Italiano"
    val default_theme : String = "Tema 1"
    val default_version : String = "1.0.0"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //settings view
        val v = inflater.inflate(R.layout.fragment_settings, container, false)

        //Components of the view
        val language = v.findViewById<TextView>(R.id.settings_language)
        val theme = v.findViewById<TextView>(R.id.settings_theme)
        val version = v.findViewById<TextView>(R.id.settings_version)

        //default value assign
        language.text = default_language
        theme.text = default_theme
        version.text = default_version


        val um_radioGroup = v.findViewById<RadioGroup>(R.id.radioGroup)
        val radioButton_1 = v.findViewById<RadioButton>(R.id.radioButton_1)
        val radioButton_2 = v.findViewById<RadioButton>(R.id.radioButton_2)

        communicator  = activity as Communicator


        if (radioButton_1.isChecked) {
            communicator.um_update("Kg", "Cm")
        } else {
            communicator.um_update("Lib", "In")
        }

                //Listener for radioGroup
        um_radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton_1 ->  {communicator.um_update("Kg", "Cm") } //Case RadioButton 1
                R.id.radioButton_2 -> { communicator.um_update("Lib", "In") } //Case RadioButton 2
            }
        })




return v
}

}