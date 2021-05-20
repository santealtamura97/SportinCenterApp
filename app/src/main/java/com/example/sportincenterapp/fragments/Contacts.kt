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

        /* DEFAULT VALUE COLOR */
        rlLayout.setBackgroundResource(arguments!!.getInt("color"))
        text_1.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        text_2.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        text_3.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        text_4.setTextColor(getResources().getColor(arguments!!.getInt("color")))

        return v
    }
}