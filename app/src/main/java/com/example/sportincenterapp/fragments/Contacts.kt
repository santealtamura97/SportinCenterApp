package com.example.sportincenterapp.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element


class Contacts : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*val v =  inflater.inflate(R.layout.fragment_contacts, container, false)

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

        return v*/

        if (requireArguments().getInt("cl_contacts_background") == R.color.background_primary_color) {
            return AboutPage(context)
                .isRTL(false)
                .setCustomFont(Typeface.DEFAULT) // or Typeface
                .setImage(R.drawable.logo_small)
                .setDescription(resources.getString(R.string.fr_home_firstAnswer_it))
                //DA FARE
                .addGroup(resources.getString(requireArguments().getInt("st_contacts_title")))
                .addEmail("sportingcentertorino@gmail.com", "Inviaci una mail")
                .addWebsite("https://mehdisakout.com/", "Visita il nostro sito Web")
                .addFacebook("the.medy", "Visita la nostra pagina Facebook")
                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA", "Abbiamo anche un canale Youtube")
                .addInstagram("medyo80", "Guarda la nostre foto su Instagram")
                .addGroup("Trovaci")
                .addItem(Element().setTitle(resources.getString(requireArguments().getInt("st_contacts_addressValue"))))
                .addItem(Element().setTitle(resources.getString(requireArguments().getInt("st_contacts_TelephoneText"))))
                .create()
        }else{
            return AboutPage(context, true)
                .isRTL(false)
                .setCustomFont(Typeface.DEFAULT) // or Typeface
                .setImage(R.drawable.logo_small)
                .setDescription(resources.getString(R.string.fr_home_firstAnswer_it))
                    //DA FARE
                .addGroup(resources.getString(requireArguments().getInt("st_contacts_title")))
                .addEmail("elmehdi.sakout@gmail.com", "Inviaci una mail")
                .addWebsite("https://mehdisakout.com/", "Visita il nostro sito Web")
                .addFacebook("the.medy", "Visita la nostra pagina Facebook")
                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA", "Abbiamo anche un canale Youtube")
                .addInstagram("medyo80", "Guarda la nostre foto su Instagram")
                .addGroup("Trovaci")

                .addItem(Element().setTitle(resources.getString(requireArguments().getInt("st_contacts_addressValue"))))
                .addItem(Element().setTitle(resources.getString(requireArguments().getInt("st_contacts_TelephoneText"))))
                .create()
        }

    }
}