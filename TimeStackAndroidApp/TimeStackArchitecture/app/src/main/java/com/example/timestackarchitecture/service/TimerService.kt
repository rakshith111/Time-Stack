package com.example.timestackarchitecture.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.media.*
import android.os.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.timestackarchitecture.MainActivity
import com.example.timestackarchitecture.R
import com.example.timestackarchitecture.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.timestackarchitecture.other.Constants.NOTIFICATION_ID
import com.example.timestackarchitecture.ui.components.convertTime
import com.example.timestackarchitecture.viewmodels.TimerViewModel
import kotlinx.coroutines.*
import timber.log.Timber


class TimerService : Service(){
    companion object {
        @Volatile private var isThreadRunning = false
        private var duration: Long? = null
        private lateinit var ringtone: Ringtone
        private lateinit var NotificationRingtone: MediaPlayer
        var isDeviceActive = true
        var stackName = ""
        var convertedTime = ""
        var countDownTimer: CountDownTimer? = null
        private var isNotificationRingtonePlaying = false
        @SuppressLint("StaticFieldLeak")
        private lateinit var builder: NotificationCompat.Builder
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val progress = TimerViewModel(this).getTimer()
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

        // Start the countdown timer after the first notification update
            startCountdownTimer(progress, this)
        return START_STICKY
    }

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
        builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
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

    private fun startCountdownTimer(
        progress: Int,
        timerServiceContext: TimerService,
    ){
        var i = progress
        var percentage: Int
        val countRingtone = 0
        isThreadRunning = true
        val originalDuration = duration
        duration = duration?.minus (progress * 1000L)
        Timber.d("Duration before starting timer: $duration ${duration?.div(1000)}")
        countDownTimer = object : CountDownTimer(duration!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                i++
                TimerViewModel(timerServiceContext).saveTimer(i)
                percentage = ((i.times(1000).toFloat() / (originalDuration!!.toFloat())) * 100).toInt()

                if(originalDuration.div(millisUntilFinished).toFloat() == 2.0F){
                    Timber.d("notification ringtone started")
                    if (!isNotificationRingtonePlaying) {
                        NotificationRingtone.start()
                        isNotificationRingtonePlaying = true
                    }

                    countRingtone.plus(1)
                }
                if(percentage == 100 && isDeviceActive){
                    NotificationRingtone.start()
                }
                Timber.d("i = $i duration $duration percentage $percentage%")
            }

            override fun onFinish() {
                if(isDeviceActive){
                    Timber.d("Notification ringtone started")
                    NotificationRingtone.start()
                } else {
                    Timber.d("ringtone started")
                    ringtone.play()
                }
                builder.setContentText("Timer stopped")
                updateNotificationContent(builder)
                stopProgressNotificationThread {

                }
            }
        }
        countDownTimer?.start()
        NotificationRingtone.setOnCompletionListener {
            isNotificationRingtonePlaying = false
        }
    }

    private fun updateNotificationContent(
        builder: NotificationCompat.Builder,
    ) {
        builder.setContentText("Activity completed")
        NotificationManagerCompat.from(this).apply {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    fun stopProgressNotificationThread(stopNotification: () -> Unit) {
        Timber.d("notification thread stopped")
        countDownTimer?.cancel()
        stopNotification()
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
}