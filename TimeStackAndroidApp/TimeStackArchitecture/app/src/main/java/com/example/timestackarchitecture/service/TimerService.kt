package com.example.timestackarchitecture.service

import android.app.*
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.timestackarchitecture.MainActivity
import com.example.timestackarchitecture.R
import com.example.timestackarchitecture.other.Constants.ACTION_PAUSE_SERVICE
import com.example.timestackarchitecture.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.timestackarchitecture.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.timestackarchitecture.other.Constants.ACTION_STOP_SERVICE
import com.example.timestackarchitecture.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.timestackarchitecture.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.timestackarchitecture.other.Constants.NOTIFICATION_ID
import com.example.timestackarchitecture.viewmodels.TimerViewModel


class TimerService : Service(){


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        TimerViewModel(this).startTimer(TimerViewModel(this).getProgress())
        println("service started")
        startForeground(NOTIFICATION_ID, createNotification())
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        } else {
            FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, flags)

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Timer")
            .setContentText("Your timer is running...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}