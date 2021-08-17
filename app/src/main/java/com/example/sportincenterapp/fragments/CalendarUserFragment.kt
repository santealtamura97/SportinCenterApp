package com.example.sportincenterapp.fragments

import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.User
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.BookingProgressChangeEvent
import com.example.sportincenterapp.utils.CalendarUserAdapter
import com.example.sportincenterapp.utils.SessionManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    var bus = EventBus.getDefault()
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bus.register(this)
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
        setUserEntries()

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
            tabDates.add(dateFormat.format(day) + " | " + weekdays[intDay])
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            intDay = calendar.get(Calendar.DAY_OF_WEEK)
            day = calendar.time
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: BookingProgressChangeEvent) {
        var progressState = (view?.findViewById<TextView>(R.id.progress_bar_number)?.text).toString()
        if (event.progress != null) {
            var newProgressState = progressState.toInt() - event.progress!!
            view?.findViewById<TextView>(R.id.progress_bar_number)?.text = newProgressState.toString()
            view?.findViewById<CircularProgressBar>(R.id.circularProgressBar)?.progress = newProgressState.toFloat()
        }
    }

    private fun setUserEntries() {
        apiClient = ApiClient()
        sessionManager = SessionManager(ApplicationContextProvider.getContext())
        context?.let {
            sessionManager.fetchUserId()?.let { it1 ->
                apiClient.getApiServiceAuth(it).getMyUserInfo(it1)
                    .enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            println(response.body()!!.ingressi.toString().toFloat())
                            view?.findViewById<CircularProgressBar>(R.id.circularProgressBar)?.progress = response.body()!!.ingressi.toString().toFloat()
                            view?.findViewById<TextView>(R.id.progress_bar_number)?.text = response.body()!!.ingressi.toString()

                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                        }

                    })
            }
        }
    }

}