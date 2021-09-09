package com.example.sportincenterapp.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_admin_calendar.*
import kotlinx.android.synthetic.main.listview.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class CalendarAdminFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var communicator: Communicator
    var adapter: EventAdminAdapter? = null
    private lateinit var calendarDate: EditText
    private lateinit var listView: ListView
    private val formatDate = SimpleDateFormat("dd-MM-yyyy")
    private lateinit var todayDate: Date
    private lateinit var apiClient: ApiClient
    private lateinit var eventList: MutableList<Event>
    private lateinit var checkAllButton: View
    private lateinit var confirmDeleteButton: View
    private var allChecked: Boolean = false
    private var textColor: Int = 0
    private var backgroundColor: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        textColor = arguments!!.getInt("cl_adminCalendar_text")
        backgroundColor = arguments!!.getInt("cl_adminCalendar_background")
        val v =  inflater.inflate(R.layout.fragment_admin_calendar, container, false)
        listView = v.findViewById<ListView>(R.id.simpleListView)
        val addactivity = v.findViewById<ImageButton>(R.id.add_activity_button)
        val adminCalendar_mainLayout = v.findViewById<RelativeLayout>(R.id.adminCalendar_mainLayout)
        val adminCalendar_title = v.findViewById<TextView>(R.id.adminCalendar_title)

        adminCalendar_mainLayout.setBackgroundResource(backgroundColor)

        adminCalendar_title.setText(getResources().getString(arguments!!.getInt("st_adminCalendar_title")))

        todayDate = formatDate.parse(formatDate.format(Date()))
        calendarDate = v.findViewById<EditText>(R.id.date)
        calendarDate.setTextColor(resources.getColor(textColor))
        calendarDate.isFocusable = false
        calendarDate.isClickable = true
        calendarDate.setText(formatDate.format(todayDate))
        calendarDate.setOnClickListener{
            showDatePickerDialog()
        }
        getEvents()

        communicator  = activity as Communicator

        addactivity.setOnClickListener{
            communicator.openAddActivity()
        }

        checkAllButton = v.findViewById(R.id.check_all_button)
        checkAllButton.setOnClickListener {
            allChecked = true
            refreshAdapter()
            confirm_delete_button.visibility = View.VISIBLE
        }
        confirmDeleteButton = v.findViewById(R.id.confirm_delete_button)
        confirmDeleteButton.setOnClickListener{
            var v: View
            var et: CheckBox
            val toRemoveEvents: MutableList<Event> = mutableListOf()
            for (i in 0 until listView.count) {
                v = listView.getChildAt(i)
                et = v.findViewById<View>(R.id.checkbox_meat) as CheckBox
                if (et.isChecked) {
                    toRemoveEvents.add(eventList[i])
                }
            }
            callDeleteEvents(toRemoveEvents)
            eventList.removeAll(toRemoveEvents)
            allChecked = false
            refreshAdapter()
            confirmDeleteButton.visibility = View.GONE
            toRemoveEvents.clear()
        }

        listView.setOnItemClickListener { parent: AdapterView<*> , view: View, position: Int, id: Long ->
            communicator.openPartecipantsForEvent(eventList[position].id, textColor)
        }
        return v;
    }

    override fun onResume() {
        super.onResume()
        getEvents()
    }


    private fun refreshAdapter() {
        adapter = EventAdminAdapter(ApplicationContextProvider.getContext(),
            eventList as java.util.ArrayList<Event>, allChecked,textColor, backgroundColor)
        listView.adapter = adapter
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = context?.let {
            DatePickerDialog(
                it,
                R.style.dateDialogStyle,
                this,
                Calendar.getInstance()[Calendar.YEAR],
                Calendar.getInstance()[Calendar.MONTH],
                Calendar.getInstance()[Calendar.DAY_OF_MONTH]
            )
        }
        datePickerDialog?.datePicker?.minDate = System.currentTimeMillis() - 1000;
        datePickerDialog?.show()
    }

    private fun callDeleteEvents(eventList: List<Event>) {
        if (eventList.isNotEmpty()) {
            apiClient = ApiClient()
            activity?.let {
                apiClient.getApiServiceGateway(it).deleteEvents(eventList)
                    .enqueue(object : Callback<ResponseBody?> {
                        override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                            if (response.isSuccessful) {
                                Toast.makeText(ApplicationContextProvider.getContext(), "EVENTI ELIMINATI CON SUCCESSO!", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(ApplicationContextProvider.getContext(), "ERRORE", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                            Toast.makeText(ApplicationContextProvider.getContext(), "ERRORE", Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var date = ""
        println("month " + month)
        if (month < 9){
            if (dayOfMonth < 10)
                date = "0" + dayOfMonth.toString() + "-" + "0" + (month + 1).toString() + "-" + year.toString()
            else
                date = dayOfMonth.toString() + "-" + "0" + (month + 1).toString() + "-" + year.toString()
        }else{
            if (dayOfMonth < 10)
                date = "0" + dayOfMonth.toString() + "-" + (month + 1).toString() + "-" + year.toString()
            else
                date = dayOfMonth.toString() + "-" + (month + 1).toString() + "-" + year.toString()
        }
        calendarDate.setText(date)
        getEvents()
    }


    private fun getEvents(){
        apiClient = ApiClient()
        context?.let {
            apiClient.getApiServiceGateway(it).getEventsInDateForAdmin(calendarDate.text.toString())
                .enqueue(object : Callback<List<Event>> {
                    override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                        if (response.isSuccessful) {
                            eventList = (response.body() as MutableList<Event>?)!!
                            val orderedEventList = orderEventsByTime(eventList as MutableList<Event>)
                            if (orderedEventList.isEmpty()) {
                                view?.findViewById<TextView>(R.id.no_event)?.visibility = View.VISIBLE
                            }else{
                                view?.findViewById<TextView>(R.id.no_event)?.visibility = View.GONE
                            }
                            //Assign the adapter
                            adapter = EventAdminAdapter(ApplicationContextProvider.getContext(),
                                orderedEventList as java.util.ArrayList<Event>, allChecked, textColor, backgroundColor)
                            listView.adapter = adapter
                        }
                    }

                    override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                        Toast.makeText(ApplicationContextProvider.getContext(), "SI E' VERIFICATO UN ERRORE!", Toast.LENGTH_LONG).show()
                    }
                })
        }

    }

    private fun orderEventsByTime(eventList: MutableList<Event>) : MutableList<Event> {

        removePastEventsByTime(eventList)
        var change: Boolean = true
        while (change) {
            change = false
            for (i in 0 until eventList.size - 1) {
                var timeih = eventList[i].oraInizio.split(":")[0]
                var timei1h = eventList[i+1].oraInizio.split(":")[0]
                var timeim = eventList[i].oraInizio.split(":")[1]
                var timei1m = eventList[i+1].oraInizio.split(":")[1]
                if (timeih.toInt() == timei1h.toInt()) {
                    if (timeim.toInt() > timei1m.toInt()) {
                        var temp = eventList[i]
                        eventList[i] = eventList[i+1]
                        eventList[i+1] = temp
                        change = true
                    }
                }else if (timeih.toInt() > timei1h.toInt()) {
                    var temp = eventList[i]
                    eventList[i] = eventList[i+1]
                    eventList[i+1] = temp
                    change = true
                }
            }
        }
        return eventList
    }

    private fun removePastEventsByTime(eventList: MutableList<Event>) : MutableList<Event> {
        var toRemove : MutableList<Event> = mutableListOf()
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val todayDate = sdf.parse(sdf.format(Date()))
        for (event in eventList) {
            var eventDate = sdf.parse(event.data + " " + event.oraInizio)
            if (eventDate.before(todayDate) || eventDate.equals(todayDate)) {
                toRemove.add(event)
            }
        }
        eventList.removeAll(toRemove)
        return eventList
    }
}



class EventAdminAdapter(private val context: Context, private val arrayList: ArrayList<Event>, allChecked: Boolean, private val textColor: Int, private val backgroundColor: Int) : BaseAdapter() {
    private lateinit var activityName: TextView
    private lateinit var activityHour: TextView
    private lateinit var activityentries: TextView
    private lateinit var checkBox: CheckBox
    private var checked = allChecked

    override fun getCount(): Int {
        return arrayList.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.listview, parent, false)
        activityName = convertView.findViewById(R.id.title)
        activityHour = convertView.findViewById(R.id.hour)
        activityentries = convertView.findViewById(R.id.entries_number)

        //set text color
        activityHour.setTextColor(context.resources.getColor(textColor))
        activityentries.setTextColor(context.resources.getColor(textColor))

        checkBox = convertView.findViewById(R.id.checkbox_meat)

        checkBox.setBackgroundColor(context.resources.getColor(backgroundColor))

        activityName.text = arrayList[position].title
        activityHour.text = arrayList[position].oraInizio + " - " + arrayList[position].oraFine
        activityentries.text = activityentries.text.toString() + arrayList[position].number
        if (checked) {
            checkBox.visibility = View.VISIBLE
        }
        return convertView
    }
}
