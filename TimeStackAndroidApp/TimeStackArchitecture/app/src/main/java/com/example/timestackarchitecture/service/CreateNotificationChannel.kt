package com.example.timestackarchitecture.service

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.timestackarchitecture.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.timestackarchitecture.other.Constants.NOTIFICATION_CHANNEL_NAME

class CreateNotificationChannel(private val context: Context) {

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = android.app.NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                importance
            )
                .apply { description = "Timer notification channel" }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
