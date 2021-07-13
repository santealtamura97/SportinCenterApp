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

        //layout
        val rlLayout = v.findViewById<LinearLayout>(R.id.title_contacts_layout)
        //text
        var text_1 = v.findViewById<TextView>(R.id.address_contats_text)
        var text_2 = v.findViewById<TextView>(R.id.telephone_contats_text)
        var text_3 = v.findViewById<TextView>(R.id.hour_contats_text)
        var text_4 = v.findViewById<TextView>(R.id.email_contats_text)
        var text_5 = v.findViewById<TextView>(R.id.contacts_title)
        var text_6 = v.findViewById<TextView>(R.id.hour_contacts_1)
        var text_7 = v.findViewById<TextView>(R.id.hour_contacts_2)
        var text_8 = v.findViewById<TextView>(R.id.address_contacts_1)

        /* DEFAULT VALUE COLOR */
        rlLayout.setBackgroundResource(arguments!!.getInt("color"))
        text_1.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        text_2.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        text_3.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        text_4.setTextColor(getResources().getColor(arguments!!.getInt("color")))

        /* STRING VALUE */
        text_1.setText(getResources().getString(arguments!!.getInt("string_contact_1")))
        text_2.setText(getResources().getString(arguments!!.getInt("string_contact_2")))
        text_3.setText(getResources().getString(arguments!!.getInt("string_contact_3")))
        text_4.setText(getResources().getString(arguments!!.getInt("string_contact_4")))
        text_5.setText(getResources().getString(arguments!!.getInt("string_contact_5")))
        text_6.setText(getResources().getString(arguments!!.getInt("string_contact_6")))
        text_7.setText(getResources().getString(arguments!!.getInt("string_contact_7")))
        text_8.setText(getResources().getString(arguments!!.getInt("string_contact_8")))

        return v
    }
}