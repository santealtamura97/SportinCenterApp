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
import kotlinx.android.synthetic.main.fragment_faq.*

class Faq : Fragment() {

    var open1 = false;
    var open2 = false;
    var open3 = false;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_faq, container, false)

        val faq_mainLayout = v.findViewById<LinearLayout>(R.id.faq_mainLayout)
        var faq_questionText1 = v.findViewById<TextView>(R.id.faq_QuestionText1)
        var faq_answerText1 = v.findViewById<TextView>(R.id.faq_answerText1)
        var faq_questionText2 = v.findViewById<TextView>(R.id.faq_QuestionText2)
        var faq_answerText2 = v.findViewById<TextView>(R.id.faq_answerText2)
        var faq_questionText3 = v.findViewById<TextView>(R.id.faq_QuestionText3)
        var faq_answerText3 = v.findViewById<TextView>(R.id.faq_answerText3)

        faq_questionText1.setOnClickListener{

            if(open1) {
                faq_answerText1.visibility = View.GONE
                open1 = false;
            }else{
                faq_answerText1.visibility = View.VISIBLE
                open1 = true;
            }
        }

        faq_questionText2.setOnClickListener{

            if(open2) {
                faq_answerText2.visibility = View.GONE
                open2 = false;
            }else{
                faq_answerText2.visibility = View.VISIBLE
                open2 = true;
            }
        }

        faq_questionText3.setOnClickListener{

            if(open3) {
                faq_answerText3.visibility = View.GONE
                open3 = false;
            }else{
                faq_answerText3.visibility = View.VISIBLE
                open3 = true;
            }

        }

        /* DEFAULT VALUE COLOR */
        faq_mainLayout.setBackgroundResource(arguments!!.getInt("cl_faq_background"))
        faq_answerText1.setBackgroundResource(arguments!!.getInt("cl_faq_background"))
        faq_answerText2.setBackgroundResource(arguments!!.getInt("cl_faq_background"))
        faq_answerText3.setBackgroundResource(arguments!!.getInt("cl_faq_background"))
        faq_answerText1.setTextColor(getResources().getColor(arguments!!.getInt("cl_faq_text")))
        faq_answerText2.setTextColor(getResources().getColor(arguments!!.getInt("cl_faq_text")))
        faq_answerText3.setTextColor(getResources().getColor(arguments!!.getInt("cl_faq_text")))


        /* STRING VALUE */
        faq_questionText1.setText(getResources().getString(arguments!!.getInt("st_faq_question1")))
        faq_questionText2.setText(getResources().getString(arguments!!.getInt("st_faq_question2")))
        faq_questionText3.setText(getResources().getString(arguments!!.getInt("st_faq_question3")))
        faq_answerText1.setText(getResources().getString(arguments!!.getInt("st_faq_answer1")))
        faq_answerText2.setText(getResources().getString(arguments!!.getInt("st_faq_answer2")))
        faq_answerText3.setText(getResources().getString(arguments!!.getInt("st_faq_answer3")))

        return v
    }
}