package com.tqs.filecommander.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


object DateUtils {
    fun getMothTimeByMillis(millis: Long): String {
        val currentTime: String = SimpleDateFormat("yyyy-MM").format(millis)
        return "$currentTime"
    }

    fun getMonthTimeBySecond(second: Long): String {
        return getMothTimeByMillis(second * 1000)
    }

    fun getDateTimeByMillis(millis: Long): CharSequence? {
        val date = Date(millis)
        val year = date.year
        val month = date.month + 1
        val day = date.day
        val hours = date.hours
        val minutes = date.minutes
        val monthString = getMonthString(month)
        val hourMinuteString = getHourMinuteString(hours, minutes)
        return "$hourMinuteString, $day $monthString"
    }

    fun getDateTimeBySecond(second: Long): CharSequence? {
        return getDateTimeByMillis(second * 1000)
    }

    fun getCurrentTime(): CharSequence? {
        val calendar = Calendar.getInstance(Locale("en"))
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val date = calendar.get(Calendar.DATE)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val monthString = getMonthString(month)
        val hourMinuteString = getHourMinuteString(hour, minute)
        return "$hourMinuteString, $date $monthString"
    }

    private fun getHourMinuteString(hour: Int, minute: Int): String {
        return when (hour) {
            in 0..12 -> {
                "${hour}:${minute}am"
            }

            in 13..24 -> {
                "${hour - 12}:${minute}pm"
            }

            else -> {
                ""
            }
        }
    }

    private fun getMonthString(month: Int): String {
        return when (month) {
            1 -> {
                "Jan"
            }

            2 -> {
                "Feb"
            }

            3 -> {
                "Mar"
            }

            4 -> {
                "Apr"
            }

            5 -> {
                "May"
            }

            6 -> {
                "Jun"
            }

            7 -> {
                "Jul"
            }

            8 -> {
                "Aug"
            }

            9 -> {
                "Sept"
            }

            10 -> {
                "Oct"
            }

            11 -> {
                "Nov"
            }

            12 -> {
                "Dec"
            }

            else -> {
                ""
            }
        }
    }

    fun getMillisDay(millis: Long):Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return calendar.get(Calendar.DAY_OF_YEAR)
    }
}