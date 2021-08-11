package com.example.sportincenterapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.models.Event
import kotlinx.android.synthetic.main.book_item.view.*
import kotlinx.android.synthetic.main.event_item.view.*
import kotlinx.android.synthetic.main.event_item.view.img
import kotlinx.android.synthetic.main.event_item.view.sub_txt
import kotlinx.android.synthetic.main.event_item.view.time
import kotlinx.android.synthetic.main.event_item.view.txt
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


class EventAdapter(val modelList: MutableList<Event>, val context: Context, val itemType: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val EVENT = "EVENT"
    private val BOOKING = "BOOKING"


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(modelList.get(position));
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        if (itemType == EVENT)
            return ViewHolder(layoutInflater.inflate(R.layout.event_item, parent, false))
        else if (itemType == BOOKING)
            return ViewHolder(layoutInflater.inflate(R.layout.book_item, parent, false))
        else
            return ViewHolder(layoutInflater.inflate(R.layout.event_item, parent, false))
    }

    override fun getItemCount(): Int {
        return modelList.size;
    }

    lateinit var mClickListener: Listener

    fun setOnClickListener(aClickListener: Listener) {
        mClickListener = aClickListener
    }

    interface Listener {
        fun onClick(pos: Int, aView: View)
    }

    interface ClickListenerEvent: Listener {

        fun onBookClick(pos: Int)
        //fun onInfoClick(pos: Int)
    }

    interface ClickListenerBooking: Listener{
        fun onDeleteClick(pos: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
            if (itemType == EVENT) {
                itemView.findViewById<Button>(R.id.book_button).setOnClickListener() {
                    (mClickListener as ClickListenerEvent).onBookClick(adapterPosition)
                }
                /*itemView.findViewById<Button>(R.id.info_button).setOnClickListener() {
                    mClickListener.onInfoClick(adapterPosition)
                }*/
            }else if (itemType == BOOKING) {
                itemView.findViewById<Button>(R.id.delete_button).setOnClickListener() {
                    (mClickListener as ClickListenerBooking).onDeleteClick(adapterPosition)
                }
            }
        }

        fun bind(model: Event): Unit {
            if (adapterPosition != 0 &&  itemType == BOOKING && modelList[adapterPosition - 1].data == model.data) {
                itemView.date.visibility = View.GONE
                itemView.line.visibility = View.GONE
            } else if (itemType == BOOKING) {
                val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                val myDate: Date = dateFormat.parse(model.data)
                val c = Calendar.getInstance()
                c.time = myDate
                val dayOfWeek = c[Calendar.DAY_OF_WEEK]
                val usersLocale = Locale.getDefault()
                val dfs = DateFormatSymbols(usersLocale)
                val weekdays: Array<String> = dfs.weekdays
                val stringDayOfWeek = weekdays[dayOfWeek]
                itemView.date.text = model.data + " | " + stringDayOfWeek
            }
            itemView.txt.text = model.title
            itemView.sub_txt.text = itemView.sub_txt.text.toString() + model.number.toString()
            itemView.time.text = model.oraInizio + " - " + model.oraFine
            val id = context.resources.getIdentifier(model.title.toLowerCase(), "drawable", context.packageName)
            itemView.img.setBackgroundResource(id)
        }

        override fun onClick(p0: View?) {
            (mClickListener as Listener).onClick(adapterPosition, itemView)
        }

    }

    fun deleteItem(position: Int) {
        modelList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, modelList.size)
    }

}