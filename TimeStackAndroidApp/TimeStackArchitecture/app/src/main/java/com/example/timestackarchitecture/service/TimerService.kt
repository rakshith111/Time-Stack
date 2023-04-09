package com.example.timestackarchitecture.service

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.media.*
import android.os.*
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.timestackarchitecture.MainActivity
import com.example.timestackarchitecture.R
import com.example.timestackarchitecture.data.SharedPreferencesProgressRepository
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
        startForeground(NOTIFICATION_ID,  createNotification(progress))
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(progress: Int): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        } else {
            FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, flags)
        val collapsedView = RemoteViews(
            packageName,
            R.layout.notification_collapsed
        )
        val expandedView = RemoteViews(
            packageName,
            R.layout.notification_expanded
        )
        collapsedView.setTextViewText(R.id.tvCollapsedTitle, "Timer is running..")

        expandedView.setTextViewText(R.id.text_view_activity_name, stackName)
        expandedView.setTextViewText(R.id.text_view_duration, convertedTime)

        val defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(applicationContext, RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, defaultRingtoneUri)
        NotificationRingtone = MediaPlayer.create(applicationContext, R.raw.promise)
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stack_noti)
            .setContentIntent(pendingIntent)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setOngoing(true)
            .setSilent(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultRingtoneUri)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())

        NotificationManagerCompat.from(this).apply {
            //background service thread to update notification according to timer
            createThread(collapsedView, expandedView, builder, progress, this@TimerService)
            notify( NOTIFICATION_ID, builder.build())
        }
        return builder.build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createThread(
        collapsedView: RemoteViews,
        expandedView: RemoteViews,
        builder: NotificationCompat.Builder,
        progress: Int,
        timerServiceContext: TimerService,
    ) {
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
                SharedPreferencesProgressRepository(this@TimerService).saveTimerProgress(i)
                percentage = ((i.times(1000).toFloat() / (originalDuration!!.toFloat())) * 100).toInt()
                collapsedView.setTextViewText(R.id.tvCollapsedTime, "$percentage%")
                expandedView.setTextViewText(R.id.text_view_expanded, "$percentage%")
                if(percentage == 50 ){
                    Timber.d("notification ringtone started")
                    NotificationRingtone.start()
                    countRingtone.plus(1)
                }
                Timber.d("i = $i duration $duration percentage $percentage%")
                if(i >= (duration?.toInt()!!)) {
                    collapsedView.setTextViewText(R.id.tvCollapsedTime, "Task completed")
                    expandedView.setTextViewText(R.id.text_view_expanded, "Task completed")
                    collapsedView.setTextViewText(R.id.tvCollapsedTitle, "Timer stopped")
                    builder.setCustomContentView(collapsedView)
                        .setCustomBigContentView(expandedView)
                    stopProgressNotificationThread {}
                } else {
                    builder.setCustomContentView(collapsedView)
                        .setCustomBigContentView(expandedView)
                }
                percentage = (((duration!! - millisUntilFinished) / 1000.0) / (duration!! / 1000.0) * 100).toInt()
                NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, builder.build())
            }

            override fun onFinish() {
                collapsedView.setTextViewText(R.id.tvCollapsedTime, "Task completed")
                expandedView.setTextViewText(R.id.text_view_expanded, "Task completed")
                collapsedView.setTextViewText(R.id.tvCollapsedTitle, "Timer stopped")
                builder.setCustomContentView(collapsedView)
                    .setCustomBigContentView(expandedView)

                if(isDeviceActive){
                    Timber.d("Notification ringtone started")
                    NotificationRingtone.start()
                } else {
                    Timber.d("ringtone started")
                    ringtone.play()
                }

                NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, builder.build())
                stopProgressNotificationThread {
                }
            }
        }
        countDownTimer?.start()
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
        } }

//    private val foregroundBackgroundReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            when (intent.action) {
//                Intent.ACTION_SCREEN_ON -> {
//                    isDeviceActive = true
//                    if (ringtone.isPlaying) {
//                        Timber.d("Stopping ringtone as user returned to app")
//                        ringtone.stop()
//                    }
//                }
//                Intent.ACTION_SCREEN_OFF -> isDeviceActive = false
//            }
//        }
//    }
//    override fun onCreate() {
//        super.onCreate()
//        val filter = IntentFilter().apply {
//            addAction(Intent.ACTION_SCREEN_ON)
//            addAction(Intent.ACTION_SCREEN_OFF)
//        }
//        registerReceiver(foregroundBackgroundReceiver, filter)
//
//    }
//
//    override fun onDestroy() {
//        unregisterReceiver(foregroundBackgroundReceiver)
//        super.onDestroy()
//    }

}