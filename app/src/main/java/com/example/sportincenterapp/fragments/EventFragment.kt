package com.example.sportincenterapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Activity
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.BookingProgressChangeEvent
import com.example.sportincenterapp.utils.EventAdapter
import com.example.sportincenterapp.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_activities.*
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    private lateinit var eventList: List<Event>
    private lateinit var eventsDate: String
    private val ITEM_TYPE = "EVENT"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //la data che rappresenta il fragment corrente
        eventsDate = arguments!!.getString("date").toString().split(" ")[0]

        rcv.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            apiClient = ApiClient()
            sessionManager = SessionManager(ApplicationContextProvider.getContext())
            activity?.let {
                if (sessionManager.fetchUserId().isNullOrEmpty()) {
                    apiClient.getApiServiceCalendar(it).getEventsInDate(eventsDate)
                        .enqueue(object : Callback<List<Event>> {
                            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                                if (response.isSuccessful) {
                                    eventList = response.body()!!
                                    if (eventList.isEmpty()) {
                                        view.findViewById<TextView>(R.id.no_event).visibility = View.VISIBLE
                                    }
                                    val orderedEventList = orderEventsByTime(eventList as MutableList<Event>)
                                    adapter = EventAdapter(orderedEventList, context, ITEM_TYPE)
                                    (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListenerEvent {
                                        override fun onBookClick(pos: Int) {
                                        }

                                        override fun onInfoClick(pos: Int) {
                                            infoEventDialog(orderedEventList[pos].title, orderedEventList[pos].activityId)
                                        }

                                        override fun onClick(pos: Int, aView: View) {
                                            Toast.makeText(activity, orderedEventList[pos].data, Toast.LENGTH_LONG).show()
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
                                        if (eventList.isEmpty()) {
                                            view.findViewById<TextView>(R.id.no_event).visibility = View.VISIBLE
                                        }
                                        val orderedEventList = orderEventsByTime(eventList as MutableList<Event>)
                                        adapter = EventAdapter(orderedEventList, context, ITEM_TYPE)
                                        (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListenerEvent {
                                            override fun onClick(pos: Int, aView: View) {
                                                Toast.makeText(activity, eventList[pos].data, Toast.LENGTH_LONG).show()
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
        strBuilder.appendln(activity?.resources?.getString(R.string.book_event_confirm))
        strBuilder.appendln(" ")
        strBuilder.appendln(activity?.resources?.getString(R.string.book_date) + date)
        strBuilder.appendln(activity?.resources?.getString(R.string.book_time) + oraInizio + " - " + oraFine)

        builder?.setMessage(strBuilder);

        builder?.setPositiveButton(R.string.book_yes) {
                dialog, which -> // Do nothing but close the dialog
            callBookEvent(userId, eventId, adapter, pos)
        }

        builder?.setNegativeButton(R.string.book_no) {
                dialog, which -> // Do nothing but close the dialog
            dialog.dismiss()
        }

        val alert = builder?.create()
        alert?.show()

    }

    private fun callBookEvent(userId : String, eventId: String, adapter: EventAdapter, pos: Int) {
        apiClient = ApiClient()
        activity?.let {
            apiClient.getApiServiceGateway(it).bookEvent(userId, eventId)
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                        if (response.isSuccessful) {
                            adapter.deleteItem(pos)
                            Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.booked), Toast.LENGTH_LONG).show()
                            val bus = EventBus.getDefault()
                            val progress = 1
                            bus.post(BookingProgressChangeEvent(progress));
                        }else{
                            Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.not_booked), Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.not_booked), Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    private fun orderEventsByTime(eventList: MutableList<Event>) : MutableList<Event> {
        println(eventList)

        var change: Boolean = true
        while (change) {
            change = false
            for (i in 0 until eventList.size - 1) {
                var timeih = eventList[i].oraInizio.split(":")[0]
                var timei1h = eventList[i+1].oraInizio.split(":")[0]
                var timeim = eventList[i].oraInizio.split(":")[1]
                var timei1m = eventList[i+1].oraInizio.split(":")[1]
                if (timeih.toInt() + timeim.toInt() > timei1h.toInt() + timei1m.toInt()) { //controllo le ore
                    var temp = eventList[i]
                    eventList[i] = eventList[i+1]
                    eventList[i+1] = temp
                    change = true
                }
            }
        }
        println(eventList)
        return eventList
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
            apiClient.getApiServiceGateway(it).getActivityFromId(activityId)
                .enqueue(object : Callback<Activity?> {
                    override fun onResponse(call: Call<Activity?>, response: Response<Activity?>) {
                        customlayout.findViewById<TextView>(R.id.sub_txt).text = response.body()?.descr
                    }
                    override fun onFailure(call: Call<Activity?>, t: Throwable) {
                        Toast.makeText(ApplicationContextProvider.getContext(), "ERRORE", Toast.LENGTH_LONG).show()
                    }
                })
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