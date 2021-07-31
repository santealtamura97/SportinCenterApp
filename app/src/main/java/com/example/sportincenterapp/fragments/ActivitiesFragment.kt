package com.example.sportincenterapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Activity
import com.example.sportincenterapp.utils.ActivityAdapter
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_activities.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivitiesFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activities, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        rcv.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            apiClient = ApiClient()
            sessionManager = SessionManager(ApplicationContextProvider.getContext())
            activity?.let {
                apiClient.getApiServiceGateway(it).getSportActivities()
                    .enqueue(object : Callback<List<Activity>> {
                        override fun onResponse(call: Call<List<Activity>>, response: Response<List<Activity>>) {
                            if (response.isSuccessful) {
                                val activityList = response.body()!!
                                adapter = ActivityAdapter(activityList, context)
                                (adapter as ActivityAdapter).setOnClickListener(object : ActivityAdapter.ClickListener {
                                    override fun onClick(pos: Int, aView: View) {
                                        Toast.makeText(activity, activityList[pos].name, Toast.LENGTH_LONG).show()
                                    }
                                })
                            }else{
                                Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.failed_to_load_activities), Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onFailure(call: Call<List<Activity>>, t: Throwable) {
                            Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.failed_to_load_activities), Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }
    }
}