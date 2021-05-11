package com.example.sportincenterapp

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.text.TextWatcher
import androidx.core.widget.doOnTextChanged
import org.w3c.dom.Text

class UserPage : Fragment() {
    // Default values //
    val default_user = "Andrea Forino"
    val default_age = "26"
    val default_weight = "73"
    val default_tall = "173"
    val default_subscription= "plus"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_user_page, container, false)
        val user = v.findViewById<EditText>(R.id.user_text)
        val age = v.findViewById<EditText>(R.id.age_text)
        val weight = v.findViewById<EditText>(R.id.weight_text)
        val tall = v.findViewById<EditText>(R.id.tall_text)
        val sub =  v.findViewById<TextView>(R.id.subscription_text)
        val bmi = v.findViewById<TextView>(R.id.bmi_text)

        user.setText(default_user)
        age.setText(default_age)
        weight.setText(default_weight)
        tall.setText(default_tall)
        sub.text = default_subscription
        bmi.text = calculateBMI(weight.text.toString(), tall.text.toString())

        weight.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.length > 0 && tall.text.length > 0) {
                    bmi.text = calculateBMI(s.toString(), tall.text.toString())
                }
            }
        })

        tall.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.length > 0 && weight.text.length > 0) {
                    bmi.text = calculateBMI(weight.text.toString(), s.toString())
                }
            }
        })

        return v
    }

    fun calculateBMI(weight: String, tall: String): String {
        val bmi = ((weight.toDouble()) / ( (tall.toDouble()/100) * (tall.toDouble()/100))).toString()
        return bmi
    }


}