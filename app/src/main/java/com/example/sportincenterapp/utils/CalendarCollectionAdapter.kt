package com.example.sportincenterapp.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportincenterapp.fragments.*


class CalendarCollectionAdapter(fragment: CalendarCollectionFragment) : FragmentStateAdapter(fragment) {

    private val ARG_OBJECT = "object"

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        if (position == 0) {
            val fragment = CalendarUserFragment()
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-P
                putInt(ARG_OBJECT, position + 1)
            }
            return fragment
        }else{
            val fragment = ActivitiesFragment() //è DI ESEMPIO, BISOGNA MODIFICARE
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-P
                putInt(ARG_OBJECT, position + 1)
            }
            return fragment
        }
    }

}