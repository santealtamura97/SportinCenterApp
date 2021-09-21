package com.example.sportincenterapp.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Activity
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.CalendarUtils
import com.example.sportincenterapp.utils.EventAdapter
import com.example.sportincenterapp.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_activities.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class EventFragment() : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var communicator: Communicator

    private var languageAvailablePlaces: Int = 0
    private var languageBookButton: Int = 0
    private var color: Int = 0

    private lateinit var eventList: List<Event>
    private lateinit var eventsDate: String
    private val ITEM_TYPE = "EVENT"
    private var translate: Boolean = false
    private var textColor: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_event, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        color  = arguments?.getInt("color") as Int
        languageAvailablePlaces = arguments?.getInt("languageAvailablePlaces") as Int
        languageBookButton = arguments?.getInt("languageBookButton") as Int


        if (languageBookButton == R.string.fr_calendarCollection_book_en) {
            translate = true
        }

        //la data che rappresenta il fragment corrente
        eventsDate = requireArguments().getString("date").toString().split(" ")[0]
        communicator  = activity as Communicator

        if (color == R.color.background_primary_color_2){
            view.findViewById<TextView>(R.id.no_event).setTextColor(resources.getColor(R.color.background_primary_color))
        }

        getEventsInDate(view)
    }

    private fun getEventsInDate(view: View) {
        rcv.apply {
            // set the custom adapter to the RecyclerView

            layoutManager = LinearLayoutManager(activity)

            val itemsSwipeToRefresh = view.findViewById<SwipeRefreshLayout>(R.id.itemsswipetorefresh)


            itemsSwipeToRefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context, R.color.background_primary_color_2))
            itemsSwipeToRefresh.setColorSchemeColors(Color.WHITE)

            itemsSwipeToRefresh.setOnRefreshListener {
                getEventsInDate(view)
                itemsSwipeToRefresh.isRefreshing = false
            }

            apiClient = ApiClient()
            sessionManager = SessionManager(ApplicationContextProvider.getContext())
            println(sessionManager.fetchUserId() + " " + sessionManager.fetchUserName() + " " + sessionManager.fetchAuthToken())
            activity?.let {
                if (sessionManager.fetchUserId().isNullOrEmpty()) {
                    apiClient.getApiServiceCalendar(it).getEventsInDate(eventsDate)
                        .enqueue(object : Callback<List<Event>> {
                            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                                if (response.isSuccessful) {
                                    eventList = response.body()!!
                                    val orderedEventList = CalendarUtils.orderEventsByTime(eventList as MutableList<Event>)
                                    if (orderedEventList.isEmpty()) {
                                        view.findViewById<TextView>(R.id.no_event).visibility = View.VISIBLE
                                        if (translate)
                                            view.findViewById<TextView>(R.id.no_event).text = "No event in this date!"
                                    }
                                    adapter = EventAdapter(orderedEventList, context, ITEM_TYPE, color, languageAvailablePlaces, languageBookButton)
                                    (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListenerEvent {
                                        override fun onBookClick(pos: Int) {
                                        }

                                        override fun onInfoClick(pos: Int) {
                                            infoEventDialog(orderedEventList[pos].title, orderedEventList[pos].activityId)
                                        }

                                        override fun onClick(pos: Int, aView: View) {

                                        }
                                    })
                                }else
                                    Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.failed_to_load_activities), Toast.LENGTH_LONG).show()
                            }
                            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                                Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.failed_to_load_activities), Toast.LENGTH_LONG).show()
                            }
                        })
                }else{
                    sessionManager.fetchIdAbbonamento()?.let { it1 ->
                        apiClient.getApiServiceGateway(it).getEventsForUserInDate(it1,
                            sessionManager.fetchUserId()!!, eventsDate)
                            .enqueue(object : Callback<List<Event>> {
                                override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                                    if (response.isSuccessful) {
                                        eventList = response.body()!!
                                        val orderedEventList = CalendarUtils.orderEventsByTime(eventList as MutableList<Event>)
                                        if (orderedEventList.isEmpty()) {
                                            view.findViewById<TextView>(R.id.no_event).visibility = View.VISIBLE
                                            if (translate)
                                                view.findViewById<TextView>(R.id.no_event).text = "No event in this date!"
                                        }
                                        adapter = EventAdapter(orderedEventList, context, ITEM_TYPE, color, languageAvailablePlaces, languageBookButton)
                                        (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListenerEvent {
                                            override fun onClick(pos: Int, aView: View) {
                                                communicator.openPartecipantsForEvent(eventList[pos].id, textColor)
                                            }
                                            override fun onBookClick(pos: Int) {
                                                bookEvent(eventList[pos].id, sessionManager.fetchUserId()!!, eventList[pos].title,eventList[pos].data,
                                                    eventList[pos].oraInizio, eventList[pos].oraFine,
                                                    adapter as EventAdapter, pos)
                                            }

                                            override fun onInfoClick(pos: Int) {
                                                infoEventDialog(eventList[pos].title, eventList[pos].activityId)
                                            }
                                        })
                                    }else{
                                        Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.failed_to_load_activities), Toast.LENGTH_LONG).show()
                                    }
                                }
                                override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                                    Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.failed_to_load_activities), Toast.LENGTH_LONG).show()
                                }
                            })
                    }
                }
            }
        }
    }

    private fun bookEvent(eventId: String, userId: String, eventTitle: String, date: String, oraInizio: String, oraFine: String, adapter: EventAdapter, pos: Int){
        val builder: AlertDialog.Builder? = activity?.let { AlertDialog.Builder(it) }
        builder?.setTitle(eventTitle);

        val strBuilder = StringBuilder()

        if (translate) {
            strBuilder.appendln(activity?.resources?.getString(R.string.book_event_confirm_eng))
            strBuilder.appendln(" ")
            strBuilder.appendln(activity?.resources?.getString(R.string.book_date_eng) + " " +  date)
            strBuilder.appendln(activity?.resources?.getString(R.string.book_time_eng) + " " +  oraInizio + " - " + oraFine)
            builder?.setPositiveButton(R.string.book_yes_eng) {
                    dialog, which -> // Do nothing but close the dialog
                callBookEvent(userId, eventId, adapter, pos)
            }

            builder?.setNegativeButton(R.string.book_no_eng) {
                    dialog, which -> // Do nothing but close the dialog
                dialog.dismiss()
            }

        }else{
            strBuilder.appendln(activity?.resources?.getString(R.string.book_event_confirm_ita))
            strBuilder.appendln(" ")
            strBuilder.appendln(activity?.resources?.getString(R.string.book_date_ita) + " " +  date)
            strBuilder.appendln(activity?.resources?.getString(R.string.book_time_ita) + " " +  oraInizio + " - " + oraFine)
            builder?.setPositiveButton(R.string.book_yes_ita) {
                    dialog, which -> // Do nothing but close the dialog
                callBookEvent(userId, eventId, adapter, pos)
            }

            builder?.setNegativeButton(R.string.book_no_ita) {
                    dialog, which -> // Do nothing but close the dialog
                dialog.dismiss()
            }
        }

        builder?.setMessage(strBuilder);

        val alert = builder?.create()
        alert?.show()

    }

    private fun callBookEvent(userId : String, eventId: String, adapter: EventAdapter, pos: Int) {
        apiClient = ApiClient()
        activity?.let {
            apiClient.getApiServiceGateway(it).bookEvent(userId, eventId)
                .enqueue(object : Callback<Event?> {
                    override fun onResponse(call: Call<Event?>, response: Response<Event?>) {
                        if (response.isSuccessful) {
                            adapter.deleteItem(pos)
                            if (translate)
                                Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.booked_eng), Toast.LENGTH_LONG).show()
                            else
                                Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.booked_ita), Toast.LENGTH_LONG).show()

                        }else{
                            //Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.not_booked), Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<Event?>, t: Throwable) {
                        //Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.not_booked), Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    private fun infoEventDialog(eventTitle: String, activityId: String) {
        val builder = activity?.let { android.app.AlertDialog.Builder(context, it.taskId) }
        val customlayout = layoutInflater.inflate(R.layout.info_event_dialog, null)
        customlayout.findViewById<TextView>(R.id.txt).text = eventTitle
        val id = context?.resources?.getIdentifier(eventTitle.toLowerCase(), "drawable", context?.packageName)
        if (id != null) {
            customlayout.findViewById<ImageView>(R.id.img).setBackgroundResource(id)
        }

        apiClient = ApiClient()
        activity?.let {
            if (!sessionManager.fetchUserName().isNullOrEmpty()) {
                apiClient.getApiServiceGateway(it).getActivityFromId(activityId)
                    .enqueue(object : Callback<Activity?> {
                        override fun onResponse(call: Call<Activity?>, response: Response<Activity?>) {
                            var descr = ""
                            if (translate) {
                                descr = response.body()?.descrEng.toString()
                            }else{
                                descr = response.body()?.descrIta.toString()
                            }
                            customlayout.findViewById<TextView>(R.id.sub_txt).text = descr
                        }
                        override fun onFailure(call: Call<Activity?>, t: Throwable) {
                            Toast.makeText(ApplicationContextProvider.getContext(), "ERRORE", Toast.LENGTH_LONG).show()
                        }
                    })
            }else{
                apiClient.getApiServiceActivity(it).getActivityFromIdNoAuth(activityId)
                    .enqueue(object : Callback<Activity?> {
                        override fun onResponse(call: Call<Activity?>, response: Response<Activity?>) {
                            var descr = ""
                            if (translate) {
                                descr = response.body()?.descrEng.toString()
                            }else{
                                descr = response.body()?.descrIta.toString()
                            }
                            customlayout.findViewById<TextView>(R.id.sub_txt).text = descr
                        }
                        override fun onFailure(call: Call<Activity?>, t: Throwable) {
                            Toast.makeText(ApplicationContextProvider.getContext(), "ERRORE", Toast.LENGTH_LONG).show()
                        }
                    })
            }

        }



        builder?.setView(customlayout)
        builder?.setPositiveButton("OK") {
                dialog, which ->
            dialog.dismiss()
        }

        val alert = builder?.create()
        alert?.show()
    }

}