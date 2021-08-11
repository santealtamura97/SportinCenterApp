package com.example.sportincenterapp.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
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
import kotlinx.android.synthetic.main.fragment_activities.*
import okhttp3.ResponseBody
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
                                    adapter = EventAdapter(eventList as MutableList<Event>, context, ITEM_TYPE)
                                    (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListenerEvent {
                                        override fun onClick(pos: Int, aView: View) {
                                            Toast.makeText(activity, eventList[pos].data, Toast.LENGTH_LONG).show()
                                        }
                                        override fun onBookClick(pos: Int) {
                                            bookEvent(eventList[pos].id, sessionManager.fetchUserId()!!, eventList[pos].title,eventList[pos].data,
                                                eventList[pos].oraInizio, eventList[pos].oraFine,
                                                adapter as EventAdapter, pos)
                                        }

                                        /*override fun onInfoClick(pos: Int) {
                                            val popUpClass = InfoPopUpClass()
                                            popUpClass.showInfoPopUpWindow(view)
                                        }*/
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
        sessionManager = SessionManager(ApplicationContextProvider.getContext())
        activity?.let {
            apiClient.getApiServiceGateway(it).bookEvent(userId, eventId)
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                        if (response.isSuccessful) {
                            adapter.deleteItem(pos)
                            Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.booked), Toast.LENGTH_LONG).show()
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

    /*class InfoPopUpClass {
        public fun showInfoPopUpWindow(view: View) {
            val layoutInflater = LayoutInflater.from(view.context)
            val popupView: View = layoutInflater.inflate(R.layout.activity_item, null)

            //Specify the length and width through constants
            //Specify the length and width through constants
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT

            //Make Inactive Items Outside Of PopupWindow
            //Make Inactive Items Outside Of PopupWindow
            val focusable = true

            //Create a window with our parameters
            //Create a window with our parameters
            val popupWindow = PopupWindow(popupView, width, height, focusable)

            popupView.findViewById<TextView>(R.id.txt).text = "CIAOOOOO"

            //Set the location of the window on the screen
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }
    }*/
}