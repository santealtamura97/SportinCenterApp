package com.example.sportincenterapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.sportincenterapp.R

class Advertisment : Fragment() {
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Exctract the view
        val v = inflater.inflate(R.layout.fragment_advertisment, container, false)

        //Extract the component of the view
        val title_layout = v.findViewById<LinearLayout>(R.id.title_advertisment)
        val first_string = v.findViewById<TextView>(R.id.text_first_event)
        val second_string = v.findViewById<TextView>(R.id.text_second_event)
        val third_string = v.findViewById<TextView>(R.id.text_third_event)
        val fourth_string = v.findViewById<TextView>(R.id.text_fourth_event)
        val fifth_string = v.findViewById<TextView>(R.id.text_fifth_event)

        /* SET DEFAULT COLOR */
        title_layout.setBackgroundResource(arguments!!.getInt("color"))
        first_string.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        second_string.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        third_string.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        fourth_string.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        fifth_string.setTextColor(getResources().getColor(arguments!!.getInt("color")))

        return v
    }
}