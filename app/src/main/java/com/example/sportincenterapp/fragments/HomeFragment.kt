package com.example.sportincenterapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.R

class HomeFragment : Fragment() {

    private var nameList = intArrayOf(R.drawable.palestra_1, R.drawable.palestra_2, R.drawable.palestra_3, R.drawable.palestra_4, R.drawable.palestra_5)
    private var index = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        val imgSwitcher = v.findViewById<ImageSwitcher>(R.id.imageswitcher_home)
        imgSwitcher?.setFactory({
            val imgView = ImageView(ApplicationContextProvider.getContext())
            imgView
        })

        imgSwitcher?.setImageResource(nameList[index])

        val imgIn = AnimationUtils.loadAnimation(
                ApplicationContextProvider.getContext(), android.R.anim.slide_in_left)
        imgSwitcher?.inAnimation = imgIn

        val imgOut = AnimationUtils.loadAnimation(
                ApplicationContextProvider.getContext(), android.R.anim.slide_out_right)
        imgSwitcher?.outAnimation = imgOut

        // previous button functionality
       val prev = v.findViewById<ImageButton>(R.id.prev)
        prev.setOnClickListener {
            index = if (index - 1 >= 0) index - 1 else 2
            imgSwitcher?.setImageResource(nameList[index])
        }
        // next button functionality
        val next = v.findViewById<ImageButton>(R.id.next)
        next.setOnClickListener {
            index = if (index + 1 < nameList.size) index +1 else 0
            imgSwitcher?.setImageResource(nameList[index])
        }

        return v
    }

}