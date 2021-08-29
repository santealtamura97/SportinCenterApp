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

        val generalLayout = v.findViewById<RelativeLayout>(R.id.home_generalLayout)
        val mainLayout = v.findViewById<LinearLayout>(R.id.home_mainLayout)
        val firstAnswer = v.findViewById<TextView>(R.id.home_firstAnswer)
        val firstQuestion = v.findViewById<TextView>(R.id.home_firstQuestion)
        val secondAnswer = v.findViewById<TextView>(R.id.home_secondAnswer)
        val secondQuestion = v.findViewById<TextView>(R.id.home_secondQuestion)

        //Image switcher
        val imgSwitcher = v.findViewById<ImageSwitcher>(R.id.home_imageSwitcher)
        val prev = v.findViewById<ImageButton>(R.id.home_imagePrevious)
        val next = v.findViewById<ImageButton>(R.id.home_imageFollowing)

        /* Image switcher settings */
        imgSwitcher?.setFactory {
            val imgView = ImageView(ApplicationContextProvider.getContext())
            imgView
        }
        //Set first image
        imgSwitcher?.setImageResource(nameList[index])
        //Animation
        val imgIn = AnimationUtils.loadAnimation(
                ApplicationContextProvider.getContext(), android.R.anim.slide_in_left)
        imgSwitcher?.inAnimation = imgIn

        val imgOut = AnimationUtils.loadAnimation(
                ApplicationContextProvider.getContext(), android.R.anim.slide_out_right)
        imgSwitcher?.outAnimation = imgOut

        //Previous/Follow buttons listeners
        prev.setOnClickListener {
            index = if (index - 1 >= 0) index - 1 else 2
            imgSwitcher?.setImageResource(nameList[index])
        }

        next.setOnClickListener {
            index = if (index + 1 < nameList.size) index +1 else 0
            imgSwitcher?.setImageResource(nameList[index])
        }


        /* DEFAULT COLORS */
        mainLayout.setBackgroundResource(arguments!!.getInt("cl_home_background"))
        generalLayout.setBackgroundResource(arguments!!.getInt("cl_home_background"))
        firstAnswer.setTextColor(getResources().getColor(arguments!!.getInt("cl_home_text")))
        secondAnswer.setTextColor(getResources().getColor(arguments!!.getInt("cl_home_text")))
        
        /* STRING VALUE */
        firstQuestion.setText(getResources().getString(arguments!!.getInt("st_home_firstQuestion")))
        firstAnswer.setText(getResources().getString(arguments!!.getInt("st_home_firstAnswer")))
        secondQuestion.setText(getResources().getString(arguments!!.getInt("st_home_secondQuestion")))
        secondAnswer.setText(getResources().getString(arguments!!.getInt("st_home_secondAnswer")))

        return v
    }

}