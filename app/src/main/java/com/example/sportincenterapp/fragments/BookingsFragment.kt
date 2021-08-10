package com.example.sportincenterapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.EventAdapter
import com.example.sportincenterapp.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_bookings.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BookingsFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private val ITEM_TYPE = "BOOKING"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcv.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            apiClient = ApiClient()
            sessionManager = SessionManager(ApplicationContextProvider.getContext())
            activity?.let {
                sessionManager.fetchUserId()?.let { it1 ->
                    apiClient.getApiServiceGateway(context).getBookingsForUser(it1)
                        .enqueue(object : Callback<List<Event>> {
                            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                                if (response.isSuccessful) {
                                    val eventList = response.body()!!
                                    if (eventList.isEmpty()) {
                                        view.findViewById<TextView>(R.id.no_event).visibility = View.VISIBLE
                                    }
                                    val orderedEventList = orderEvents(eventList as MutableList<Event>)
                                    adapter = EventAdapter(orderedEventList, context, ITEM_TYPE)

                                }else
                                    Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.failed_to_load_activities), Toast.LENGTH_LONG).show()
                            }

                            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                                Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.failed_to_load_activities), Toast.LENGTH_LONG).show()
                            }

                        })
                }
            }
        }
    }

    private fun orderEvents(eventList: MutableList<Event>) : MutableList<Event> {
        println(eventList)
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        for (i in 0 until eventList.size) {
            var minDate = eventList[i]
            var minJ = i
            for (j in i + 1 until eventList.size) {
                var date1 = sdf.parse(eventList[j].data)
                var date2 = sdf.parse(eventList[i].data)
                if (date1.before(date2)) {
                    minDate = eventList[j]
                    minJ = j
                }
            }
            if (eventList[i] != minDate) {
                eventList[minJ] = eventList[i]
                eventList[i] = minDate
            }
        }
        println(eventList)
        return eventList
    }

}