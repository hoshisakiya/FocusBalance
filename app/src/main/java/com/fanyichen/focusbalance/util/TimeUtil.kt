package com.fanyichen.focusbalance.util

import java.text.SimpleDateFormat
import java.util.*

enum class FBDateFormat(val value: String) {
    FORMAT1("yyyy-MM-dd hh:mm:ss"),
    FORMAT2("yyyy-MM-dd")
}

fun getTodayString(): String {
    return formatDate(FBDateFormat.FORMAT2, Date())
}

fun getNow(): String {
    return formatDate(FBDateFormat.FORMAT1, Date())
}

fun formatDate(format: FBDateFormat, date: Date): String {
    val sdf = SimpleDateFormat(format.value, Locale.CHINA)
    return sdf.format(date)
}

fun getDate(date: String): Date? {
    val sdf = SimpleDateFormat(FBDateFormat.FORMAT1.value, Locale.CHINA)
    return sdf.parse(date)
}

fun getMin(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.MINUTE)
}

fun getSecond(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.SECOND)
}

fun getTimeDeltaMinute(prev: Date, end: Date): Long {
    val diff: Long = end.getTime() - prev.getTime()
    return diff / (1000 * 60)
}

fun getTimeDeltaSecond(prev: Date, end: Date): Long {
    val diff: Long = prev.getTime() - end.getTime()
    return diff / (1000 * 60 * 60)
}

fun isToday(date: Date): Boolean {
    val dateString = formatDate(FBDateFormat.FORMAT2, date)
    return dateString == getTodayString()
}