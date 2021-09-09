package com.example.sportincenterapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.data.models.User
import java.util.ArrayList

class PartecipantsEventAdapter(private val context: Context, private val arrayList: ArrayList<User>, private val userType: String?, private val textColor: Int) : BaseAdapter() {
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var userNUmber: TextView

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
        convertView = LayoutInflater.from(context).inflate(R.layout.partecipant_item, parent, false)
        userName = convertView.findViewById(R.id.name)
        userEmail = convertView.findViewById(R.id.email)
        userNUmber = convertView.findViewById(R.id.entries_number)

        if (textColor != 0) {
            userEmail.setTextColor(context.resources.getColor(textColor))
            userNUmber.setTextColor(context.resources.getColor(textColor))
        }

        if (!userType.equals("Admin")) {
            userEmail.visibility = View.GONE
            userNUmber.visibility = View.GONE
        }

        userName.text = arrayList[position].displayName
        userEmail.text = userEmail.text.toString() + arrayList[position].email
        val number = arrayList[position].number
        if (number != null)
            userNUmber.text = userNUmber.text.toString() + number
        else
            userNUmber.text = userNUmber.text.toString()


        return convertView
    }
}