package com.example.kadaituti

import java.text.SimpleDateFormat
import java.util.*

class DateManager {
    private var calendar = Calendar.getInstance()
    private val current = Calendar.getInstance().time

    fun getCalendar():List<Date>{
        val list = mutableListOf<Date>()
        var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-2
        if(dayOfWeek == -1){
            dayOfWeek = 6
        }
        calendar.add(Calendar.DATE, -dayOfWeek)

        for(i in 0..6){
            list.add(calendar.time)
            calendar.add(Calendar.DATE, 1)
        }
        calendar.time = current
        return list
    }
}