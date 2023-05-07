package com.ntduc.datetimeutils

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

val currentMillis get() = System.currentTimeMillis()
val currentDate get() = Date(System.currentTimeMillis())
val currentCalendar get() = Calendar.getInstance()

val Calendar.year: Int
  get() = get(Calendar.YEAR)

val Calendar.dayOfMonth: Int
  get() = get(Calendar.DAY_OF_MONTH)

val Calendar.month: Int
  get() = get(Calendar.MONTH)

val Calendar.hour: Int
  get() = get(Calendar.HOUR)

val Calendar.hourOfDay: Int
  get() = get(Calendar.HOUR_OF_DAY)

val Calendar.minute: Int
  get() = get(Calendar.MINUTE)

val Calendar.second: Int
  get() = get(Calendar.SECOND)

val Date.toCalendar: Calendar
  get() {
    val calendar = currentCalendar
    calendar.time = this
    return calendar
  }

fun Date.isFuture(): Boolean {
  return !Date().before(this)
}

fun Date.isTomorrow(): Boolean {
  return DateUtils.isToday(this.time - DateUtils.DAY_IN_MILLIS)
}

fun Date.isToday(): Boolean {
  return DateUtils.isToday(this.time)
}

fun Date.isYesterday(): Boolean {
  return DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)
}

fun Date.isPast(): Boolean {
  return Date().before(this)
}

private val DATEFORMAT = "dd-MM-yyyy HH:mm:ss"

@SuppressLint("SimpleDateFormat")
fun stringDateToDate(StrDate: String): Date? {
  var dateToReturn: Date? = null
  val dateFormat = SimpleDateFormat(DATEFORMAT)
  dateFormat.timeZone = TimeZone.getTimeZone("UTC")
  try {
    dateToReturn = dateFormat.parse(StrDate)
  } catch (e: ParseException) {
    e.printStackTrace()
  }
  
  return dateToReturn
}

fun timeAsMillis(hours: Int, minutes: Int, seconds: Int): Long {
  return TimeUnit.HOURS.toMillis(hours.toLong()) + TimeUnit.MINUTES.toMillis(minutes.toLong()) + TimeUnit.SECONDS.toMillis(
    seconds.toLong()
  )
}

fun Long.formatAsTime(): String {
  val seconds = (TimeUnit.MILLISECONDS.toSeconds(this) % 60L).toInt()
  val minutes = (TimeUnit.MILLISECONDS.toMinutes(this) % 60L).toInt()
  val hours = TimeUnit.MILLISECONDS.toHours(this).toInt()
  return if (hours == 0) String.format(
    "%02d:%02d", minutes, seconds
  ) else String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun extractHours(millis: Long): Long {
  return millis / (1000 * 60 * 60)
}

fun extractMinutes(millis: Long): Long {
  return millis / (1000 * 60) % 60
}

fun extractSeconds(millis: Long): Long {
  return millis / 1000 % 60
}

fun convertDate(
  date: String,
  formatDefault: String,
  formatWanted: String,
  localeDefault: Locale = Locale.getDefault(),
  localeWanted: Locale = Locale.getDefault()
): String {
  val format1 = SimpleDateFormat(formatDefault, localeDefault)
  val format2 = SimpleDateFormat(formatWanted, localeWanted)
  return try {
    format2.format(format1.parse(date) ?: date)
  } catch (e: ParseException) {
    date
  }
}

fun getDateTimeFromMillis(
  millis: Long, dateFormat: String, locale: Locale = Locale.getDefault()
): String = SimpleDateFormat(dateFormat, locale).format(Date(millis))

const val TIME_12HOUR = "hh:mm:ss a"
const val TIME_24HOUR = "HH:mm:ss"

fun get24HourCurTime(): String {
  return getDateTimeFromMillis(
    currentMillis, TIME_24HOUR
  )
}

fun get12HourCurTime(): String {
  return getDateTimeFromMillis(
    currentMillis, TIME_12HOUR
  )
}

fun getCurDate(format: String): String {
  return getDateTimeFromMillis(
    currentMillis, format
  )
}

fun convert24HoursTimeTo12HoursTime(date: String): String =
  convertDate(date, TIME_24HOUR, TIME_12HOUR)

fun convert12HoursTimeTo24HoursTime(date: String): String =
  convertDate(date, TIME_12HOUR, TIME_24HOUR)