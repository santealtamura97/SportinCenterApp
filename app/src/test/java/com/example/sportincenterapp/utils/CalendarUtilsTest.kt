package com.example.sportincenterapp.utils

import com.example.sportincenterapp.data.models.Event
import org.junit.Test

import org.junit.Assert.*

class CalendarUtilsTest {

    @Test
    fun orderEvents() {
        val eventList: MutableList<Event> = mutableListOf()
        eventList.add(Event("601", "CROSSFIT", "12-09-2022", "10:30", "11:30", 20, "2", false, false))
        eventList.add(Event("602", "CROSSFIT", "16-09-2022", "9:30", "10:30", 20, "2", false, false))
        eventList.add(Event("603", "CROSSFIT", "14-09-2022", "7:30", "9:30", 20, "2", false, false))
        eventList.add(Event("604", "CROSSFIT", "21-09-2022", "15:30", "16:30", 20, "2", false, false))
        eventList.add(Event("605", "CROSSFIT", "19-09-2022", "14:30", "15:30", 20, "2", false, false))
        eventList.add(Event("606", "CROSSFIT", "08-09-2022", "13:30", "14:30", 20, "2", false, false))
        eventList.add(Event("607", "CROSSFIT", "14-09-2022", "9:30", "10:30", 20, "2", false, false))

        val orderedEventList = CalendarUtils.orderEvents(eventList)

        val myOrderedEventList: MutableList<Event> = mutableListOf()

        myOrderedEventList.add(Event("606", "CROSSFIT", "08-09-2022", "13:30", "14:30", 20, "2", false, false))
        myOrderedEventList.add(Event("601", "CROSSFIT", "12-09-2022", "10:30", "11:30", 20, "2", false, false))
        myOrderedEventList.add(Event("603", "CROSSFIT", "14-09-2022", "7:30", "9:30", 20, "2", false, false))
        myOrderedEventList.add(Event("607", "CROSSFIT", "14-09-2022", "9:30", "10:30", 20, "2", false, false))
        myOrderedEventList.add(Event("602", "CROSSFIT", "16-09-2022", "9:30", "10:30", 20, "2", false, false))
        myOrderedEventList.add(Event("605", "CROSSFIT", "19-09-2022", "14:30", "15:30", 20, "2", false, false))
        myOrderedEventList.add(Event("604", "CROSSFIT", "21-09-2022", "15:30", "16:30", 20, "2", false, false))

        assertEquals(myOrderedEventList, orderedEventList)
    }

    @Test
    fun removePastEvents() {

        val eventList: MutableList<Event> = mutableListOf()
        eventList.add(Event("601", "CROSSFIT", "12-09-2021", "10:30", "11:30", 20, "2", false, false))
        eventList.add(Event("602", "CROSSFIT", "16-09-2021", "9:30", "10:30", 20, "2", false, false))
        eventList.add(Event("603", "CROSSFIT", "14-09-2021", "7:30", "9:30", 20, "2", false, false))
        eventList.add(Event("604", "CROSSFIT", "21-09-2021", "15:30", "16:30", 20, "2", false, false))
        eventList.add(Event("605", "CROSSFIT", "19-09-2021", "14:30", "15:30", 20, "2", false, false))
        eventList.add(Event("606", "CROSSFIT", "08-09-2021", "13:30", "14:30", 20, "2", false, false))
        eventList.add(Event("607", "CROSSFIT", "14-09-2021", "9:30", "10:30", 20, "2", false, false))

        val futureEventList = CalendarUtils.removePastEvents(eventList)

        val myFutureEventList: MutableList<Event> = mutableListOf()

        myFutureEventList.add(Event("605", "CROSSFIT", "19-09-2021", "14:30", "15:30", 20, "2", false, false))
        myFutureEventList.add(Event("604", "CROSSFIT", "21-09-2021", "15:30", "16:30", 20, "2", false, false))

        assertTrue(myFutureEventList.size == futureEventList.size && myFutureEventList.containsAll(futureEventList) && futureEventList.containsAll(myFutureEventList))
    }

    @Test
    fun orderEventsByTime() {

        val eventList: MutableList<Event> = mutableListOf()
        eventList.add(Event("601", "CROSSFIT", "21-09-2021", "10:30", "11:30", 20, "2", false, false))
        eventList.add(Event("602", "CROSSFIT", "21-09-2021", "9:30", "10:30", 20, "2", false, false))
        eventList.add(Event("603", "CROSSFIT", "21-09-2021", "7:30", "9:30", 20, "2", false, false))
        eventList.add(Event("604", "CROSSFIT", "21-09-2021", "15:30", "16:30", 20, "2", false, false))
        eventList.add(Event("605", "CROSSFIT", "21-09-2021", "14:30", "15:30", 20, "2", false, false))
        eventList.add(Event("606", "CROSSFIT", "21-09-2021", "13:30", "14:30", 20, "2", false, false))
        eventList.add(Event("607", "CROSSFIT", "21-09-2021", "9:35", "10:30", 20, "2", false, false))

        val orderedEventListByTime = CalendarUtils.orderEventsByTime(eventList)

        val myOrderedEventListByTime: MutableList<Event> = mutableListOf()

        myOrderedEventListByTime.add(Event("603", "CROSSFIT", "21-09-2021", "7:30", "9:30", 20, "2", false, false))
        myOrderedEventListByTime.add(Event("602", "CROSSFIT", "21-09-2021", "9:30", "10:30", 20, "2", false, false))
        myOrderedEventListByTime.add(Event("607", "CROSSFIT", "21-09-2021", "9:35", "10:30", 20, "2", false, false))
        myOrderedEventListByTime.add(Event("601", "CROSSFIT", "21-09-2021", "10:30", "11:30", 20, "2", false, false))
        myOrderedEventListByTime.add(Event("606", "CROSSFIT", "21-09-2021", "13:30", "14:30", 20, "2", false, false))
        myOrderedEventListByTime.add(Event("605", "CROSSFIT", "21-09-2021", "14:30", "15:30", 20, "2", false, false))
        myOrderedEventListByTime.add(Event("604", "CROSSFIT", "21-09-2021", "15:30", "16:30", 20, "2", false, false))


        assertEquals(myOrderedEventListByTime, orderedEventListByTime)


    }
}