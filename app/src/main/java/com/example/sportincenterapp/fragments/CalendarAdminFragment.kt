package com.example.sportincenterapp.fragments

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
import kotlinx.android.synthetic.main.fragment_admin_calendar.*
import java.net.DatagramPacket
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdminFragment : Fragment() {

    private lateinit var communicator: Communicator
    var arrayList: ArrayList<MyData> = ArrayList()
    var adapter: MyAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v =  inflater.inflate(R.layout.fragment_admin_calendar, container, false)
        val calendar_button = v.findViewById<ImageButton>(R.id.add_calendar)
        val datapicker = v.findViewById<DatePicker>(R.id.calendar)
        val listView = v.findViewById<ListView>(R.id.simpleListView)
        val listTitle = v.findViewById<TextView>(R.id.listTitle)
        val dateSelected = v.findViewById<TextView>(R.id.dateSelected)
        val addactivity = v.findViewById<ImageButton>(R.id.add_activity_button)

        communicator  = activity as Communicator

        //Calendar pop-up
        /*calendar_button.setOnClickListener{
            //communicator.createActivity() //For use calendar as pop up
        }*/

        calendar_button.setOnClickListener {
            datapicker.visibility = View.VISIBLE
            calendar_button.visibility = View.GONE
            dateSelected.visibility = View.GONE
            addactivity.visibility = View.GONE
            listTitle.visibility = View.GONE
            listView.visibility = View.GONE
        }

        v.setOnClickListener {
            datapicker.visibility = View.GONE
            calendar_button.visibility = View.VISIBLE
            dateSelected.visibility = View.VISIBLE
            addactivity.visibility = View.VISIBLE
            listTitle.visibility = View.VISIBLE
            listView.visibility = View.VISIBLE
        }

        addactivity.setOnClickListener{
            communicator.openAddActivity()
        }


        //Fill the list with default data
        arrayList.add(MyData("NUOTO", "10:00 - 11:00"))
        arrayList.add(MyData("SALA PESI", "16:30 - 17:30"))
        arrayList.add(MyData("SALA PESI", "16:30 - 17:30"))
        arrayList.add(MyData("SALA PESI", "16:30 - 17:30"))
        arrayList.add(MyData("SALA PESI", "16:30 - 17:30"))
        arrayList.add(MyData("SALA PESI", "16:30 - 17:30"))
        arrayList.add(MyData("SALA PESI", "16:30 - 17:30"))

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

        dateSelected.setText(day + "/" + month + "/" + year) //Today date

        //Init the calendar
        datapicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)

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
            dateSelected.setText(sel_day +"/"+ sel_month +"/"+ sel_year);

            datapicker.visibility = View.GONE
            calendar_button.visibility = View.VISIBLE
            dateSelected.visibility = View.VISIBLE
            addactivity.visibility = View.VISIBLE
            listTitle.visibility = View.VISIBLE
            listView.visibility = View.VISIBLE
        }

        //Assign the adapter
        adapter = MyAdapter(ApplicationContextProvider.getContext(), arrayList)
        listView.adapter = adapter

        return v;
    }

}

class MyAdapter(private val context: Context, private val arrayList: java.util.ArrayList<MyData>) : BaseAdapter() {
    private lateinit var activityName: TextView
    private lateinit var activityHour: TextView

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
        activityName.text = arrayList[position].activityName
        activityHour.text = arrayList[position].activityHour
        return convertView
    }
}

class MyData(var activityName: String, var activityHour: String)