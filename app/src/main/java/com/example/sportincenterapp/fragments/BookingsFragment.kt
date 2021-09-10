package com.example.sportincenterapp.fragments

import android.content.Context.WINDOW_SERVICE
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportincenterapp.R
import com.example.sportincenterapp.activities.MainActivity
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Activity
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.EventAdapter
import com.example.sportincenterapp.utils.SessionManager
import com.google.zxing.WriterException
import kotlinx.android.synthetic.main.fragment_bookings.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BookingsFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var bookingList: List<Event>
    private var bookingToRemove : MutableList<Event> = mutableListOf()
    private var translate: Boolean = false

    private val ITEM_TYPE = "BOOKING"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //Session manager
        return inflater.inflate(R.layout.fragment_bookings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(ApplicationContextProvider.getContext())

        val color : Int = arguments?.getInt("color") as Int
        val languageAvailablePlaces: Int = arguments?.getInt("languageAvailablePlaces") as Int
        val languageBookButton: Int = arguments?.getInt("languageBookButton") as Int
        if (languageBookButton == R.string.fr_calendarCollection_book_en) {
            translate = true
        }
        val fragment_bookings_mainLayout = view.findViewById<CoordinatorLayout>(R.id.fragment_bookings_mainLayout)
        fragment_bookings_mainLayout.setBackgroundResource(color)

        rcv.apply {
            val checkAllButton: View = view.findViewById(R.id.check_all_button)
            if (sessionManager.fetchUserName().isNullOrEmpty())
                checkAllButton.visibility = View.GONE
            checkAllButton.setOnClickListener {
                checkAllButton.visibility = View.GONE
                for(booking in bookingList) {booking.isSelectable = true}
                adapter = EventAdapter(bookingList as MutableList<Event>, context, ITEM_TYPE, color, languageAvailablePlaces, languageBookButton)
                (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListenerDeleteBooking {
                    override fun onChecked(pos: Int) {
                        bookingToRemove.add(bookingList[pos])
                    }
                    override fun onClick(pos: Int, aView: View) {

                    }
                })

                val deleteButton: View = view.findViewById(R.id.confirm_delete_button)
                deleteButton.visibility = View.VISIBLE
                deleteButton.setOnClickListener{
                    deleteButton.visibility = View.GONE
                    val cancelDelete: View = view.findViewById(R.id.cancel_delete)
                    cancelDelete.visibility = View.GONE
                    checkAllButton.visibility = View.VISIBLE
                    for (booking in bookingToRemove)
                        callDeleteBooking(sessionManager.fetchUserId()!!, booking.id)
                    (bookingList as MutableList<Event>).removeAll(bookingToRemove)
                    for(booking in bookingList) {booking.isSelectable = false}
                    bookingToRemove.clear()
                    adapter = EventAdapter(bookingList as MutableList<Event>, context, ITEM_TYPE, color, languageAvailablePlaces, languageBookButton)
                    (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListenerBooking {
                        override fun onInfoClick(pos: Int) {
                            infoEventDialog(bookingList[pos].title, bookingList[pos].activityId)
                        }

                        override fun onQrCodeClick(pos: Int) {
                            qrCodeEventDialog(bookingList[pos].title, bookingList[pos].id, bookingList[pos].data, bookingList[pos].oraInizio,
                                bookingList[pos].oraFine, sessionManager.fetchUserId()!!)
                        }
                        override fun onClick(pos: Int, aView: View) {
                        }
                    })
                }

                val cancelDelete: View = view.findViewById(R.id.cancel_delete)
                cancelDelete.visibility = View.VISIBLE
                cancelDelete.setOnClickListener{
                    deleteButton.visibility = View.GONE
                    cancelDelete.visibility = View.GONE
                    checkAllButton.visibility = View.VISIBLE
                    for(booking in bookingList) {booking.isSelectable = false}
                    bookingToRemove.clear()
                    adapter = EventAdapter(bookingList as MutableList<Event>, context, ITEM_TYPE, color, languageAvailablePlaces, languageBookButton)
                    (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListenerBooking {
                        override fun onInfoClick(pos: Int) {
                            infoEventDialog(bookingList[pos].title, bookingList[pos].activityId)
                        }

                        override fun onQrCodeClick(pos: Int) {
                            qrCodeEventDialog(bookingList[pos].title, bookingList[pos].id, bookingList[pos].data, bookingList[pos].oraInizio,
                                bookingList[pos].oraFine, sessionManager.fetchUserId()!!)
                        }

                        override fun onClick(pos: Int, aView: View) {

                        }
                    })
                }
            }
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            apiClient = ApiClient()
            sessionManager = SessionManager(ApplicationContextProvider.getContext())
            activity?.let {
                if (!sessionManager.fetchUserId().isNullOrEmpty()) {
                    sessionManager.fetchUserId()?.let { it1 ->
                        apiClient.getApiServiceGateway(context).getBookingsForUser(it1)
                            .enqueue(object : Callback<List<Event>> {
                                override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                                    if (response.isSuccessful) {
                                        bookingList = response.body()!!
                                        val orderedEventList = orderEvents(bookingList as MutableList<Event>)
                                        if (orderedEventList.isEmpty()) {
                                            view.findViewById<TextView>(R.id.no_event).visibility = View.VISIBLE
                                        }
                                        bookingList = orderedEventList
                                        adapter = EventAdapter(orderedEventList, context, ITEM_TYPE, color, languageAvailablePlaces, languageBookButton)
                                        (adapter as EventAdapter).setOnClickListener(object : EventAdapter.ClickListenerBooking {
                                            override fun onInfoClick(pos: Int) {
                                                infoEventDialog(orderedEventList[pos].title, orderedEventList[pos].activityId)
                                            }

                                            override fun onQrCodeClick(pos: Int) {
                                                qrCodeEventDialog(orderedEventList[pos].title, orderedEventList[pos].id, orderedEventList[pos].data, orderedEventList[pos].oraInizio,
                                                    orderedEventList[pos].oraFine, sessionManager.fetchUserId()!!)
                                            }

                                            override fun onClick(pos: Int, aView: View) {

                                            }
                                        })
                                    }else
                                        Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.failed_to_load_activities), Toast.LENGTH_LONG).show()
                                }

                                override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                                    Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.failed_to_load_activities), Toast.LENGTH_LONG).show()
                                }
                            })
                    }
                }
            }
        }
    }

    private fun callDeleteBooking(userId : String, eventId: String) {
        apiClient = ApiClient()
        sessionManager = SessionManager(ApplicationContextProvider.getContext())
        activity?.let {
            apiClient.getApiServiceGateway(it).deleteBooking(userId, eventId)
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                        if (response.isSuccessful) {
                            if (translate)
                                Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.booking_deleted_succesfully_eng), Toast.LENGTH_LONG).show()
                            else
                                Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.booking_deleted_succesfully_ita), Toast.LENGTH_LONG).show()


                        }else{
                            //Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.booking_not_deleted_succesfully), Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        //Toast.makeText(ApplicationContextProvider.getContext(), resources.getString(R.string.booking_not_deleted_succesfully), Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    /**
     * Bubble sort
     */
    private fun orderEvents(eventList: MutableList<Event>) : MutableList<Event> {

        removePastEvents(eventList)

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        var change: Boolean = true
        while (change) {
            change = false
            for (i in 0 until eventList.size - 1) {
                if (sdf.parse(eventList[i].data).after(sdf.parse(eventList[i+1].data))) { //controllo le date
                    var temp = eventList[i]
                    eventList[i] = eventList[i+1]
                    eventList[i+1] = temp
                    change = true
                }else if(sdf.parse(eventList[i].data).equals(sdf.parse(eventList[i+1].data))) {
                    var timeih = eventList[i].oraInizio.split(":")[0]
                    var timei1h = eventList[i+1].oraInizio.split(":")[0]
                    var timeim = eventList[i].oraInizio.split(":")[1]
                    var timei1m = eventList[i+1].oraInizio.split(":")[1]
                    if (timeih.toInt() == timei1h.toInt()) {
                        if (timeim.toInt() > timei1m.toInt()) {
                            var temp = eventList[i]
                            eventList[i] = eventList[i+1]
                            eventList[i+1] = temp
                            change = true
                        }
                    }else if (timeih.toInt() > timei1h.toInt()) {
                        var temp = eventList[i]
                        eventList[i] = eventList[i+1]
                        eventList[i+1] = temp
                        change = true
                    }
                }
            }
        }
        return eventList
    }

    private fun orderEventsByTime(eventList: MutableList<Event>) : MutableList<Event> {

        var change: Boolean = true
        while (change) {
            change = false
            for (i in 0 until eventList.size - 1) {
                var timeih = eventList[i].oraInizio.split(":")[0]
                var timei1h = eventList[i+1].oraInizio.split(":")[0]
                var timeim = eventList[i].oraInizio.split(":")[1]
                var timei1m = eventList[i+1].oraInizio.split(":")[1]
                if (timeih.toInt() == timei1h.toInt()) {
                    if (timeim.toInt() > timei1m.toInt()) {
                        var temp = eventList[i]
                        eventList[i] = eventList[i+1]
                        eventList[i+1] = temp
                        change = true
                    }
                }else if (timeih.toInt() > timei1h.toInt()) {
                    var temp = eventList[i]
                    eventList[i] = eventList[i+1]
                    eventList[i+1] = temp
                    change = true
                }
            }
        }
        return eventList
    }

    private fun removePastEvents(eventList: MutableList<Event>) : MutableList<Event> {
        var toRemove : MutableList<Event> = mutableListOf()
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val todayDate = sdf.parse(sdf.format(Date()))
        for (event in eventList) {
            var eventDate = sdf.parse(event.data)
            if (eventDate.before(todayDate)) {
                toRemove.add(event)
            }
        }
        eventList.removeAll(toRemove)
        return eventList
    }

    private fun infoEventDialog(eventTitle: String, activityId: String) {
        val builder = activity?.let { android.app.AlertDialog.Builder(context, it.taskId) }
        val customlayout = layoutInflater.inflate(R.layout.info_event_dialog, null)
        customlayout.findViewById<TextView>(R.id.txt).text = eventTitle
        val id = context?.resources?.getIdentifier(eventTitle.toLowerCase(), "drawable", context?.packageName)
        if (id != null) {
            customlayout.findViewById<ImageView>(R.id.img).setBackgroundResource(id)
        }

        if (!sessionManager.fetchUserName().isNullOrEmpty()) {
            apiClient = ApiClient()
            activity?.let {
                apiClient.getApiServiceGateway(it).getActivityFromId(activityId)
                    .enqueue(object : Callback<Activity?> {
                        override fun onResponse(call: Call<Activity?>, response: Response<Activity?>) {
                            var descr = ""
                            if (translate) {
                                descr = response.body()?.descrEng.toString()
                            }else{
                                descr = response.body()?.descrIta.toString()
                            }
                            customlayout.findViewById<TextView>(R.id.sub_txt).text = descr
                        }
                        override fun onFailure(call: Call<Activity?>, t: Throwable) {
                            Toast.makeText(ApplicationContextProvider.getContext(), "ERRORE", Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }else{
            //TO-DO
        }

        builder?.setView(customlayout)
        builder?.setPositiveButton("OK") {
                dialog, which ->
            dialog.dismiss()
        }

        val alert = builder?.create()
        alert?.show()
    }

    private fun qrCodeEventDialog(eventTitle: String, eventId: String, eventDate: String, oraInizio: String, oraFine: String, userId: String) {
        val builder = activity?.let { android.app.AlertDialog.Builder(context, it.taskId) }
        val customlayout = layoutInflater.inflate(R.layout.qr_code_event_dialog, null)
        val qrCodeIV = customlayout.findViewById<ImageView>(R.id.idIVQrcode)

        var bitmap: Bitmap
        var qrgEncoder: QRGEncoder

        val manager = activity?.getSystemService(WINDOW_SERVICE) as WindowManager?

        val display = manager!!.defaultDisplay

        val point = Point()
        display.getSize(point)


        val width: Int = point.x
        val height: Int = point.y

        var dimen = if (width < height) width else height
        dimen = dimen * 3 / 4

        qrgEncoder = QRGEncoder(eventId + eventTitle + eventDate + oraInizio + oraFine + userId, null, QRGContents.Type.TEXT, dimen)


        try {
            bitmap = qrgEncoder.encodeAsBitmap()
            qrCodeIV.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            Log.e("Tag", e.toString())
        }

        builder?.setView(customlayout)
        builder?.setPositiveButton("OK") {
                dialog, which ->
            dialog.dismiss()
        }

        builder?.setTitle(eventTitle);

        val strBuilder = StringBuilder()
        if (translate) {
            strBuilder.appendln("CLIENT: " + sessionManager.fetchUserName())
            strBuilder.appendln(" ")
            strBuilder.appendln(activity?.resources?.getString(R.string.book_date_eng) + " " +  eventDate)
            strBuilder.appendln(activity?.resources?.getString(R.string.book_time_eng) + " " + oraInizio + " - " + oraFine)
        }else{
            strBuilder.appendln("CLIENTE: " + sessionManager.fetchUserName())
            strBuilder.appendln(" ")
            strBuilder.appendln(activity?.resources?.getString(R.string.book_date_ita) + " " +  eventDate)
            strBuilder.appendln(activity?.resources?.getString(R.string.book_time_ita) + " " + oraInizio + " - " + oraFine)
        }

        builder?.setMessage(strBuilder);

        val alert = builder?.create()
        alert?.show()
    }



}