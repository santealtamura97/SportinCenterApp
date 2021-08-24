package com.example.sportincenterapp.fragments


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Activity
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.google.gson.annotations.SerializedName
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup
import kotlinx.android.synthetic.main.fragment_add_activity.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddActivityFragment : Fragment(), DatePickerDialog.OnDateSetListener{

    private lateinit var communicator: Communicator
    private var activitiesNameList: ArrayList<String> = arrayListOf()
    private var activityList: ArrayList<Activity> = arrayListOf()
    private var dateType: String? = null
    private var timeType: String? = null
    private lateinit var calendarStart: EditText
    private lateinit var calendarEnd: EditText

    private lateinit var timeStart: EditText
    private lateinit var timeEnd: EditText

    private lateinit var activityTitleSelected: String
    private lateinit var activityIdSelected: String

    private var selectedDaysList: ArrayList<String> = arrayListOf()
    val daysMap = mapOf(2131296743 to "Sun", 2131296567 to "Mon", 2131296815 to "tue", 2131296839 to "Wed", 2131296784 to "Thu", 2131296481 to "Fri", 2131296662 to "Sat")

    private lateinit var apiClient: ApiClient
    var spinnerActivity: Spinner? = null
    private var daysSelected: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_add_activity, container, false)

        //communicator with main activity
        communicator  = activity as Communicator

        //calendar date RANGE
        calendarStart = v.findViewById<EditText>(R.id.date_start)
        calendarStart.isFocusable = false
        calendarStart.isClickable = true
        calendarEnd = v.findViewById<EditText>(R.id.date_end)
        calendarEnd.isFocusable = false
        calendarEnd.isClickable = true

        calendarStart.setOnClickListener{
            dateType = "START"
            showDatePickerDialog()
        }
        calendarEnd.setOnClickListener{
            dateType = "END"
            showDatePickerDialog()
        }

        //time RANGE
        timeStart = v.findViewById(R.id.time_start)
        timeStart.isFocusable = false
        timeStart.isClickable = true
        timeEnd = v.findViewById(R.id.time_end)
        timeEnd.isFocusable = false
        timeEnd.isClickable = true

        timeStart.setOnClickListener {
            timeType = "START"
            showTimePickerDialog()
        }

        timeEnd.setOnClickListener {
            timeType = "END"
            showTimePickerDialog()
        }

        //SPINNER ACTIVITIES
        spinnerActivity = v.findViewById<Spinner>(R.id.spinner_activity_add)
        getActivities()

        //DAYS of week
        val multi: MultiSelectToggleGroup = v.findViewById(R.id.group_weekdays)
        multi.setOnCheckedChangeListener { group, checkedId, isChecked ->
            selectedDaysList.clear()
            for (id in group.checkedIds) {
                daysMap[id]?.let { selectedDaysList.add(it) }
            }
        }

        //SAVE BUTTON
        val buttonSave = v.findViewById<Button>(R.id.button_save)


        val entriesNumber = v.findViewById<EditText>(R.id.edit_free_booking)

        buttonSave.setOnClickListener{
            saveEvents(calendarStart.text.toString(), calendarEnd.text.toString(),
                        timeStart.text.toString(), timeEnd.text.toString(),
                        entriesNumber.text.toString().toInt(), activityTitleSelected, selectedDaysList)
        }

        return v
    }


    private fun saveEvents(startDateString: String, endDateString: String, startTime: String, endTime: String, entriesNumber: Int, title: String, selectedDaysList: List<String>) {

        println(selectedDaysList)

        //RETRIEVES ALL DATES
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val dayFormat: DateFormat = SimpleDateFormat("EEEE")
        val startDate = sdf.parse(startDateString)
        val endDate = sdf.parse(endDateString)

        val dates: MutableList<Date> = ArrayList(25)
        val cal = Calendar.getInstance()
        cal.time = startDate
        while (cal.time.before(endDate)) {
            val currentDate = cal.time
            if (selectedDaysList.contains(dayFormat.format(currentDate).take(3))) {
                dates.add(cal.time)
            }
            cal.add(Calendar.DATE, 1)
        }

        //CREATE EVENTS LIST
        var eventList: ArrayList<EventObject> = arrayListOf()
        for (date in dates) {
            eventList.add(EventObject(title, sdf.format(date), startTime, endTime, entriesNumber.toLong(), activityIdSelected))
        }

        //CALL API
        apiClient = ApiClient()
        activity?.let {
            apiClient.getApiServiceGateway(it).addEvents(eventList)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        Toast.makeText(ApplicationContextProvider.getContext(), "EVENTI SALVATI CORRETTAMENTE!", Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(ApplicationContextProvider.getContext(), "SI E' VERIFICATO UN ERRORE DURANTE IL SALVATAGGIO!", Toast.LENGTH_LONG).show()
                    }

                })
        }
    }

    public data class EventObject(
        @SerializedName("title")
        var title: String,

        @SerializedName("data")
        var data: String,

        @SerializedName("oraInizio")
        var oraInizio: String,

        @SerializedName("oraFine")
        var oraFine: String,

        @SerializedName("number")
        var number: Long,

        @SerializedName("activityId")
        var activityId: String
        )

    private fun showDatePickerDialog() {
        val datePickerDialog = context?.let {
            DatePickerDialog(
                it,
                this,
                Calendar.getInstance()[Calendar.YEAR],
                Calendar.getInstance()[Calendar.MONTH],
                Calendar.getInstance()[Calendar.DAY_OF_MONTH]
            )
        }
        datePickerDialog?.show()
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var date = ""
        if (month < 11){
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
        if (dateType == "START")
            calendarStart.setText(date)
        else
            calendarEnd.setText(date)

    }

    private fun showTimePickerDialog() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        var time: EditText
        if (timeType == "START") {
            time = timeStart
        }else{

            time = timeEnd
        }
        mTimePicker = TimePickerDialog(context,
            { timePicker, selectedHour, selectedMinute -> time.setText("$selectedHour:$selectedMinute") },
            hour,
            minute,
            true
        ) //Yes 24 hour time

        mTimePicker.setTitle("Selezione l'orario")
        mTimePicker.show()
    }

    private fun getActivities() {
        activitiesNameList.clear()
        apiClient = ApiClient()
        activity?.let {
            apiClient.getApiServiceGateway(it).getSportActivities()
                .enqueue(object : Callback<List<Activity>>, AdapterView.OnItemSelectedListener {
                    override fun onResponse(call: Call<List<Activity>>, response: Response<List<Activity>>) {
                        activityList = response.body() as ArrayList<Activity>
                        for (activity in activityList){
                            activitiesNameList.add(activity.name)
                        }
                        var adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, activitiesNameList) }
                        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_item)
                        spinnerActivity?.adapter = adapter
                        spinnerActivity?.onItemSelectedListener = this
                    }

                    override fun onFailure(call: Call<List<Activity>>, t: Throwable) {

                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        //Toast.makeText(ApplicationContextProvider.getContext(), p0?.getItemAtPosition(p2).toString(), Toast.LENGTH_LONG).show()
                        activityTitleSelected = p0?.getItemAtPosition(p2).toString()
                        for (activity in activityList) {
                            if (activity.name == activityTitleSelected) {
                                activityIdSelected = activity.id
                            }
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                })
        }
    }


}

