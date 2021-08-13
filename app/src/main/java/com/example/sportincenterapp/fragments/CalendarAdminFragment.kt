package com.example.sportincenterapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.R
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import kotlinx.android.synthetic.main.fragment_admin_calendar.view.*
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
        val calendar = v.findViewById<LinearLayout>(R.id.calendar_body)
        val listView = v.findViewById<ListView>(R.id.simpleListView)
        val listTitle = v.findViewById<TextView>(R.id.listTitle)
        val dateSelected = v.findViewById<TextView>(R.id.dateSelected)
        val datapicker = v.findViewById<DatePicker>(R.id.calendar)
        val addactivity = v.findViewById<ImageButton>(R.id.add_activity_button)

        communicator  = activity as Communicator


        addactivity.setOnClickListener{
            communicator.createActivity()
        }

        calendar_button.setOnClickListener{
            calendar.visibility = View.VISIBLE
            calendar_button.visibility = View.GONE
            listView.visibility = View.GONE
            listTitle.visibility = View.GONE
            dateSelected.visibility = View.GONE
            addactivity.visibility = View.GONE
        }
        v.setOnClickListener{
            calendar.visibility = View.GONE
            calendar_button.visibility = View.VISIBLE
            listView.visibility = View.VISIBLE
            listView.visibility = View.VISIBLE
            listTitle.visibility = View.VISIBLE
            dateSelected.visibility = View.VISIBLE
            addactivity.visibility = View.VISIBLE
        }

        //Fill the list with default data
        arrayList.add(MyData("NUOTO", "10:00 - 11:00"))
        arrayList.add(MyData("SALA PESI", "16:30 - 17:30"))
        arrayList.add(MyData("SALA PESI", "16:30 - 17:30"))
        arrayList.add(MyData("SALA PESI", "16:30 - 17:30"))
        arrayList.add(MyData("SALA PESI", "16:30 - 17:30"))
        arrayList.add(MyData("SALA PESI", "16:30 - 17:30"))
        arrayList.add(MyData("SALA PESI", "16:30 - 17:30"))

        val today = Calendar.getInstance()
        datapicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            dateSelected.setText(day.toString() +"/"+month.toString()+"/"+year.toString());
            calendar.visibility = View.GONE
            calendar_button.visibility = View.VISIBLE
            listView.visibility = View.VISIBLE
            listView.visibility = View.VISIBLE
            listTitle.visibility = View.VISIBLE
            dateSelected.visibility = View.VISIBLE
        }

        //Assign the adapter
        adapter = MyAdapter(ApplicationContextProvider.getContext(), arrayList)
        listView.adapter = adapter

        /* RETURN VALUE */
        val title = arguments?.getString("title")
        val booking = arguments?.getString("booking")
        val initial = arguments?.getString("initial")
        val final = arguments?.getString("final")

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