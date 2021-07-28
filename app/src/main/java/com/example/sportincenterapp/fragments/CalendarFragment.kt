package com.example.sportincenterapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ListView;
import com.example.sportincenterapp.R
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider


class CalendarFragment : Fragment() {

    var arrayList: ArrayList<MyData> = ArrayList()
    var adapter: MyAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v =  inflater.inflate(R.layout.fragment_calendar, container, false)
        val add_button = v.findViewById<ImageButton>(R.id.add_calendar)
        val calendar = v.findViewById<LinearLayout>(R.id.calendar_body)
        val listView = v.findViewById<ListView>(R.id.simpleListView)

        add_button.setOnClickListener{
            calendar.visibility = View.VISIBLE
            add_button.visibility = View.GONE
            listView.visibility = View.GONE
        }
        v.setOnClickListener{
            calendar.visibility = View.GONE
            add_button.visibility = View.VISIBLE
            listView.visibility = View.VISIBLE
        }

        //Fill the list with default data
        arrayList.add(MyData(1, " Mashu", "987576443"))
        arrayList.add(MyData(2, " Azhar", "8787576768"))
        arrayList.add(MyData(3, " Niyaz", "65757657657"))
        arrayList.add(MyData(1, " Mashu", "987576443"))
        arrayList.add(MyData(2, " Azhar", "8787576768"))
        arrayList.add(MyData(3, " Niyaz", "65757657657"))
        arrayList.add(MyData(1, " Mashu", "987576443"))
        arrayList.add(MyData(2, " Azhar", "8787576768"))
        arrayList.add(MyData(3, " Niyaz", "65757657657"))
        arrayList.add(MyData(1, " Mashu", "987576443"))
        arrayList.add(MyData(2, " Azhar", "8787576768"))
        arrayList.add(MyData(3, " Niyaz", "65757657657"))

        //Assign the adapter
        adapter = MyAdapter(ApplicationContextProvider.getContext(), arrayList)
        listView.adapter = adapter

        return v;
    }
}

class MyAdapter(private val context: Context, private val arrayList: java.util.ArrayList<MyData>) : BaseAdapter() {
    private lateinit var serialNum: TextView
    private lateinit var name: TextView
    private lateinit var contactNum: TextView

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
        serialNum = convertView.findViewById(R.id.serialNumber)
        name = convertView.findViewById(R.id.studentName)
        contactNum = convertView.findViewById(R.id.mobileNum)
        serialNum.text = " " + arrayList[position].num
        name.text = arrayList[position].name
        contactNum.text = arrayList[position].mobileNumber
        return convertView
    }
}

class MyData(var num: Int, var name: String, var mobileNumber: String)