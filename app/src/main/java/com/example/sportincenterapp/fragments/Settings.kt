package com.example.sportincenterapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.R
import com.example.sportincenterapp.interfaces.Communicator


class Settings : Fragment() {

    // Communicator instance
    private lateinit var communicator: Communicator

    //Default value
    val default_version : String = "1.0.0"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        //settings view
        val v = inflater.inflate(R.layout.fragment_settings, container, false)

        //Components of the view
        //Linear layout
        val lrLayout = v.findViewById<LinearLayout>(R.id.title_settings_layout)
        //Title
        val title_setting_text = v.findViewById<TextView>(R.id.text_title_settings)
        //Language
        val language_text = v.findViewById<TextView>(R.id.language_text)
        val spinner_language = v.findViewById<Spinner>(R.id.spinner_language)
        val spinner_language_en = v.findViewById<Spinner>(R.id.spinner_language_en)
        //Theme
        val theme_text = v.findViewById<TextView>(R.id.theme_text)
        val spinner_theme = v.findViewById<Spinner>(R.id.spinner_theme)
        val spinner_theme_en = v.findViewById<Spinner>(R.id.spinner_theme_en)
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
        communicator = activity as Communicator

        /* DEFAULT VALUE ASSIGN */
        version.text = default_version
        title_setting_text.text = (getResources().getString(R.string.settings_title))
        language_text.text = (getResources().getString(R.string.settings_language))
        theme_text.text = (getResources().getString(R.string.settings_theme))

        /* DEFAULT COLOR ASSIGN */
        lrLayout.setBackgroundResource(R.color.orange)
        language_text.setTextColor(getResources().getColor(R.color.orange))
        theme_text.setTextColor(getResources().getColor(R.color.orange))
        measure_text.setTextColor(getResources().getColor(R.color.orange))
        version_text.setTextColor(getResources().getColor(R.color.orange))

        spinner_theme?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        communicator.theme(0)
                        lrLayout.setBackgroundResource(R.color.orange)
                        language_text.setTextColor(getResources().getColor(R.color.orange))
                        theme_text.setTextColor(getResources().getColor(R.color.orange))
                        measure_text.setTextColor(getResources().getColor(R.color.orange))
                        version_text.setTextColor(getResources().getColor(R.color.orange))
                    }
                    1 -> {
                        communicator.theme(1)
                        lrLayout.setBackgroundResource(R.color.orange)
                        language_text.setTextColor(getResources().getColor(R.color.orange))
                        theme_text.setTextColor(getResources().getColor(R.color.orange))
                        measure_text.setTextColor(getResources().getColor(R.color.orange))
                        version_text.setTextColor(getResources().getColor(R.color.orange))
                    }
                }
            }
        }

        spinner_theme_en?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        communicator.theme(0)
                        lrLayout.setBackgroundResource(R.color.orange)
                        language_text.setTextColor(getResources().getColor(R.color.orange))
                        theme_text.setTextColor(getResources().getColor(R.color.orange))
                        measure_text.setTextColor(getResources().getColor(R.color.orange))
                        version_text.setTextColor(getResources().getColor(R.color.orange))
                    }
                    1 -> {
                        communicator.theme(1)
                        lrLayout.setBackgroundResource(R.color.orange)
                        language_text.setTextColor(getResources().getColor(R.color.orange))
                        theme_text.setTextColor(getResources().getColor(R.color.orange))
                        measure_text.setTextColor(getResources().getColor(R.color.orange))
                        version_text.setTextColor(getResources().getColor(R.color.orange))
                    }
                }
            }
        }

        spinner_language?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        communicator.language(0)
                        title_setting_text.text =
                            (getResources().getString(R.string.settings_title))
                        language_text.text = (getResources().getString(R.string.settings_language))
                        theme_text.text = (getResources().getString(R.string.settings_theme))
                        measure_text.text = (getResources().getString(R.string.um))
                        version_text.text = (getResources().getString(R.string.settings_version))
                    }
                    1 -> {
                        communicator.language(1)
                        title_setting_text.text =
                            (getResources().getString(R.string.settings_title_en))
                        language_text.text =
                            (getResources().getString(R.string.settings_language_en))
                        theme_text.text = (getResources().getString(R.string.settings_theme_en))
                        measure_text.text = (getResources().getString(R.string.um_en))
                        version_text.text = (getResources().getString(R.string.settings_version_en))
                        spinner_language.visibility = View.GONE
                        spinner_language_en.visibility = View.VISIBLE
                        spinner_language_en.setSelection(1)
                        spinner_theme_en.visibility = View.VISIBLE
                        spinner_theme.visibility = View.GONE
                        spinner_theme_en.setSelection(spinner_theme.selectedItemPosition)
                    }
                }
            }
        }

        spinner_language_en?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        communicator.language(0)
                        title_setting_text.text =
                            (getResources().getString(R.string.settings_title))
                        language_text.text = (getResources().getString(R.string.settings_language))
                        theme_text.text = (getResources().getString(R.string.settings_theme))
                        measure_text.text = (getResources().getString(R.string.um))
                        version_text.text = (getResources().getString(R.string.settings_version))
                        spinner_language.visibility = View.VISIBLE
                        spinner_language_en.visibility = View.GONE
                        spinner_language.setSelection(0)
                        spinner_theme.visibility = View.VISIBLE
                        spinner_theme_en.visibility = View.GONE
                        spinner_theme.setSelection(spinner_theme_en.selectedItemPosition)
                    }
                    1 -> {
                        communicator.language(1)
                        title_setting_text.text =
                            (getResources().getString(R.string.settings_title_en))
                        language_text.text =
                            (getResources().getString(R.string.settings_language_en))
                        theme_text.text = (getResources().getString(R.string.settings_theme_en))
                        measure_text.text = (getResources().getString(R.string.um_en))
                        version_text.text = (getResources().getString(R.string.settings_version_en))
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
                R.id.radioButton_1 -> {
                    communicator.um_update("Kg", "Cm")
                } //Case RadioButton 1

                R.id.radioButton_2 -> {
                    communicator.um_update("Lib", "In")
                } //Case RadioButton 2
            }
        })
        return v
    }
}