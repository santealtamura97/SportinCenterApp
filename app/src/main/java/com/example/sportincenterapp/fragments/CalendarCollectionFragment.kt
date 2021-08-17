package com.example.sportincenterapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.sportincenterapp.R
import com.example.sportincenterapp.utils.CalendarCollectionAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator


class CalendarCollectionFragment : Fragment() {

    private lateinit var calendarCollectionAdapter: CalendarCollectionAdapter
    private lateinit var viewPager: ViewPager2
    private val tabTitles = arrayOf("CALENDARIO", "PRENOTAZIONI")
    private val tabIcons = intArrayOf(
        R.drawable.ic_baseline_calendar_today_24,
        R.drawable.ic_baseline_edit_24
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_collection_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewPager = view.findViewById(R.id.pager)
        viewPager.isUserInputEnabled = false;
        calendarCollectionAdapter = CalendarCollectionAdapter(this)
        viewPager.adapter = calendarCollectionAdapter
        val tabLayout = view.findViewById(R.id.tab_layout) as TabLayout
        tabLayout.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.adapter = calendarCollectionAdapter
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        TabLayoutMediator(tabLayout, viewPager) {
                tab, position -> tab.text = tabTitles[position];
                tab.setIcon(tabIcons[position])
        }.attach()

    }

}