package com.example.sportincenterapp.fragments


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.data.models.User
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.PartecipantsEventAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventPartecipantsFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var eventId: String
    var adapter: PartecipantsEventAdapter? = null
    private lateinit var apiClient: ApiClient
    private var userList: MutableList<User> = arrayListOf()
    private lateinit var presence: FloatingActionButton


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v =  inflater.inflate(R.layout.fragment_event_partecipants, container, false)
        listView = v.findViewById(R.id.event_partecipants_list)

        val checkAll: FloatingActionButton = v.findViewById(R.id.check_all_button)

        checkAll.setOnClickListener(View.OnClickListener {
            for (i in 0 until listView.count) {
                var view = listView.getChildAt(i)
                (view.findViewById<View>(R.id.presence) as CheckBox).visibility = View.VISIBLE
            }
            /*presence = v.findViewById(R.id.presence)
            presence.visibility = View.VISIBLE*/
            checkAll.visibility = View.GONE
        })


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
                                println(userList)
                                //Assign the adapter
                                adapter = PartecipantsEventAdapter(
                                    ApplicationContextProvider.getContext(),
                                    userList as java.util.ArrayList<User>)
                                listView.adapter = adapter

                                getProfileImages()
                            }
                        }
                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            Toast.makeText(ApplicationContextProvider.getContext(), "SI E' VERIFICATO UN ERRORE!", Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }
    }

    private fun getProfileImages() {
        apiClient = ApiClient()

        for (user in userList) {
            context?.let {
                apiClient.getApiServiceAuth(it).getImageProfile(user.id)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                var v: View
                                var et: CircleImageView
                                val body = response.body()?.byteStream()
                                val bitmap = BitmapFactory.decodeStream(body)
                                if (bitmap != null) {
                                    v = listView.getChildAt(userList.indexOf(user))
                                    et = v.findViewById<CircleImageView>(R.id.profileimage_container) as CircleImageView
                                    et.setImageBitmap(bitmap)
                                }
                            }
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            println("ERRORE")
                        }
                    })
            }
        }
    }
}


