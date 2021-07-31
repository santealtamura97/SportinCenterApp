package com.example.sportincenterapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.models.Activity
import com.example.sportincenterapp.data.models.Event
import kotlinx.android.synthetic.main.activity_item.view.*

class ActivityAdapter(val modelList: List<Activity>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(modelList.get(position));
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.activity_item, parent, false))
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(model: Activity): Unit {
            itemView.txt.text = model.name
            itemView.sub_txt.text = model.descr
            val id = context.resources.getIdentifier(model.name.toLowerCase(), "drawable", context.packageName)
            itemView.img.setBackgroundResource(id)
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick(adapterPosition, itemView)
        }
    }
}