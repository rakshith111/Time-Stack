package com.example.timestackarchitecture.service

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.media.*
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
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
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class TimerService : Service(){
    companion object {
        private lateinit var executor: ScheduledExecutorService
        private lateinit var task: ScheduledFuture<*>
        private var duration: Long? = null
        private lateinit var ringtone: Ringtone
        private lateinit var NotificationRingtone: MediaPlayer
        var isAppInForeground = false
        var stackName = ""
        var convertedTime = ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val progress = TimerViewModel(this).getProgress()
        duration = intent?.getLongExtra("duration", 0)
        stackName = intent?.getStringExtra("stackName") ?: ""
        TimerViewModel(this).startTimer(progress, duration!!.toInt())
        duration = duration?.div(1000)

// Convert the selected time from seconds to hours and minutes
        val hours = duration?.div(3600)
        val remainingSeconds = duration?.minus((hours?.times(3600)!!))
        val minutes = remainingSeconds?.div(60)

        convertedTime = convertTime(hours!!, minutes!!)

        Timber.d("service started $duration")
        startForeground(NOTIFICATION_ID,  createNotification(progress))
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(progress: Int): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
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
        collapsedView.setTextViewText(R.id.tvCollapsedTime, "00:00:00")

        expandedView.setTextViewText(R.id.text_view_expanded, "1 2 3 4 %")
        expandedView.setTextViewText(R.id.text_view_activity_name, stackName)
        expandedView.setTextViewText(R.id.text_view_duration, convertedTime)

        val defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
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
            .setColorized(true)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())

        NotificationManagerCompat.from(this).apply {
            //background service thread to update notification according to timer
            createThread(collapsedView, expandedView, builder, progress)
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
        ) {
        var i = progress
        var percentage = 0
        executor = Executors.newSingleThreadScheduledExecutor()
        task = executor.scheduleAtFixedRate({
            Timber.d("service running $i")
            collapsedView.setTextViewText(R.id.tvCollapsedTime, "$percentage%")
            expandedView.setTextViewText(R.id.text_view_expanded, "$percentage%")
            i++
            percentage = ((i.toFloat() / (duration?.toFloat()!!)) * 100).toInt()
            percentage = 100 - percentage
            if(percentage == 50 || percentage == 10){
                NotificationRingtone.start()
            }
            Timber.d("duration $duration percentage $percentage%")
            if(i >= (duration?.toInt()!!)) {
                collapsedView.setTextViewText(R.id.tvCollapsedTime, "Task completed")
                expandedView.setTextViewText(R.id.text_view_expanded, "Task completed")
                collapsedView.setTextViewText(R.id.tvCollapsedTitle, "Timer stopped")
                builder.setCustomContentView(collapsedView)
                    .setCustomBigContentView(expandedView)
                ringtone.play()
                stopProgressNotificationThread("onStopCommand")
            } else {
                builder.setCustomContentView(collapsedView)
                    .setCustomBigContentView(expandedView)
            }

            NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, builder.build())

        }, 0, 1, TimeUnit.SECONDS)
    }

    fun stopProgressNotificationThread(LifecycleEvent: String) {
        executor.shutdown()
        task.cancel(false)
        Timber.d("notification thread stopped $LifecycleEvent")
        if (LifecycleEvent == "onCreate" || LifecycleEvent == "onResume") {
            ringtone.stop()
        }
    }
}
