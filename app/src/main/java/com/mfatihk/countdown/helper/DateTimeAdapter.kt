package com.mfatihk.countdown.helper

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateTimeAdapter constructor(format:String = "yyyy-MM-dd'T'HH:mm:ss") {
    private var dateFormat: DateFormat = SimpleDateFormat(format, Locale.getDefault())

    @ToJson
    @Synchronized
    fun toJson(d: Date): String {
        return dateFormat.format(d)
    }

    @FromJson
    @Synchronized
    @Throws(ParseException::class)
    fun fromJson(s: String): Date {
        return dateFormat.parse(s)!!
    }

    init {
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
}