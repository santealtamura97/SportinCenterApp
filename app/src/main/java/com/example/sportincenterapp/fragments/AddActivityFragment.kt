package com.example.sportincenterapp.fragments


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Activity
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.google.gson.annotations.SerializedName
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup
import com.nex3z.togglebuttongroup.button.CircularToggle
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
    private lateinit var entriesNumber: EditText

    private lateinit var timeStart: EditText
    private lateinit var timeEnd: EditText

    private lateinit var activityTitleSelected: String
    private lateinit var activityIdSelected: String

    private var selectedDaysList: MutableSet<String> = mutableSetOf()
    val daysMap = mutableMapOf<Int, String>()

    private lateinit var multi2: MultiSelectToggleGroup
    private lateinit var multi1: MultiSelectToggleGroup

    private lateinit var apiClient: ApiClient
    var spinnerActivity: Spinner? = null
    private var daysSelected: String? = null

    private val formatDate = SimpleDateFormat("dd-MM-yyyy")
    private val todayDate = formatDate.parse(formatDate.format(Date()))
    private val formatTime = SimpleDateFormat("dd-MM-yyyy HH:mm")
    private val now = formatTime.parse(formatTime.format(Date()))


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_add_activity, container, false)

        val addActivity_mainLayout = v.findViewById<FrameLayout>(R.id.addActivity_mainLayout)
        //val title_add_activity = v.findViewById<TextView>(R.id.title_add_activity)
        val addActivity_startDate = v.findViewById<TextView>(R.id.addActivity_startDate)
        val addActivity_endDate = v.findViewById<TextView>(R.id.addActivity_endDate)
        val addActivity_startTime = v.findViewById<TextView>(R.id.addActivity_startTime)
        val addActivity_endTime = v.findViewById<TextView>(R.id.addActivity_endTime)
        val addActivity_positioNumberText = v.findViewById<TextView>(R.id.addActivity_positioNumberText)
        val addActivity_activityTypeText = v.findViewById<TextView>(R.id.addActivity_activityTypeText)
        val addActivity_dayOfWeekText = v.findViewById<TextView>(R.id.addActivity_dayOfWeekText)
        val sun = v.findViewById<CircularToggle>(R.id.sun)
        val mon = v.findViewById<CircularToggle>(R.id.mon)
        val tue = v.findViewById<CircularToggle>(R.id.tue)
        val wed = v.findViewById<CircularToggle>(R.id.wed)
        val thu = v.findViewById<CircularToggle>(R.id.thu)
        val fri = v.findViewById<CircularToggle>(R.id.fri)
        val sat = v.findViewById<CircularToggle>(R.id.sat)
        val button_clear = v.findViewById<Button>(R.id.button_clear)
        val button_save = v.findViewById<Button>(R.id.button_save)

        addActivity_mainLayout.setBackgroundResource(arguments!!.getInt("cl_addActivity_background"))
        addActivity_startDate.setTextColor(getResources().getColor(arguments!!.getInt("cl_addActivity_text")))
        addActivity_endDate.setTextColor(getResources().getColor(arguments!!.getInt("cl_addActivity_text")))
        addActivity_endTime.setTextColor(getResources().getColor(arguments!!.getInt("cl_addActivity_text")))
        addActivity_startTime.setTextColor(getResources().getColor(arguments!!.getInt("cl_addActivity_text")))
        addActivity_positioNumberText.setTextColor(getResources().getColor(arguments!!.getInt("cl_addActivity_text")))
        addActivity_activityTypeText.setTextColor(getResources().getColor(arguments!!.getInt("cl_addActivity_text")))
        addActivity_dayOfWeekText.setTextColor(getResources().getColor(arguments!!.getInt("cl_addActivity_text")))
        calendarStart.setTextColor(getResources().getColor(arguments!!.getInt("cl_addActivity_text")))
        calendarEnd.setTextColor(getResources().getColor(arguments!!.getInt("cl_addActivity_text")))

        //title_add_activity.setText(getResources().getString(arguments!!.getInt("st_addActivity_title")))
        addActivity_startDate.setText(getResources().getString(arguments!!.getInt("st_addActivity_startDate")))
        addActivity_endDate.setText(getResources().getString(arguments!!.getInt("st_addActivity_endDate")))
        addActivity_startTime.setText(getResources().getString(arguments!!.getInt("st_addActivity_startTime")))
        addActivity_endTime.setText(getResources().getString(arguments!!.getInt("st_addActivity_endTime")))
        addActivity_positioNumberText.setText(getResources().getString(arguments!!.getInt("st_addActivity_positionNumber")))
        addActivity_activityTypeText.setText(getResources().getString(arguments!!.getInt("st_addActivity_activityType")))
        addActivity_dayOfWeekText.setText(getResources().getString(arguments!!.getInt("st_addActivity_dayOfWeek")))
        sun.setText(getResources().getString(arguments!!.getInt("st_addActivity_sun")))
        mon.setText(getResources().getString(arguments!!.getInt("st_addActivity_mon")))
        tue.setText(getResources().getString(arguments!!.getInt("st_addActivity_tue")))
        wed.setText(getResources().getString(arguments!!.getInt("st_addActivity_wed")))
        thu.setText(getResources().getString(arguments!!.getInt("st_addActivity_thu")))
        fri.setText(getResources().getString(arguments!!.getInt("st_addActivity_fri")))
        sat.setText(getResources().getString(arguments!!.getInt("st_addActivity_sat")))

        //communicator with main activity
        communicator  = activity as Communicator

        //calendar date RANGE
        calendarStart = v.findViewById<EditText>(R.id.date_start)
        calendarStart.isFocusable = false
        calendarStart.isClickable = true
        calendarEnd = v.findViewById<EditText>(R.id.date_end)
        calendarEnd.isFocusable = false
        calendarEnd.isClickable = true


        calendarStart.setText(formatDate.format(todayDate))
        calendarStart.setOnClickListener{
            dateType = "START"
            showDatePickerDialog()
        }
        calendarEnd.setText(formatDate.format(todayDate))
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


        timeStart.setText(formatTime.format(now).split(" ")[1])
        timeStart.setOnClickListener {
            timeType = "START"
            showTimePickerDialog()
        }

        timeEnd.setText(formatTime.format(now).split(" ")[1])
        timeEnd.setOnClickListener {
            timeType = "END"
            showTimePickerDialog()
        }

        //SPINNER ACTIVITIES
        spinnerActivity = v.findViewById<Spinner>(R.id.spinner_activity_add)
        getActivities()

        //DAYS of week
        multi1 = v.findViewById(R.id.group_weekdays)
        daysMap[multi1.getChildAt(0).id] = "Sun"
        daysMap[multi1.getChildAt(1).id] = "Mon"
        daysMap[multi1.getChildAt(2).id] = "Tue"
        daysMap[multi1.getChildAt(3).id] = "Wed"
        multi1.setOnCheckedChangeListener { group, checkedId, isChecked ->
            //selectedDaysList.clear()
            for (id in group.checkedIds) {
                daysMap[id]?.let { selectedDaysList.add(it) }
            }
        }
        multi2 = v.findViewById(R.id.group_weekdays2)
        daysMap[multi2.getChildAt(0).id] = "Thu"
        daysMap[multi2.getChildAt(1).id] = "Fri"
        daysMap[multi2.getChildAt(2).id] = "Sat"
        multi2.setOnCheckedChangeListener { group, checkedId, isChecked ->
            //selectedDaysList.clear()
            for (id in group.checkedIds) {
                daysMap[id]?.let { selectedDaysList.add(it) }
            }
        }

        //SAVE BUTTON
        val buttonSave = v.findViewById<Button>(R.id.button_save)
        val buttonClear = v.findViewById<Button>(R.id.button_clear)

        entriesNumber = v.findViewById<EditText>(R.id.edit_free_booking)
        entriesNumber.isFocusable = false
        entriesNumber.isClickable = true
        entriesNumber.setOnClickListener{
            numberPicker()
        }

        buttonSave.setOnClickListener{
            try{
                saveEvents(calendarStart.text.toString(), calendarEnd.text.toString(),
                    timeStart.text.toString(), timeEnd.text.toString(),
                    entriesNumber.text.toString().toInt(), activityTitleSelected, selectedDaysList)
            }catch(e: java.lang.Exception){
                Toast.makeText(ApplicationContextProvider.getContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
        buttonClear.setOnClickListener{
            clearAll()
        }
        return v
    }


    private fun saveEvents(startDateString: String, endDateString: String, startTime: String, endTime: String, entriesNumber: Int, title: String, selectedDaysList: MutableSet<String>) {

        println(selectedDaysList)

        //RETRIEVES ALL DATES
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val dayFormat: DateFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val startDate = sdf.parse(startDateString)
        val endDate = sdf.parse(endDateString)

        if (startDate.after(endDate) || startDate.equals(endDate)){
            throw  NotValidDateException("La data di inizio deve essere maggiore di quella finale!")
        }else if (startTime.split(":")[0].toInt() >= endTime.split(":")[0].toInt()) {
            throw  NotValidTimeException("L'orario di inizio deve essere maggiore e superiore di ALMENO 1 ora a quello di fine!")
        }else if (entriesNumber <= 0 || entriesNumber !is Int) {
            throw  NotValidEntriesException("Inserire un numero di partecipanti valido!")
        }else if (selectedDaysList.isEmpty()){
            throw NotValidDaysException("Selezionare almeno un giorno della settimana valido!")
        }

        val dates: MutableList<Date> = ArrayList(25)
        val cal = Calendar.getInstance()
        cal.time = startDate
        while (cal.time.before(endDate) || cal.time.equals(endDate)) {
            val currentDate = cal.time
            println(dayFormat.format(currentDate).take(3))
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
                        clearAll()
                        Toast.makeText(ApplicationContextProvider.getContext(), "EVENTI SALVATI CORRETTAMENTE!", Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(ApplicationContextProvider.getContext(), "SI E' VERIFICATO UN ERRORE DURANTE IL SALVATAGGIO!", Toast.LENGTH_LONG).show()
                    }

                })
        }
    }

    private fun clearAll() {
        calendarStart.setText(formatDate.format(todayDate))

        calendarEnd.setText(formatDate.format(todayDate))
        calendarEnd.setTextColor(Color.parseColor("#808080")) //grey color

        timeStart.setText(formatTime.format(now).split(" ")[1])

        timeEnd.setText(formatTime.format(now).split(" ")[1])
        timeEnd.setTextColor(Color.parseColor("#808080")) //grey color

        entriesNumber.setText("0")

        multi1.clearCheck()

        multi2.clearCheck()

    }

    class NotValidDateException(message:String): Exception(message)
    class NotValidTimeException(message:String): Exception(message)
    class NotValidEntriesException(message:String): Exception(message)
    class NotValidDaysException(message:String): Exception(message)

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
        datePickerDialog?.datePicker?.minDate = System.currentTimeMillis() - 1000;
        datePickerDialog?.show()
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
        if (dateType == "START"){
            calendarStart.setText(date)
        }else{
            calendarEnd.setTextColor(Color.parseColor("#000000"))
            calendarEnd.setText(date)
        }
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
        time.setTextColor(Color.parseColor("#000000"))
        mTimePicker = TimePickerDialog(context,
            { timePicker, selectedHour, selectedMinute -> time.setText("$selectedHour:$selectedMinute") },
            hour,
            minute,
            true
        ) //Yes 24 hour time

        mTimePicker.setTitle("Seleziona l'orario")
        mTimePicker.show()
    }

    fun numberPicker() {
        val numberPicker = NumberPicker(activity)

        numberPicker.maxValue = 360 //Maximum value to select
        numberPicker.minValue = 0 //Minimum value to select
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(numberPicker)
        builder.setTitle("Partecipanti")
        builder.setMessage("Scegli il numero massimo di partecipanti :")
        builder.setPositiveButton("OK",
            DialogInterface.OnClickListener { dialog, which ->
                entriesNumber.setText(numberPicker.value.toString())
            })
        builder.setNegativeButton("CANCEL",
            DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(context, "Non hai selezionato nulla", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            })
        builder.show()
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
                            activitiesNameList.add(activity.nameIta)
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
                            if (activity.nameIta == activityTitleSelected) {
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

