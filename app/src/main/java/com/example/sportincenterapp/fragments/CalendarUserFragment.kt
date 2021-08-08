package com.example.sportincenterapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.example.sportincenterapp.R
import com.example.sportincenterapp.utils.CalendarUserAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*


class CalendarUserFragment : Fragment() {

    private lateinit var calendarUserAdapter: CalendarUserAdapter
    private lateinit var viewPager: ViewPager2
    private val tabTitles = arrayOf("Oggi - 07/08/2021", "Dom - 08/08/2021", "Lun - 09/08/2021", "Mar - 10/08/2021", "Mer - 11/08/2021")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calendarUserAdapter = CalendarUserAdapter(this)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = calendarUserAdapter
        val tabLayout = view.findViewById(R.id.tab_layout) as TabLayout
        TabLayoutMediator(tabLayout, viewPager) {
            tab, position ->  tab.text = tabTitles[position];
        }.attach()
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
    }

}