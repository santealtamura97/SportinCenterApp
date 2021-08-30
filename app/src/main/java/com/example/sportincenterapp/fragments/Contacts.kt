package com.example.sportincenterapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.sportincenterapp.R

class Contacts : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_contacts, container, false)

        val contacts_mainLayout = v.findViewById<LinearLayout>(R.id.contacts_mainLayout)
        var contacts_title = v.findViewById<TextView>(R.id.contacts_title)
        var contacts_timeTableText = v.findViewById<TextView>(R.id.contacts_timeTableText)
        var contacts_timeTableTextValue1 = v.findViewById<TextView>(R.id.contacts_timeTableTextValue1)
        var contacts_timeTableTextValue2 = v.findViewById<TextView>(R.id.contacts_timeTableTextValue2)
        var contacts_addressText = v.findViewById<TextView>(R.id.contacts_addressText)
        var contacts_addressValue = v.findViewById<TextView>(R.id.contacts_addressValue)
        var contacts_telephoneText = v.findViewById<TextView>(R.id.contacts_telephoneText)
        var contacts_telephoneValue1 = v.findViewById<TextView>(R.id.contacts_telephoneValue1)
        var contacts_telephoneValue2 = v.findViewById<TextView>(R.id.contacts_telephoneValue2)
        var contacts_telephoneValue3 = v.findViewById<TextView>(R.id.contacts_telephoneValue3)
        var contacts_emailText = v.findViewById<TextView>(R.id.contacts_emailText)
        var contacts_emailValue = v.findViewById<TextView>(R.id.contacts_emailValue)

        /* DEFAULT VALUE COLOR */
        contacts_mainLayout.setBackgroundResource(arguments!!.getInt("cl_contacts_background"))
        contacts_timeTableTextValue1.setTextColor(getResources().getColor(arguments!!.getInt("cl_contacts_text")))
        contacts_timeTableTextValue2.setTextColor(getResources().getColor(arguments!!.getInt("cl_contacts_text")))
        contacts_addressValue.setTextColor(getResources().getColor(arguments!!.getInt("cl_contacts_text")))
        contacts_telephoneValue1.setTextColor(getResources().getColor(arguments!!.getInt("cl_contacts_text")))
        contacts_telephoneValue2.setTextColor(getResources().getColor(arguments!!.getInt("cl_contacts_text")))
        contacts_telephoneValue3.setTextColor(getResources().getColor(arguments!!.getInt("cl_contacts_text")))
        contacts_emailValue.setTextColor(getResources().getColor(arguments!!.getInt("cl_contacts_text")))


        /* STRING VALUE */
        contacts_title.setText(getResources().getString(arguments!!.getInt("st_contacts_title")))
        contacts_timeTableText.setText(getResources().getString(arguments!!.getInt("st_contacts_timetableText")))
        contacts_timeTableTextValue1.setText(getResources().getString(arguments!!.getInt("st_contacts_timetableValue1")))
        contacts_timeTableTextValue2.setText(getResources().getString(arguments!!.getInt("st_contacts_timetableValue2")))
        contacts_addressText.setText(getResources().getString(arguments!!.getInt("st_contacts_addressText")))
        contacts_addressValue.setText(getResources().getString(arguments!!.getInt("st_contacts_addressValue")))
        contacts_telephoneText.setText(getResources().getString(arguments!!.getInt("st_contacts_TelephoneText")))

        return v
    }
}