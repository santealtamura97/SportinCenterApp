package com.example.sportincenterapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.data.models.User
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.PartecipantsEventAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventPartecipantsFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var eventId: String
    var adapter: PartecipantsEventAdapter? = null
    private lateinit var apiClient: ApiClient
    private lateinit var userList: MutableList<User>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v =  inflater.inflate(R.layout.fragment_event_partecipants, container, false)
        listView = v.findViewById(R.id.event_partecipants_list)

        eventId = arguments?.getString("eventId")!!
        getPartecipants()

        // Inflate the layout for this fragment
        return v
    }

    private fun getPartecipants() {
        apiClient = ApiClient()
        activity?.let {
            context?.let { it1 ->
                apiClient.getApiServiceGateway(it1).getUsersForEvent(eventId.toLong())
                    .enqueue(object : Callback<List<User>>{
                        override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                            if (response.isSuccessful) {
                                userList = (response.body() as MutableList<User>)
                                if (userList.isEmpty()) {
                                    view?.findViewById<TextView>(R.id.no_partecipants)?.visibility = View.VISIBLE
                                }else{
                                    view?.findViewById<TextView>(R.id.no_partecipants)?.visibility = View.GONE
                                }
                                //Assign the adapter
                                adapter = PartecipantsEventAdapter(
                                    ApplicationContextProvider.getContext(),
                                    userList as java.util.ArrayList<User>)
                                listView.adapter = adapter
                            }
                        }
                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            Toast.makeText(ApplicationContextProvider.getContext(), "SI E' VERIFICATO UN ERRORE!", Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }
    }
}


