package com.example.sportincenterapp.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportincenterapp.fragments.CalendarUserFragment
import com.example.sportincenterapp.fragments.EventFragment


class CalendarUserAdapter (fragment: CalendarUserFragment) : FragmentStateAdapter(fragment) {

    private val ARG_OBJECT = "object"

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {

        val fragment = EventFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT, position + 1)
        }
        return fragment
    }

}