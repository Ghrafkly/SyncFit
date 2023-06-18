package com.example.syncfit

import java.util.concurrent.TimeUnit

fun String.toTimeStamp(): Long {
    val parts = this.split(":")

    val hours: Long
    val minutes: Long
    val seconds: Long

    if (parts.size == 3) {
        hours = TimeUnit.HOURS.toMillis(parts[0].toLong())
        minutes = TimeUnit.MINUTES.toMillis(parts[1].toLong())
        seconds = TimeUnit.SECONDS.toMillis(parts[2].toLong())
    } else {
        hours = 0
        minutes = TimeUnit.MINUTES.toMillis(parts[0].toLong())
        seconds = TimeUnit.SECONDS.toMillis(parts[1].toLong())
    }

    return hours + minutes + seconds
}

fun Long.fromTimeStamp(): String {
    var hours = TimeUnit.MILLISECONDS.toHours(this).toString()
    var minutes = (TimeUnit.MILLISECONDS.toMinutes(this) % 60).toString()
    var seconds = (TimeUnit.MILLISECONDS.toSeconds(this) % 60).toString()

    hours = hours.padStart(2, '0')
    minutes = minutes.padStart(2, '0')
    seconds = seconds.padStart(2, '0')

    return "$hours:$minutes:$seconds"
}
