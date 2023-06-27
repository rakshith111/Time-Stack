package com.example.timestackarchitecture.casualmode.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.*
import android.os.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.timestackarchitecture.MainActivity
import com.example.timestackarchitecture.R
import com.example.timestackarchitecture.casualmode.data.SharedPreferencesProgressRepository
import com.example.timestackarchitecture.casualmode.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.timestackarchitecture.casualmode.other.Constants.NOTIFICATION_ID
import com.example.timestackarchitecture.ui.components.convertTime
import kotlinx.coroutines.*
import timber.log.Timber


class TimerService : Service(){
    companion object {
        private var duration: Long? = null
        private lateinit var ringtone: Ringtone
        private lateinit var NotificationRingtone: MediaPlayer
        var isDeviceActive = true
        var stackName = ""
        var convertedTime = ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        duration = intent?.getLongExtra("duration", 0)
        stackName = intent?.getStringExtra("stackName") ?: ""
        val durationSeconds = duration?.div(1000)
        // Convert the selected time from seconds to hours and minutes
        val hours = durationSeconds?.div(3600)
        val remainingSeconds = durationSeconds?.minus((hours?.times(3600)!!))
        val minutes = remainingSeconds?.div(60)

        convertedTime = convertTime(hours!!, minutes!!)
        Timber.d("service started duration = $duration")
         startForeground(NOTIFICATION_ID,  createNotification())
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun createNotification(): Notification {

        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        } else {
            FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, flags)

        val defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(applicationContext, RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, defaultRingtoneUri)
        NotificationRingtone = MediaPlayer.create(applicationContext, R.raw.promise)
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stack_noti)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultRingtoneUri)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentTitle(stackName)
            .setContentText("Timer is running for $convertedTime")

        NotificationManagerCompat.from(this).apply {
            notify( NOTIFICATION_ID, builder.build())
        }
        return builder.build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    fun updateNotificationContent(context: Context) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.action = "ALARM_TRIGGERED"
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        } else {
            FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, flags)
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stack_noti)
            .setOngoing(true)
            .setSilent(true)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentTitle(stackName)
            .setContentText("Activity completed")

        NotificationManagerCompat.from(context).apply {
            notify(NOTIFICATION_ID, builder.build())
        }
    }


    fun stopRingtone(){
        try {
            if(ringtone.isPlaying) {
                Timber.d("ringtone stopped")
                ringtone.stop()
            }
        } catch (e: Exception) {
            Timber.d("ringtone not playing")
        }
    }

    fun startRingtone(context: Context){
        if(!ringtone.isPlaying ) {
            Timber.d("ringtone started")
            ringtone.play()
            SharedPreferencesProgressRepository(context).saveAlarmTriggered(true)
        }
    }

    fun startNotificationRingtone(){
        Timber.d("notification ringtone started")
        NotificationRingtone.start()
    }
}