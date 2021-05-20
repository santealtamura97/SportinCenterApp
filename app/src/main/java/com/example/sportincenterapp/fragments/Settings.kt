package com.example.sportincenterapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.R


class Settings : Fragment() {

    // Communicator instance
    private lateinit var communicator: Communicator

    //Default value
    val default_language : String = "Italiano"
    val default_version : String = "1.0.0"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //settings view
        val v = inflater.inflate(R.layout.fragment_settings, container, false)

        //Components of the view
        //Linear layout
        val lrLayout = v.findViewById<LinearLayout>(R.id.title_settings_layout)
        //Language
        val language = v.findViewById<TextView>(R.id.settings_language)
        val language_text = v.findViewById<TextView>(R.id.language_text)
        //Theme
        val theme_text = v.findViewById<TextView>(R.id.theme_text)
        val spinner_theme = v.findViewById<Spinner>(R.id.spinner_theme)
        //Measure
        val measure_text = v.findViewById<TextView>(R.id.misure_text)
        //Version
        val version = v.findViewById<TextView>(R.id.settings_version)
        val version_text = v.findViewById<TextView>(R.id.version_text)
        //Radiobutton
        val um_radioGroup = v.findViewById<RadioGroup>(R.id.radioGroup)
        val radioButton_1 = v.findViewById<RadioButton>(R.id.radioButton_1)
        val radioButton_2 = v.findViewById<RadioButton>(R.id.radioButton_2)

        //Communicator
        communicator  = activity as Communicator

        /* DEFAULT VALUE ASSIGN */
        language.text = default_language
        version.text = default_version

        /* DEFAULT COLOR ASSIGN */
        lrLayout.setBackgroundResource(R.color.primary_color)
        language_text.setTextColor(getResources().getColor(R.color.primary_color))
        theme_text.setTextColor(getResources().getColor(R.color.primary_color))
        measure_text.setTextColor(getResources().getColor(R.color.primary_color))
        version_text.setTextColor(getResources().getColor(R.color.primary_color))

        spinner_theme?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        communicator.theme(0)
                        lrLayout.setBackgroundResource(R.color.primary_color)
                        language_text.setTextColor(getResources().getColor(R.color.primary_color))
                        theme_text.setTextColor(getResources().getColor(R.color.primary_color))
                        measure_text.setTextColor(getResources().getColor(R.color.primary_color))
                        version_text.setTextColor(getResources().getColor(R.color.primary_color))
                    }
                    1 -> {
                        communicator.theme(1)
                        lrLayout.setBackgroundResource(R.color.primary_color_2)
                        language_text.setTextColor(getResources().getColor(R.color.primary_color_2))
                        theme_text.setTextColor(getResources().getColor(R.color.primary_color_2))
                        measure_text.setTextColor(getResources().getColor(R.color.primary_color_2))
                        version_text.setTextColor(getResources().getColor(R.color.primary_color_2))
                    }
                    2 -> {
                        communicator.theme(2)
                        lrLayout.setBackgroundResource(R.color.primary_color_3)
                        language_text.setTextColor(getResources().getColor(R.color.primary_color_3))
                        theme_text.setTextColor(getResources().getColor(R.color.primary_color_3))
                        measure_text.setTextColor(getResources().getColor(R.color.primary_color_3))
                        version_text.setTextColor(getResources().getColor(R.color.primary_color_3))
                    }
                }
            }
        }


        if (radioButton_1.isChecked) {
            communicator.um_update("Kg", "Cm")
        } else {
            communicator.um_update("Lib", "In")
        }

                //Listener for radioGroup
        um_radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton_1 ->  { communicator.um_update("Kg", "Cm") } //Case RadioButton 1

                R.id.radioButton_2 -> { communicator.um_update("Lib", "In") } //Case RadioButton 2
            }
        })




return v
}

}