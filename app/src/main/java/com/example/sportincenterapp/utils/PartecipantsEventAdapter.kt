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

class PartecipantsEventAdapter(private val context: Context, private val arrayList: ArrayList<User>) : BaseAdapter() {
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var userEntries: TextView

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
        userEntries = convertView.findViewById(R.id.entries_number)

        userName.text = arrayList[position].displayName
        userEmail.text = userEmail.text.toString() + arrayList[position].email
        userEntries.text = userEntries.text.toString() + arrayList[position].ingressi.toString()

        return convertView
    }
}