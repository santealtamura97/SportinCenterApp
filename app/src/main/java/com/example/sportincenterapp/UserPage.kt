package com.example.sportincenterapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class UserPage : Fragment() {
    // Communicator instance
    private lateinit var communicator: Communicator

    // Default values for the user page //
    //var user_name: String? = "Default"
    var userName = "Dafault"
    val default_age = "26"
    val default_weight = "73"
    val default_tall = "173"
    val default_subscription = "plus"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        //Exctract the frangment
        val v = inflater.inflate(R.layout.fragment_user_page, container, false)
        //Extract all the component that will be used
        var user = v.findViewById<TextView>(R.id.user_text)
        val age = v.findViewById<EditText>(R.id.age_text)
        val weight = v.findViewById<EditText>(R.id.weight_text)
        val tall = v.findViewById<EditText>(R.id.tall_text)
        val sub =  v.findViewById<TextView>(R.id.subscription_text)
        val bmi = v.findViewById<TextView>(R.id.bmi_text)
        val um_radioGroup = v.findViewById<RadioGroup>(R.id.radioGroup)
        val radioButton_1 = v.findViewById<RadioButton>(R.id.radioButton_1)
        val radioButton_2 = v.findViewById<RadioButton>(R.id.radioButton_2)
        val weight_um = v.findViewById<TextView>(R.id.weight_um)
        val tall_um = v.findViewById<TextView>(R.id.tall_um)




        //Communicator passData example (not already used)
        communicator = activity as Communicator



        /* ASSIGN DEFAULT VALUE */
        user.text = this.arguments?.getString("username");
        age.setText(default_age)
        weight.setText(default_weight)
        tall.setText(default_tall)
        sub.text = default_subscription
        bmi.text = calculateBMI(weight.text.toString(), tall.text.toString())

        if (radioButton_1.isChecked) {
            weight_um.text = "Kg"
            tall_um.text = "Cm"
        } else {
            weight_um.text = "Lib"
            tall_um.text = "In"
        }

        /* LISTENERS */

        //Listener for radioGroup
        um_radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton_1 ->  { weight_um.text = "Kg"; tall_um.text = "Cm" } //Case RadioButton 1
                R.id.radioButton_2 -> { weight_um.text = "Lib"; tall_um.text = "In" } //Case RadioButton 2
            }
        })

        //Listener for weight edit text
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

        //Listener for tall edit text
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

        //Listener for weight edit text
        user.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                communicator.user_name_update(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })

        return v
    }

    /*
    Function used for calculate the BMI
     */
    fun calculateBMI(weight: String, tall: String): String {
        val bmi = ((weight.toDouble()) / ( (tall.toDouble()/100) * (tall.toDouble()/100))).toString()
        return bmi
    }


}