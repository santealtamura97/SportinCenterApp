package com.example.sportincenterapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.Event
import kotlinx.android.synthetic.main.event_item.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventAdapter(val modelList: List<Event>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(modelList.get(position));
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        sessionManager = SessionManager(ApplicationContextProvider.getContext()) //initialize session manager in this class
        return ViewHolder(layoutInflater.inflate(R.layout.event_item, parent, false))
    }

    override fun getItemCount(): Int {
        return modelList.size;
    }

    lateinit var mClickListener: ClickListener

    fun setOnClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(pos: Int, aView: View)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        init {
            itemView.setOnClickListener(this)
            itemView.findViewById<Button>(R.id.book_button).setOnClickListener() {
                bookEvent(modelList[position].id, sessionManager.fetchUserId()!!, modelList[position].title, modelList[position].data,
                    modelList[position].oraInizio, modelList[position].oraFine)
            }

        }

        fun bind(model: Event): Unit {
            itemView.txt.text = model.title
            itemView.sub_txt.text = itemView.sub_txt.text.toString() + model.number.toString()
            itemView.time.text = model.oraInizio + " - " + model.oraFine
            val id = context.resources.getIdentifier(model.title.toLowerCase(), "drawable", context.packageName)
            itemView.img.setBackgroundResource(id)
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick(adapterPosition, itemView)
        }

    }

    private fun bookEvent(eventId: String, userId: String, eventTitle: String, date: String, oraInizio: String, oraFine: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(eventTitle);

        val strBuilder = StringBuilder()
        strBuilder.appendln(context.resources.getString(R.string.book_event_confirm))
        strBuilder.appendln(" ")
        strBuilder.appendln(context.resources.getString(R.string.book_date) + date)
        strBuilder.appendln(context.resources.getString(R.string.book_time) + oraInizio + " - " + oraFine)

        builder.setMessage(strBuilder);

        builder.setPositiveButton(R.string.book_yes) {
                dialog, which -> // Do nothing but close the dialog
                callBookEvent(userId, eventId)
        }

        builder.setNegativeButton(R.string.book_no) {
                dialog, which -> // Do nothing but close the dialog
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    private fun callBookEvent(userId : String, eventId: String) {
        apiClient = ApiClient()

        sessionManager = SessionManager(ApplicationContextProvider.getContext())

        apiClient.getApiServiceGateway(context).bookEvent(userId, eventId)
            .enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(ApplicationContextProvider.getContext(), "PRENOTAZIONE EFFETTUATA!", Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(ApplicationContextProvider.getContext(), "PRENOTAZIONE NON EFFETTUATA!", Toast.LENGTH_LONG).show()
            }
        })
    }
}