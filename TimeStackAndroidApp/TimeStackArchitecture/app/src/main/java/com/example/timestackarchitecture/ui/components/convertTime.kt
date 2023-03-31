package com.example.timestackarchitecture.ui.components

fun convertTime(hours: Long, minutes: Long): String {
    val hoursString = if (hours == 1L) "hour" else "hours"
    val minutesString = if (minutes == 1L) "minute" else "minutes"
    return when {
        hours == 0L && minutes == 1L -> String.format("%d %s", minutes, minutesString)
        hours == 0L -> String.format("%d %s", minutes, minutesString)
        minutes == 0L -> String.format("%d %s", hours, hoursString)
        minutes == 1L && hours == 1L -> String.format("%d %s : %d %s", hours, hoursString, minutes, minutesString)
        minutes == 1L -> String.format("%d %s : %d %s", hours, hoursString, minutes, minutesString)
        hours == 1L -> String.format("%d %s : %d %s", hours, hoursString, minutes, minutesString)
        else -> String.format("%d %s : %d %s", hours, hoursString, minutes, minutesString)
    }
}
