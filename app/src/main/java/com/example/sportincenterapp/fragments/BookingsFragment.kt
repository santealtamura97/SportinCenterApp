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
import com.example.sportincenterapp.utils.EventAdapter
import com.example.sportincenterapp.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_bookings.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BookingsFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var bookingList: List<Event>
    private var bookingToRemove : MutableList<Int> = mutableListOf()

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
            val checkAllButton: View = view.findViewById(R.id.check_all_button)
            checkAllButton.setOnClickListener {
                checkAllButton.visibility = View.GONE
                for(booking in bookingList) {booking.isSelectable = true}
                adapter = EventAdapter(bookingList as MutableList<Event>, context, ITEM_TYPE)
                (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListenerDeleteBooking {
                    override fun onChecked(pos: Int) {
                        bookingToRemove.add(pos)
                    }
                    override fun onClick(pos: Int, aView: View) {}
                })

                val deleteButton: View = view.findViewById(R.id.confirm_delete_button)
                deleteButton.visibility = View.VISIBLE
                deleteButton.setOnClickListener{
                    deleteButton.visibility = View.GONE
                    val cancelDelete: View = view.findViewById(R.id.cancel_delete)
                    cancelDelete.visibility = View.GONE
                    checkAllButton.visibility = View.VISIBLE
                    for (position in bookingToRemove) {
                        callDeleteBooking(sessionManager.fetchUserId()!!, bookingList[position].id)
                    }
                    for (position in bookingToRemove){
                        (bookingList as MutableList<Event>).removeAt(position)
                    }
                    for(booking in bookingList) {booking.isSelectable = false}
                    adapter = EventAdapter(bookingList as MutableList<Event>, context, ITEM_TYPE)
                    bookingToRemove.clear()
                }

                val cancelDelete: View = view.findViewById(R.id.cancel_delete)
                cancelDelete.visibility = View.VISIBLE
                cancelDelete.setOnClickListener{
                    deleteButton.visibility = View.GONE
                    cancelDelete.visibility = View.GONE
                    checkAllButton.visibility = View.VISIBLE
                    for(booking in bookingList) {booking.isSelectable = false}
                    adapter = EventAdapter(bookingList as MutableList<Event>, context, ITEM_TYPE)
                }
            }
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
                                    bookingList = orderedEventList
                                    adapter = EventAdapter(orderedEventList, context, ITEM_TYPE)
                                    (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListenerBooking {
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
                }
            }
        }
    }


    private fun callDeleteBooking(userId : String, eventId: String) {
        apiClient = ApiClient()
        sessionManager = SessionManager(ApplicationContextProvider.getContext())
        activity?.let {
            apiClient.getApiServiceGateway(it).deleteBooking(userId, eventId)
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                        if (response.isSuccessful) {
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

    /**
     * Bubble sort
     */
    private fun orderEvents(eventList: MutableList<Event>) : MutableList<Event> {
        println(eventList)

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        var change: Boolean = true
        while (change) {
            change = false
            for (i in 0 until eventList.size - 1) {
                if (sdf.parse(eventList[i].data).after(sdf.parse(eventList[i+1].data))) {
                    var temp = eventList[i]
                    eventList[i+1] = temp
                    eventList[i] = eventList[i+1]
                    change = false
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