package com.example.timestackarchitecture.ui.components

fun convertTime(hours: Long, minutes: Long): String {
    return when {
        hours == 0L && minutes == 1L -> String.format("%d minute", minutes)
        hours == 0L -> String.format("%d minutes", minutes)
        minutes == 0L -> String.format("%d hours", hours)
        minutes == 1L && hours == 1L -> String.format("%d hour : %d minute", hours, minutes)
        minutes == 1L -> String.format("%d hours : %d minute", hours, minutes)
        hours == 1L -> String.format("%d hour : %d minutes", hours, minutes)
        else -> String.format("%d hours : %d minutes", hours, minutes)
    }
}