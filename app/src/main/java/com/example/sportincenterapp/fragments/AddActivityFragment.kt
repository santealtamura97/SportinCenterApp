package com.example.sportincenterapp.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.R
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import java.util.*


class AddActivityFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var communicator: Communicator
    var arrayList: ArrayList<MyData> = ArrayList()
    var adapter: MyAdapter? = null
    var textDateStart: TextView? = null
    var textDateEnd: TextView? = null
    var dateType: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_add_activity, container, false)

        val calendarStart = v.findViewById<ImageButton>(R.id.add_calendar_start)
        textDateStart = v.findViewById(R.id.date_start)
        val calendarEnd = v.findViewById<ImageButton>(R.id.add_calendar_end)
        textDateEnd = v.findViewById(R.id.date_end)

        val mainLayout = v.findViewById<LinearLayout>(R.id.add_activity_body)

        val spinnerActivity = v.findViewById<Spinner>(R.id.spinner_activity_add)
        val buttonSave = v.findViewById<Button>(R.id.button_save)
        val check_1 = v.findViewById<CheckBox>(R.id.check_1)
        val check_2 = v.findViewById<CheckBox>(R.id.check_2)
        val check_3 = v.findViewById<CheckBox>(R.id.check_3)
        val check_4 = v.findViewById<CheckBox>(R.id.check_4)
        val check_5= v.findViewById<CheckBox>(R.id.check_5)
        val check_6 = v.findViewById<CheckBox>(R.id.check_6)
        val check_7 = v.findViewById<CheckBox>(R.id.check_7)

        communicator  = activity as Communicator

        check_1.setOnClickListener{
            if (check_1.isChecked) {
                buttonSave.visibility = View.GONE
            } else {
                buttonSave.visibility = View.VISIBLE
            }
        }

        arrayList.add(MyData("prova1"))
        arrayList.add(MyData("prova2"))
        arrayList.add(MyData("prova3"))

        //Assign the adapter
        adapter = MyAdapter(ApplicationContextProvider.getContext(), arrayList)
        spinnerActivity.adapter = adapter

        calendarStart.setOnClickListener{
            dateType = "START"
            showDatePickerDialog()
        }

        calendarEnd.setOnClickListener{
            dateType = "END"
            showDatePickerDialog()
        }

        return v
    }

    class MyAdapter(private val context: Context, private val arrayList: java.util.ArrayList<MyData>) : BaseAdapter() {
        private lateinit var activityName: TextView

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
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner, parent, false)
            val textSpinner = convertView.findViewById<TextView>(R.id.text_spinner)
            textSpinner.text = arrayList[position].spinnerName

            return convertView
        }
    }

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

    class MyData(var spinnerName: String)

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var date = ""
        if (month < 11){
            date = dayOfMonth.toString() + "-" + "0" + (month + 1).toString() + "-" + year.toString()
        }else{
            date = dayOfMonth.toString() + "-" + (month + 1).toString() + "-" + year.toString()
        }
        if (dateType == "START")
            textDateStart?.text = date
        else
            textDateEnd?.text = date

    }
}