package com.example.sportincenterapp.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportincenterapp.fragments.*


class CalendarCollectionAdapter(fragment: CalendarCollectionFragment, color: Int) : FragmentStateAdapter(fragment) {

    val color : Int = color

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        val fragment: Fragment
        if (position == 0) {
            fragment = CalendarUserFragment()
        }else {
            fragment = BookingsFragment()
        }
        fragment.arguments = Bundle().apply {
            putInt("color", color)
        }
        return fragment
    }

}