package com.example.sportincenterapp.utils

import com.example.sportincenterapp.data.models.Event
import java.text.SimpleDateFormat
import java.util.*

object CalendarUtils {

    /**
     * Bubble sort
     */
    fun orderEvents(eventList: MutableList<Event>) : MutableList<Event> {
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

    fun removePastEvents(eventList: MutableList<Event>) : MutableList<Event> {
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


    fun orderEventsByTime(eventList: MutableList<Event>) : MutableList<Event> {
        removePastEventsByTime(eventList)
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

    private fun removePastEventsByTime(eventList: MutableList<Event>) : MutableList<Event> {
        var toRemove : MutableList<Event> = mutableListOf()
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val todayDate = sdf.parse(sdf.format(Date()))
        for (event in eventList) {
            var eventDate = sdf.parse(event.data + " " + event.oraInizio)
            if (eventDate.before(todayDate) || eventDate.equals(todayDate)) {
                toRemove.add(event)
            }
        }
        eventList.removeAll(toRemove)
        return eventList
    }

}