package com.example.sportincenterapp.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.R
import com.example.sportincenterapp.interfaces.Communicator
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*


class Settings : Fragment() {

    // Communicator instance
    private lateinit var communicator: Communicator
    private var listColor = intArrayOf(R.color.black, R.color.red)
    private var state = intArrayOf(-android.R.attr.state_enabled, android.R.attr.state_enabled)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //settings view
        val v = inflater.inflate(R.layout.fragment_settings, container, false)

        //Components of the view

        val setting_mainLayout = v.findViewById<LinearLayout>(R.id.setting_mainLayout)
        val settings_title = v.findViewById<TextView>(R.id.settings_title)
        val settings_language = v.findViewById<TextView>(R.id.settings_language)
        val settings_spinnerLanguage_it = v.findViewById<Spinner>(R.id.settings_spinnerLanguage_it)
        val settings_spinnerLanguage_en = v.findViewById<Spinner>(R.id.settings_spinnerLanguage_en)
        val settings_theme = v.findViewById<TextView>(R.id.settings_theme)
        val settings_spinnerTheme_it = v.findViewById<Spinner>(R.id.settings_spinnerTheme_it)
        val settings_spinnerTheme_en = v.findViewById<Spinner>(R.id.settings_spinnerTheme_en)
        val settings_measure = v.findViewById<TextView>(R.id.settings_measure)
        val settings_version = v.findViewById<TextView>(R.id.settings_version)
        val settings_versionText = v.findViewById<TextView>(R.id.settings_versionText)
        val settings_UMRadiogroup = v.findViewById<RadioGroup>(R.id.settings_UMRadiogroup)
        val settings_radioButton1 = v.findViewById<RadioButton>(R.id.settings_radioButton1)
        val setting_UM1 = v.findViewById<TextView>(R.id.setting_UM1)
        val setting_UM2 = v.findViewById<TextView>(R.id.setting_UM2)

        //Communicator
        communicator = activity as Communicator

        /* DEFAULT COLOR ASSIGN */
        setting_mainLayout.setBackgroundResource(R.color.background_primary_color)
        setting_UM1.setTextColor(getResources().getColor(R.color.black))
        setting_UM2.setTextColor(getResources().getColor(R.color.black))
        settings_versionText.setTextColor(getResources().getColor(R.color.black))


        /* DEFAULT VALUE ASSIGN */
        settings_title.text = (getResources().getString(R.string.fr_settings_title_it))
        settings_language.text = (getResources().getString(R.string.fr_settings_language_it))
        settings_theme.text = (getResources().getString(R.string.fr_settings_theme_it))
        settings_measure.text = (getResources().getString(R.string.fr_settings_measure_it))
        settings_version.text = (getResources().getString(R.string.fr_settings_version_it))

        settings_spinnerTheme_it?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        communicator.theme(0)
                        setting_mainLayout.setBackgroundResource(R.color.background_primary_color)
                        setting_UM1.setTextColor(getResources().getColor(R.color.black))
                        setting_UM2.setTextColor(getResources().getColor(R.color.black))
                        settings_versionText.setTextColor(getResources().getColor(R.color.black))
                    }
                    1 -> {
                        communicator.theme(1)
                        setting_mainLayout.setBackgroundResource(R.color.background_primary_color_2)
                        setting_UM1.setTextColor(getResources().getColor(R.color.white))
                        setting_UM2.setTextColor(getResources().getColor(R.color.white))
                        settings_versionText.setTextColor(getResources().getColor(R.color.white))
                    }
                }
            }
        }

        settings_spinnerTheme_en?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        communicator.theme(0)
                        setting_mainLayout.setBackgroundResource(R.color.background_primary_color)
                        setting_UM1.setTextColor(getResources().getColor(R.color.black))
                        setting_UM2.setTextColor(getResources().getColor(R.color.black))
                        settings_versionText.setTextColor(getResources().getColor(R.color.black))
                    }
                    1 -> {
                        communicator.theme(1)
                        setting_mainLayout.setBackgroundResource(R.color.background_primary_color_2)
                        setting_UM1.setTextColor(getResources().getColor(R.color.white))
                        setting_UM2.setTextColor(getResources().getColor(R.color.white))
                        settings_versionText.setTextColor(getResources().getColor(R.color.white))
                    }
                }
            }
        }

        settings_spinnerLanguage_it?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        communicator.language(0)
                        settings_title.text = (getResources().getString(R.string.fr_settings_title_it))
                        settings_language.text = (getResources().getString(R.string.fr_settings_language_it))
                        settings_theme.text = (getResources().getString(R.string.fr_settings_theme_it))
                        settings_measure.text = (getResources().getString(R.string.fr_settings_measure_it))
                        settings_version.text = (getResources().getString(R.string.fr_settings_version_it))
                    }
                    1 -> {
                        communicator.language(1)
                        settings_title.text = (getResources().getString(R.string.fr_settings_title_en))
                        settings_language.text = (getResources().getString(R.string.fr_settings_language_en))
                        settings_theme.text = (getResources().getString(R.string.fr_settings_theme_en))
                        settings_measure.text = (getResources().getString(R.string.fr_settings_measure_en))
                        settings_version.text = (getResources().getString(R.string.fr_settings_version_en))
                        settings_spinnerLanguage_it.visibility = View.GONE
                        settings_spinnerLanguage_en.visibility = View.VISIBLE
                        settings_spinnerLanguage_en.setSelection(1)
                        settings_spinnerTheme_en.visibility = View.VISIBLE
                        settings_spinnerTheme_it.visibility = View.GONE
                        settings_spinnerTheme_en.setSelection(settings_spinnerTheme_it.selectedItemPosition)
                    }
                }
            }
        }

        settings_spinnerLanguage_en?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        settings_title.text = (getResources().getString(R.string.fr_settings_title_it))
                        settings_language.text = (getResources().getString(R.string.fr_settings_language_it))
                        settings_theme.text = (getResources().getString(R.string.fr_settings_theme_it))
                        settings_measure.text = (getResources().getString(R.string.fr_settings_measure_it))
                        settings_version.text = (getResources().getString(R.string.fr_settings_version_it))
                        settings_spinnerLanguage_it.visibility = View.VISIBLE
                        settings_spinnerLanguage_en.visibility = View.GONE
                        settings_spinnerLanguage_it.setSelection(0)
                        settings_spinnerTheme_it.visibility = View.VISIBLE
                        settings_spinnerTheme_en.visibility = View.GONE
                        settings_spinnerTheme_it.setSelection(settings_spinnerTheme_en.selectedItemPosition)
                    }
                    1 -> {
                        settings_title.text = (getResources().getString(R.string.fr_settings_title_en))
                        settings_language.text = (getResources().getString(R.string.fr_settings_language_en))
                        settings_theme.text = (getResources().getString(R.string.fr_settings_theme_en))
                        settings_measure.text = (getResources().getString(R.string.fr_settings_measure_en))
                        settings_version.text = (getResources().getString(R.string.fr_settings_version_en))
                    }
                }
            }
        }


        if (settings_radioButton1.isChecked) {
            communicator.um_update("Kg", "Cm")
        } else {
            communicator.um_update("Lib", "In")
        }

        //Listener for radioGroup
        settings_UMRadiogroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.settings_radioButton1 -> {
                    communicator.um_update("Kg", "Cm")
                } //Case RadioButton 1

                R.id.settings_radioButton2 -> {
                    communicator.um_update("Lib", "In")
                } //Case RadioButton 2
            }
        })
        return v
    }
}