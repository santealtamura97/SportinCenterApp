package com.example.sportincenterapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.R
import org.w3c.dom.Text


class UserPage : Fragment() {
    // Communicator instance
    private lateinit var communicator: Communicator

    // User info //
    val default_subscription = "Non ti è ancora stato assegnato un abbonamento!"
    val default_telephone = "+39 "
    var default_address = "Indirizzo"

    // User Features //
    var default_age = "26"
    var default_weight = "73"
    var default_tall = "173"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Exctract the frangment
        val v = inflater.inflate(R.layout.fragment_user_page, container, false)

        //Extract all the component that will be used
        //User name
        var user = v.findViewById<TextView>(R.id.user_text)
        //Telephone
        var telephone_view = v.findViewById<TextView>(R.id.user_phonenumber_view) //view
        var telephone_edit = v.findViewById<EditText>(R.id.user_phonenumber_edit) //set
        //Telephone
        var email = v.findViewById<TextView>(R.id.user_emailaddress) //view
        //Birthday
        var birth = v.findViewById<TextView>(R.id.user_birthday) //view
        //Address
        val address_view = v.findViewById<TextView>(R.id.user_address_view) //view
        val address_set = v.findViewById<TextView>(R.id.user_address_edit) //set
        //Age
        val age_view = v.findViewById<TextView>(R.id.user_age_view) //view
        val age_set = v.findViewById<EditText>(R.id.user_age_edit) //set
        //Weight
        val weight_view = v.findViewById<TextView>(R.id.user_weight_view) //view
        val weight_set = v.findViewById<EditText>(R.id.user_weight_edit)
        //Tall
        val tall_view = v.findViewById<TextView>(R.id.user_tall_view) //view
        val tall_edit = v.findViewById<EditText>(R.id.user_tall_edit) //edit
        //subscription
        val sub =  v.findViewById<TextView>(R.id.user_subscription)
        //unit misure
        var um_1 =  v.findViewById<TextView>(R.id.um_1)
        var um_2 =  v.findViewById<TextView>(R.id.um_2)
        //bmi
        val bmi = v.findViewById<TextView>(R.id.bmi_text)

        //Edit button
        val editbtn_1 = v.findViewById<Button>(R.id.button_infouser)
        val editbtn_1_save = v.findViewById<Button>(R.id.button_infouser_save)
        val editbtn_2 = v.findViewById<Button>(R.id.button_physics)
        val editbtn_2_save = v.findViewById<Button>(R.id.button_physics_save)

        //Communicator passData example (not already used)
        communicator = activity as Communicator


        user.text = arguments?.getString("username");
        email.text = arguments?.getString("email")
        um_1.text = arguments?.getString("um_1")
        um_2.text = arguments?.getString("um_2")

        /* ASSIGN DEFAULT VALUE */
        sub.text = default_subscription
        telephone_view.text = default_telephone
        telephone_edit.setText(telephone_view.text)
        address_view.text = default_address
        age_view.text = default_age
        age_set.setText(age_view.text)
        weight_view.text = default_weight
        weight_set.setText(weight_view.text)
        tall_view.text = default_tall
        tall_edit.setText(tall_view.text)

        bmi.text = calculateBMI(weight_view.text.toString(), tall_view.text.toString())

        /* LISTENERS */

        //Listener for address edit text
        telephone_edit.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                telephone_view.text = telephone_edit.text
            }
        })

        //Listener for address edit text
        address_set.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                address_view.text = address_set.text
            }
        })

        //Listener for age edit text
        age_set.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                age_view.text = age_set.text
            }
        })

        //Listener for weight edit text
        weight_set.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.length > 0 && tall_view.text.length > 0) {
                    bmi.text = calculateBMI(s.toString(), tall_view.text.toString())
                }
                weight_view.text = weight_set.text
            }
        })

        //Listener for tall edit text
        tall_edit.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.length > 0 && weight_view.text.length > 0) {
                    bmi.text = calculateBMI(weight_view.text.toString(), s.toString())
                }
                tall_view.text = tall_edit.text
            }
        })

        //Button edit/save listener

        editbtn_1.setOnClickListener {
            editbtn_1.visibility = View.GONE
            editbtn_1_save.visibility = View.VISIBLE
            address_view.visibility = View.GONE
            address_set.visibility = View.VISIBLE
            telephone_view.visibility = View.GONE
            telephone_edit.visibility = View.VISIBLE
        }

        editbtn_1_save.setOnClickListener {
            editbtn_1.visibility = View.VISIBLE
            editbtn_1_save.visibility = View.GONE
            address_view.visibility = View.VISIBLE
            address_set.visibility = View.GONE
            telephone_view.visibility = View.VISIBLE
            telephone_edit.visibility = View.GONE
        }

        editbtn_2.setOnClickListener {
            editbtn_2.visibility = View.GONE
            editbtn_2_save.visibility = View.VISIBLE
            age_view.visibility = View.GONE
            age_set.visibility = View.VISIBLE
            weight_view.visibility = View.GONE
            weight_set.visibility = View.VISIBLE
            tall_view.visibility = View.GONE
            tall_edit.visibility = View.VISIBLE
        }

        editbtn_2_save.setOnClickListener {
            editbtn_2.visibility = View.VISIBLE
            editbtn_2_save.visibility = View.GONE
            age_view.visibility = View.VISIBLE
            age_set.visibility = View.GONE
            weight_view.visibility = View.VISIBLE
            weight_set.visibility = View.GONE
            tall_view.visibility = View.VISIBLE
            tall_edit.visibility = View.GONE
        }

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