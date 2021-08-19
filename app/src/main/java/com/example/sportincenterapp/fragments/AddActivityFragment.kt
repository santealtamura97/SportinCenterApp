package com.example.sportincenterapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.sportincenterapp.R
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import kotlinx.android.synthetic.main.fragment_add_activity.*
import java.util.*

class AddActivityFragment : Fragment() {

    private lateinit var communicator: Communicator
    var arrayList: ArrayList<MyData> = ArrayList()
    var adapter: MyAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_add_activity, container, false)

        val calendarStart = v.findViewById<ImageButton>(R.id.add_calendar_start)
        val datapickerStart = v.findViewById<DatePicker>(R.id.calendar_start)
        val textDateStart = v.findViewById<TextView>(R.id.date_start)
        val calendarEnd = v.findViewById<ImageButton>(R.id.add_calendar_end)
        val datapickerEnd = v.findViewById<DatePicker>(R.id.calendar_end)
        val textDateEnd = v.findViewById<TextView>(R.id.date_end)
        val titleLayout = v.findViewById<LinearLayout>(R.id.title_add_activity)
        val mainLayout = v.findViewById<LinearLayout>(R.id.add_activity_body)
        val dateLayout = v.findViewById<LinearLayout>(R.id.date_layout)
        val freePositionLayout = v.findViewById<LinearLayout>(R.id.free_booking_layout)
        val activityTypeLayout = v.findViewById<LinearLayout>(R.id.activity_type_layout)
        val dayOfWeekLayout = v.findViewById<LinearLayout>(R.id.day_week_layout)
        val buttonSectionLayout = v.findViewById<LinearLayout>(R.id.button_layout)
        val separator_1 = v.findViewById<LinearLayout>(R.id.separator_1)
        val separator_2 = v.findViewById<LinearLayout>(R.id.separator_2)
        val separator_3 = v.findViewById<LinearLayout>(R.id.separator_3)
        val separator_4 = v.findViewById<LinearLayout>(R.id.separator_4)
        val separator_5 = v.findViewById<LinearLayout>(R.id.separator_5)
        val editFreeBooking = v.findViewById<EditText>(R.id.edit_free_booking)
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
            titleLayout.visibility = View.GONE
            dateLayout.visibility = View.GONE
            freePositionLayout.visibility = View.GONE
            activityTypeLayout.visibility = View.GONE
            dayOfWeekLayout.visibility = View.GONE
            buttonSectionLayout.visibility = View.GONE
            separator_1.visibility = View.GONE
            separator_2.visibility = View.GONE
            separator_3.visibility = View.GONE
            separator_4.visibility = View.GONE
            separator_5.visibility = View.GONE
            datapickerStart.visibility = View.VISIBLE
        }

        calendarEnd.setOnClickListener{
            titleLayout.visibility = View.GONE
            dateLayout.visibility = View.GONE
            freePositionLayout.visibility = View.GONE
            activityTypeLayout.visibility = View.GONE
            dayOfWeekLayout.visibility = View.GONE
            buttonSectionLayout.visibility = View.GONE
            separator_1.visibility = View.GONE
            separator_2.visibility = View.GONE
            separator_3.visibility = View.GONE
            separator_4.visibility = View.GONE
            separator_5.visibility = View.GONE
            datapickerEnd.visibility = View.VISIBLE
            datapickerEnd.visibility = View.VISIBLE
        }

        mainLayout.setOnClickListener{
            titleLayout.visibility = View.VISIBLE
            dateLayout.visibility = View.VISIBLE
            freePositionLayout.visibility = View.VISIBLE
            activityTypeLayout.visibility = View.VISIBLE
            dayOfWeekLayout.visibility = View.VISIBLE
            buttonSectionLayout.visibility = View.VISIBLE
            separator_1.visibility = View.VISIBLE
            separator_2.visibility = View.VISIBLE
            separator_3.visibility = View.VISIBLE
            separator_4.visibility = View.VISIBLE
            separator_5.visibility = View.VISIBLE
            datapickerEnd.visibility = View.GONE
            datapickerStart.visibility = View.GONE
        }


        /* Calendar */
        val today = Calendar.getInstance()

        /*Check the date*/
        var day = today.get(Calendar.DAY_OF_MONTH).toString()
        var month = (today.get(Calendar.MONTH) + 1).toString()
        var year = today.get(Calendar.YEAR).toString()

        if (month.toInt() < 10) {
            month = "0" + month
        }

        if (day.toInt() < 10) {
            day = "0" + day
        }

        textDateStart.setText(day + "/" + month + "/" + year) //Today date
        textDateEnd.setText(day + "/" + month + "/" + year) //Today date

        //Init the calendar start
        datapickerStart.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->

            var sel_day = day.toString()
            var sel_month = (month + 1).toString()
            var sel_year = year

            if (month.toInt() < 10) {
                sel_month = "0" + sel_month
            }

            if (day.toInt() < 10) {
                sel_day = "0" + sel_day
            }
            textDateStart.setText(sel_day +"/"+ sel_month +"/"+ sel_year);
            titleLayout.visibility = View.VISIBLE
            dateLayout.visibility = View.VISIBLE
            freePositionLayout.visibility = View.VISIBLE
            activityTypeLayout.visibility = View.VISIBLE
            dayOfWeekLayout.visibility = View.VISIBLE
            buttonSectionLayout.visibility = View.VISIBLE
            separator_1.visibility = View.VISIBLE
            separator_2.visibility = View.VISIBLE
            separator_3.visibility = View.VISIBLE
            separator_4.visibility = View.VISIBLE
            separator_5.visibility = View.VISIBLE
            datapickerEnd.visibility = View.GONE
            datapickerStart.visibility = View.GONE
        }

        //Init the calendar end
        datapickerEnd.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->

            var sel_day = day.toString()
            var sel_month = (month + 1).toString()
            var sel_year = year

            if (month.toInt() < 10) {
                sel_month = "0" + sel_month
            }

            if (day.toInt() < 10) {
                sel_day = "0" + sel_day
            }
            textDateEnd.setText(sel_day +"/"+ sel_month +"/"+ sel_year);
            titleLayout.visibility = View.VISIBLE
            dateLayout.visibility = View.VISIBLE
            freePositionLayout.visibility = View.VISIBLE
            activityTypeLayout.visibility = View.VISIBLE
            dayOfWeekLayout.visibility = View.VISIBLE
            buttonSectionLayout.visibility = View.VISIBLE
            separator_1.visibility = View.VISIBLE
            separator_2.visibility = View.VISIBLE
            separator_3.visibility = View.VISIBLE
            separator_4.visibility = View.VISIBLE
            separator_5.visibility = View.VISIBLE
            datapickerEnd.visibility = View.GONE
            datapickerStart.visibility = View.GONE
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

    class MyData(var spinnerName: String)
}