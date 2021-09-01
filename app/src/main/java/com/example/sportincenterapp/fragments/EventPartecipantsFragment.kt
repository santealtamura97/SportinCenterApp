package com.example.sportincenterapp.fragments


import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.User
import com.example.sportincenterapp.data.responses.ApiResponse
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.PartecipantsEventAdapter
import com.example.sportincenterapp.utils.SessionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventPartecipantsFragment : BottomSheetDialogFragment() {

    private lateinit var listView: ListView
    private lateinit var eventId: String
    var adapter: PartecipantsEventAdapter? = null
    private lateinit var apiClient: ApiClient
    private var userList: MutableList<User> = arrayListOf()
    private var userToRemove: MutableList<User> = arrayListOf()
    private lateinit var setPresence: FloatingActionButton
    private lateinit var checkAll: FloatingActionButton
    private lateinit var sessionManager: SessionManager
    private lateinit var noPartecipants: TextView
    private lateinit var layout: RelativeLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /*dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from<View?>(bottomSheetInternal!!)
                .setState(BottomSheetBehavior.STATE_EXPANDED)

        }*/

        val v =  inflater.inflate(R.layout.fragment_event_partecipants, container, false)
        noPartecipants = v.findViewById(R.id.no_partecipants)
        layout = v.findViewById(R.id.event_partecipants_mainLayout)
        listView = v.findViewById(R.id.event_partecipants_list)
        if (arguments?.getInt("cl_event_partecipants_background") == R.color.background_primary_color_2) {
            layout.setBackgroundColor(resources.getColor(arguments!!.getInt("cl_event_partecipants_background")))
            noPartecipants.setTextColor(resources.getColor(arguments!!.getInt("cl_event_partecipants_text")!!))
            for (i in 0 until listView.count) {
                println("AOLALAL")
                var view = listView.getChildAt(i)
                (view.findViewById<TextView>(R.id.email)).setTextColor(resources.getColor(arguments!!.getInt("cl_event_partecipants_text")!!))
                (view.findViewById<TextView>(R.id.entries_number)).setTextColor(resources.getColor(arguments!!.getInt("cl_event_partecipants_text")!!))
            }
        }

        checkAll = v.findViewById(R.id.check_all_button)
        setPresence = v.findViewById(R.id.presence)

        //Session manager
        sessionManager = SessionManager(ApplicationContextProvider.getContext())
        //check if is not admin

        if (!sessionManager.fetchUserName().equals("Admin")) {
            checkAll.visibility = View.GONE
        }

        checkAll.setOnClickListener(View.OnClickListener {
            for (i in 0 until listView.count) {
                var view = listView.getChildAt(i)
                (view.findViewById<View>(R.id.presence) as CheckBox).visibility = View.VISIBLE
            }
            setPresence.visibility = View.VISIBLE
            checkAll.visibility = View.GONE
        })

        setPresence.setOnClickListener(View.OnClickListener {
            checkAll.visibility = View.VISIBLE
            setPresence.visibility = View.GONE
            val userIds : ArrayList<String> = arrayListOf()
            for (i in 0 until listView.count) {
                var view = listView.getChildAt(i)
                if ((view.findViewById<View>(R.id.presence) as CheckBox).isChecked){
                    userIds.add(userList[i].id)
                    userToRemove.add(userList[i])
                }
                (view.findViewById<View>(R.id.presence) as CheckBox).visibility = View.GONE
            }
            setEntries(userIds)
        })

        eventId = arguments?.getString("eventId")!!
        getPartecipants()

        // Inflate the layout for this fragment
        return v
    }


    private fun setEntries(userIds: List<String>) {
        apiClient = ApiClient()
        context?.let {
            apiClient.getApiServiceAuth(context!!).setEntries(userIds)
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            removeBookings(userIds)
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(ApplicationContextProvider.getContext(), "SI E' VERIFICATO UN ERRORE!", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    private fun refreshAdapter() {
        adapter = PartecipantsEventAdapter(ApplicationContextProvider.getContext(),
            userList as java.util.ArrayList<User>, sessionManager.fetchUserName())
        listView.adapter = adapter
    }

    private fun removeBookings(userIds: List<String>) {
        apiClient = ApiClient()
        context?.let {
            apiClient.getApiServiceGateway(context!!).removeBookings(userIds, eventId)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful){
                            Toast.makeText(ApplicationContextProvider.getContext(), "PRESENZE SALVATE CORRETTAMENTE", Toast.LENGTH_LONG).show()
                            userList.removeAll(userToRemove)
                            refreshAdapter()
                            getProfileImages()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(ApplicationContextProvider.getContext(), "SI E' VERIFICATO UN ERRORE!", Toast.LENGTH_LONG).show()
                    }
                })
        }
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
                                    userList as java.util.ArrayList<User>, sessionManager.fetchUserName())
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


