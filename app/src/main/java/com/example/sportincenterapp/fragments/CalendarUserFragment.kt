package com.example.sportincenterapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.sportincenterapp.R
import com.example.sportincenterapp.utils.CalendarUserAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalendarUserFragment : Fragment() {

    private lateinit var calendarUserAdapter: CalendarUserAdapter
    private lateinit var viewPager: ViewPager2
    private val DATES_NUMBER = 5
    private val tabDates = ArrayList<String>(DATES_NUMBER)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTabDates()
        calendarUserAdapter = CalendarUserAdapter(this)
        calendarUserAdapter.setTabTitleDates(tabDates)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = calendarUserAdapter
        val tabLayout = view.findViewById(R.id.tab_layout) as TabLayout
        TabLayoutMediator(tabLayout, viewPager) {
            tab, position ->  tab.text = tabDates[position];
        }.attach()
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
    }

    /**
     * Inserisce 5 date a partire dalla data corrente.
     * L'utente può prenotarsi agli eventi di 5 giorni da oggi.
     *
     */
    private fun setTabDates() {
        val usersLocale = Locale.getDefault()
        val dfs = DateFormatSymbols(usersLocale)
        val weekdays: Array<String> = dfs.getWeekdays()
        val calendar = Calendar.getInstance()
        var day = calendar.time
        var intDay: Int = calendar.get(Calendar.DAY_OF_WEEK)
        val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")

        for (i in 0 until DATES_NUMBER) {
            tabDates.add(dateFormat.format(day) + " " + weekdays[intDay])
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            intDay = calendar.get(Calendar.DAY_OF_WEEK)
            day = calendar.time
        }
    }
}