package com.example.sportincenterapp.utils

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportincenterapp.fragments.*


class CalendarCollectionAdapter(fragment: CalendarCollectionFragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        if (position == 0) {
            return CalendarUserFragment()
        }else {
            return BookingsFragment()
        }
    }

}