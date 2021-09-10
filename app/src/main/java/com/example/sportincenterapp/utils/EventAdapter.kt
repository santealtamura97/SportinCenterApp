package com.example.sportincenterapp.utils

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportincenterapp.R
import com.example.sportincenterapp.activities.MainActivity
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.interfaces.Communicator
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
import java.util.concurrent.TimeUnit


class EventAdapter(val modelList: MutableList<Event>, val context: Context, val itemType: String, val color: Int, val languageAvailablePlaces: Int, val languageBookButton: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val EVENT = "EVENT"
    private val BOOKING = "BOOKING"
    private lateinit var sessionManager: SessionManager
    private lateinit var communicator: Communicator

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(modelList.get(position));
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        sessionManager = SessionManager(ApplicationContextProvider.getContext())
        communicator  = context as Communicator

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
        fun onInfoClick(pos: Int)
    }

    interface ClickListenerBooking: Listener{
        fun onInfoClick(pos: Int)
        fun onQrCodeClick(pos: Int)
    }

    interface ClickListenerDeleteBooking: Listener {
        fun onChecked(pos: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
            if (itemType == EVENT) {
                if (!sessionManager.fetchUserId().isNullOrEmpty()) {
                    itemView.findViewById<Button>(R.id.book_button).setOnClickListener() {
                        (mClickListener as ClickListenerEvent).onBookClick(adapterPosition)
                    }
                }else{
                    itemView.findViewById<Button>(R.id.book_button).visibility = View.GONE
                }
                itemView.findViewById<Button>(R.id.info_button).setOnClickListener() {
                    (mClickListener as ClickListenerEvent).onInfoClick(adapterPosition)
                }

                itemView.findViewById<Button>(R.id.book_button).text = context.resources.getText(languageBookButton)
                if (languageBookButton == R.string.fr_calendarCollection_book_en) {
                    itemView.findViewById<Button>(R.id.expired).text = "EXPIRED!"
                }
            }else if (itemType == BOOKING) {

                itemView.findViewById<CardView>(R.id.card_booking).setOnClickListener(View.OnClickListener {
                    communicator.openPartecipantsForEvent(modelList[adapterPosition].id,0)
                })

                itemView.findViewById<CheckBox>(R.id.checkbox_meat).setOnClickListener() { view ->
                    modelList[adapterPosition].selected = (view as CompoundButton).isChecked
                    if(modelList[adapterPosition].selected){
                         (mClickListener as ClickListenerDeleteBooking).onChecked(adapterPosition)
                    }
                }
                itemView.findViewById<Button>(R.id.info_icon).setOnClickListener() {
                    (mClickListener as ClickListenerBooking).onInfoClick(adapterPosition)
                }
                itemView.findViewById<Button>(R.id.qr_code_icon).setOnClickListener() {
                    (mClickListener as ClickListenerBooking).onQrCodeClick(adapterPosition)
                }

                if (color == R.color.background_primary_color_2){
                    itemView.findViewById<TextView>(R.id.date).setTextColor(context.resources.getColor(R.color.background_primary_color))
                    itemView.findViewById<View>(R.id.line).setBackgroundColor(context.resources.getColor(R.color.background_primary_color))
                }
            }
            itemView.findViewById<TextView>(R.id.sub_txt).text = context.resources.getText(languageAvailablePlaces)
        }

        fun bind(model: Event): Unit {
            if ((itemType == BOOKING)) {
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
                if (adapterPosition != 0 && modelList[adapterPosition - 1].data == model.data) {
                    itemView.date.visibility = View.GONE
                    itemView.line.visibility = View.GONE
                }
                if (model.isSelectable){
                    itemView.info_icon.visibility = View.GONE
                    itemView.qr_code_icon.visibility = View.GONE
                    itemView.checkbox_meat.visibility = View.VISIBLE
                }else{
                    itemView.info_icon.visibility = View.VISIBLE
                    itemView.qr_code_icon.visibility = View.VISIBLE
                    itemView.checkbox_meat.visibility = View.GONE
                }
            }else if (itemType == EVENT) {
                val dfr = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val currentDate = Date();
                val eventDate = dfr.parse(model.data + " " + model.oraInizio + ":00")

                // Convert Date to Calendar
                // Convert Date to Calendar
                val c = Calendar.getInstance()
                c.time = eventDate

                // Perform subtraction
                c.add(Calendar.MINUTE, -15)
                val eventDateMinusFifteen = c.time

                val duration: Long = eventDateMinusFifteen.time - currentDate.time

                val diffInSeconds = ((duration / 1000) % 60)
                val diffInMinutes = ((duration / (1000 * 60)) % 60)
                val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(duration)

                var millsInFutures = (diffInMinutes * 60000) + (diffInSeconds * 1000)

                if (diffInHours.toInt() == 0 && diffInMinutes.toInt() <= 15) {
                    itemView.timer.visibility = View.VISIBLE

                    val _tv = itemView.timer as TextView
                    object : CountDownTimer(millsInFutures, 1000) {
                        // adjust the milli seconds here
                        override fun onTick(millisUntilFinished: Long) {
                            val string1_ita = "Hai a disposizone: "
                            val string2_ita = " per prenotarti"
                            val string1_eng = "You have: "
                            val string2_eng = " to book"
                            _tv.text = ""
                            var string1 = string1_ita
                            var string2 = string2_ita
                            if (languageBookButton == R.string.fr_calendarCollection_book_en) {
                                string1 = string1_eng
                                string2 = string2_eng
                            }
                            _tv.text = string1 + "" + String.format(
                                "%d min, %d sec" + string2,
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(
                                            TimeUnit.MILLISECONDS.toMinutes(
                                                millisUntilFinished
                                            )
                                        )
                            )
                        }
                        override fun onFinish() {
                            itemView.book_button.visibility = View.INVISIBLE
                            itemView.expired.visibility = View.VISIBLE
                            _tv.visibility = View.GONE
                        }
                    }.start()
                 }
            }

            //DEFAULT
            itemView.txt.text = model.title
            if (!itemView.sub_txt.text.contains(":"))
                itemView.sub_txt.text = itemView.sub_txt.text.toString() + ": "+ model.number.toString()
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