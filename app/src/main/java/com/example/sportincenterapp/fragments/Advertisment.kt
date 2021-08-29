package com.example.sportincenterapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.sportincenterapp.R
import com.google.android.material.card.MaterialCardView

class Advertisment : Fragment() {
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Exctract the view
        val v = inflater.inflate(R.layout.fragment_advertisment, container, false)

        //Extract the component of the view
        val advertisment_mainLayout = v.findViewById<LinearLayout>(R.id.advertisment_mainLayout)
        val advertisment_title = v.findViewById<TextView>(R.id.advertisment_title)
        val advertisment_event1 = v.findViewById<MaterialCardView>(R.id.advertisment_event1)
        val advertisment_textEvent1 = v.findViewById<TextView>(R.id.advertisment_textEvent1)
        val advertisment_event2 = v.findViewById<MaterialCardView>(R.id.advertisment_event2)
        val advertisment_textEvent2 = v.findViewById<TextView>(R.id.advertisment_textEvent2)
        val advertisment_event3 = v.findViewById<MaterialCardView>(R.id.advertisment_event3)
        val advertisment_textEvent3 = v.findViewById<TextView>(R.id.advertisment_textEvent3)
        val advertisment_event4 = v.findViewById<MaterialCardView>(R.id.advertisment_event4)
        val advertisment_textEvent4 = v.findViewById<TextView>(R.id.advertisment_textEvent4)
        val advertisment_event5 = v.findViewById<MaterialCardView>(R.id.advertisment_event5)
        val advertisment_textEvent5 = v.findViewById<TextView>(R.id.advertisment_textEvent5)

        /* SET DEFAULT COLOR */
        advertisment_mainLayout.setBackgroundResource(arguments!!.getInt("cl_advertisment_background"))
        advertisment_event1.setBackgroundResource(arguments!!.getInt("cl_advertisment_background"))
        advertisment_event2.setBackgroundResource(arguments!!.getInt("cl_advertisment_background"))
        advertisment_event3.setBackgroundResource(arguments!!.getInt("cl_advertisment_background"))
        advertisment_event4.setBackgroundResource(arguments!!.getInt("cl_advertisment_background"))
        advertisment_event5.setBackgroundResource(arguments!!.getInt("cl_advertisment_background"))


        /* SET TEXT */
        advertisment_title.setText(getResources().getString(arguments!!.getInt("st_advertisment_title")))
        advertisment_textEvent1.setText(getResources().getString(arguments!!.getInt("st_advertisment_event1")))
        advertisment_textEvent2.setText(getResources().getString(arguments!!.getInt("st_advertisment_event2")))
        advertisment_textEvent3.setText(getResources().getString(arguments!!.getInt("st_advertisment_event3")))
        advertisment_textEvent4.setText(getResources().getString(arguments!!.getInt("st_advertisment_event4")))
        advertisment_textEvent5.setText(getResources().getString(arguments!!.getInt("st_advertisment_event5")))

        return v
    }
}