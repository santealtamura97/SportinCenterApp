package com.example.sportincenterapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.R

class HomeFragment : Fragment() {

    //For the SwapImage
    private var nameList = intArrayOf(R.drawable.palestra_1, R.drawable.palestra_2, R.drawable.palestra_3, R.drawable.palestra_4, R.drawable.palestra_5)
    private var index = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Extract the view
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        //Exctract component of the view
        //Strings
        val firstString = v.findViewById<TextView>(R.id.firststring_home)
        val first_text_home = v.findViewById<TextView>(R.id.first_text_home)
        val secondString = v.findViewById<TextView>(R.id.secondstring_home)
        val second_text_home = v.findViewById<TextView>(R.id.second_text_home)
        val app_name = v.findViewById<TextView>(R.id.app_name)
        //Separator
        val firstSeparator = v.findViewById<LinearLayout>(R.id.first_separator)
        //Button next/prev
        val prev = v.findViewById<ImageButton>(R.id.prev)
        val next = v.findViewById<ImageButton>(R.id.next)
        //Image switcher
        val imgSwitcher = v.findViewById<ImageSwitcher>(R.id.imageswitcher_home)

        //Image switcher settings
        imgSwitcher?.setFactory({
            val imgView = ImageView(ApplicationContextProvider.getContext())
            imgView
        })
        //Set first image
        imgSwitcher?.setImageResource(nameList[index])
        //Animation
        val imgIn = AnimationUtils.loadAnimation(
                ApplicationContextProvider.getContext(), android.R.anim.slide_in_left)
        imgSwitcher?.inAnimation = imgIn

        val imgOut = AnimationUtils.loadAnimation(
                ApplicationContextProvider.getContext(), android.R.anim.slide_out_right)
        imgSwitcher?.outAnimation = imgOut

        /* DEFAULT COLORS */
        prev.setBackgroundResource(arguments!!.getInt("color"))
        next.setBackgroundResource(arguments!!.getInt("color"))
        firstSeparator.setBackgroundResource(arguments!!.getInt("color"))
        firstString.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        secondString.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        app_name.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        
        /* STRING VALUE */
        firstString.setText(getResources().getString(arguments!!.getInt("string_home_1")))
        first_text_home.setText(getResources().getString(arguments!!.getInt("string_home_2")))
        secondString.setText(getResources().getString(arguments!!.getInt("string_home_3")))
        second_text_home.setText(getResources().getString(arguments!!.getInt("string_home_4")))

        /* LISTENER */

        //Previous/Next button listener
        prev.setOnClickListener {
            index = if (index - 1 >= 0) index - 1 else 2
            imgSwitcher?.setImageResource(nameList[index])
        }

        next.setOnClickListener {
            index = if (index + 1 < nameList.size) index +1 else 0
            imgSwitcher?.setImageResource(nameList[index])
        }

        return v
    }

}