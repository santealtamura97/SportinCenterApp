package com.example.sportincenterapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.R

class Faq : Fragment() {

    var open1 = false;
    var open2 = false;
    var open3 = false;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_faq, container, false)

        //layout
        val rlLayout = v.findViewById<LinearLayout>(R.id.title_faq_layout)
        //text
        var text_1 = v.findViewById<TextView>(R.id.first_faq_text)
        var text_1a = v.findViewById<TextView>(R.id.first_faq_body)
        var text_2 = v.findViewById<TextView>(R.id.second_faq_text)
        var text_2a = v.findViewById<TextView>(R.id.second_faq_body)
        var text_3 = v.findViewById<TextView>(R.id.third_faq_text)
        var text_3a = v.findViewById<TextView>(R.id.third_faq_body)

        text_1.setOnClickListener{

            if(open1) {
                text_1a.visibility = View.GONE
                open1 = false;
            }else{
                text_1a.visibility = View.VISIBLE
                open1 = true;
            }
        }

        text_2.setOnClickListener{

            if(open2) {
                text_2a.visibility = View.GONE
                open2 = false;
            }else{
                text_2a.visibility = View.VISIBLE
                open2 = true;
            }
        }

        text_3.setOnClickListener{

            if(open3) {
                text_3a.visibility = View.GONE
                open3 = false;
            }else{
                text_3a.visibility = View.VISIBLE
                open3 = true;
            }

        }

        /* DEFAULT VALUE COLOR */
        rlLayout.setBackgroundResource(arguments!!.getInt("color"))
        text_1.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        text_2.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        text_3.setTextColor(getResources().getColor(arguments!!.getInt("color")))

        /* STRING VALUE */
        text_1.setText(getResources().getString(arguments!!.getInt("string_faq_1")))
        text_1a.setText(getResources().getString(arguments!!.getInt("string_faq_1a")))
        text_2.setText(getResources().getString(arguments!!.getInt("string_faq_2")))
        text_2a.setText(getResources().getString(arguments!!.getInt("string_faq_2a")))
        text_3.setText(getResources().getString(arguments!!.getInt("string_faq_3")))
        text_3a.setText(getResources().getString(arguments!!.getInt("string_faq_3a")))

        return v
    }
}