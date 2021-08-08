package com.example.sportincenterapp.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportincenterapp.fragments.CalendarUserFragment
import com.example.sportincenterapp.fragments.EventFragment


class CalendarUserAdapter (fragment: CalendarUserFragment) : FragmentStateAdapter(fragment) {

    private val ARG_OBJECT = "date"
    private val DATES_NUMBER = 5
    private var tabTitleDates = ArrayList<String>(DATES_NUMBER)

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {

        val fragment = EventFragment()
        fragment.arguments = Bundle().apply {
            putString(ARG_OBJECT, tabTitleDates[position])
        }
        return fragment
    }

    fun setTabTitleDates(tabDates : ArrayList<String>) {
        for (date in tabDates) {
            tabTitleDates.add(date)
        }
    }


}