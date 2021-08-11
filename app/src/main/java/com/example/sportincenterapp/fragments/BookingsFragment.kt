package com.example.sportincenterapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.EventAdapter
import com.example.sportincenterapp.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_bookings.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class BookingsFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var bookingList: List<Event>
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
                                    bookingList = response.body()!!
                                    if (bookingList.isEmpty()) {
                                        view.findViewById<TextView>(R.id.no_event).visibility = View.VISIBLE
                                    }
                                    val orderedEventList = orderEvents(bookingList as MutableList<Event>)
                                    adapter = EventAdapter(orderedEventList, context, ITEM_TYPE)
                                    (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListenerBooking {
                                        override fun onDeleteClick(pos: Int) {
                                            deleteBooking(bookingList[pos].id, sessionManager.fetchUserId()!!, bookingList[pos].title,bookingList[pos].data,
                                                bookingList[pos].oraInizio, bookingList[pos].oraFine,
                                                adapter as EventAdapter, pos)
                                        }

                                        override fun onClick(pos: Int, aView: View) {
                                            Toast.makeText(activity, bookingList[pos].data, Toast.LENGTH_LONG).show()
                                        }
                                    })
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

    private fun deleteBooking(eventId: String, userId: String, eventTitle: String, date: String, oraInizio: String, oraFine: String, adapter: EventAdapter, pos: Int){
        val builder: AlertDialog.Builder? = activity?.let { AlertDialog.Builder(it) }
        builder?.setTitle(eventTitle);

        val strBuilder = StringBuilder()
        strBuilder.appendln(activity?.resources?.getString(R.string.booking_delete_confirm))
        strBuilder.appendln(" ")
        strBuilder.appendln(activity?.resources?.getString(R.string.book_date) + date)
        strBuilder.appendln(activity?.resources?.getString(R.string.book_time) + oraInizio + " - " + oraFine)

        builder?.setMessage(strBuilder);

        builder?.setPositiveButton(R.string.book_yes) {
                dialog, which -> // Do nothing but close the dialog
            callDeleteBooking(userId,eventId,adapter,pos)
        }

        builder?.setNegativeButton(R.string.book_no) {
                dialog, which -> // Do nothing but close the dialog
            dialog.dismiss()
        }

        val alert = builder?.create()
        alert?.show()
    }

    private fun callDeleteBooking(userId : String, eventId: String, adapter: EventAdapter, pos: Int) {
        apiClient = ApiClient()
        sessionManager = SessionManager(ApplicationContextProvider.getContext())
        activity?.let {
            apiClient.getApiServiceGateway(it).deleteBooking(userId, eventId)
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                        if (response.isSuccessful) {
                            adapter.deleteItem(pos)
                            Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.booking_deleted_succesfully), Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.booking_not_deleted_succesfully), Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.booking_not_deleted_succesfully), Toast.LENGTH_LONG).show()
                    }
                })
        }
    }


    private fun orderEvents(eventList: MutableList<Event>) : MutableList<Event> {
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
                }else if(date1.equals(date2)) {
                    var time1 = eventList[j].oraInizio.split(":")[0]
                    var time2 = eventList[i].oraInizio.split(":")[0]
                    if (Integer.parseInt(time1) < Integer.parseInt(time2)) {
                        minDate = eventList[j]
                        minJ = j
                    }
                }
            }
            if (eventList[i] != minDate) {
                eventList[minJ] = eventList[i]
                eventList[i] = minDate
            }
        }
        return eventList
    }

}