package com.example.sportincenterapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.EventAdapter
import com.example.sportincenterapp.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_activities.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var eventsDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view?.findViewById<View>(R.id.book_button)?.setOnClickListener {

        }
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
                                    val eventList = response.body()!!
                                    adapter = EventAdapter(eventList, context)
                                    (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListener {
                                        override fun onClick(pos: Int, aView: View) {
                                            Toast.makeText(activity, eventList[pos].title, Toast.LENGTH_LONG).show()
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