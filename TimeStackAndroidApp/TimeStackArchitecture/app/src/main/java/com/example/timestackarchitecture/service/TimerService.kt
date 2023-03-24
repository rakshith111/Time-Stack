package com.example.timestackarchitecture.service

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.graphics.Typeface
import android.media.*
import android.os.Build
import android.os.IBinder
import android.util.TypedValue
import android.widget.RemoteViews
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import com.example.timestackarchitecture.MainActivity
import com.example.timestackarchitecture.R
import com.example.timestackarchitecture.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.timestackarchitecture.other.Constants.NOTIFICATION_ID
import com.example.timestackarchitecture.viewmodels.TimerViewModel
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


@OptIn(DelicateCoroutinesApi::class)
class TimerService : Service(){
    companion object {
        private lateinit var executor: ScheduledExecutorService
        private lateinit var task: ScheduledFuture<*>
        private var duration: Long? = null
        private lateinit var ringtone: Ringtone
        var isAppInForeground = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val progress = TimerViewModel(this).getProgress()
        duration = intent?.getLongExtra("duration", 0)
        TimerViewModel(this).startTimer(progress, duration!!.toInt())
        duration = duration?.div(1000)
        println("service started")
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

        val defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, defaultRingtoneUri)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setOngoing(true)
            .setSilent(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultRingtoneUri)
            .setColorized(true)

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
            println("$i")
            collapsedView.setTextViewText(R.id.tvCollapsedTime, "$percentage%")
            expandedView.setTextViewText(R.id.text_view_expanded, "$percentage%")
            i++
            percentage = ((i.toFloat() / (duration?.toFloat()!!)) * 100).toInt()
            percentage = 100 - percentage
            if(percentage == 10){
                ringtone.play()
            }
            println("duration $duration percentage $percentage%")
            if(i >= (duration?.toInt()!!)) {
                collapsedView.setTextViewText(R.id.tvCollapsedTime, "task completed")
                expandedView.setTextViewText(R.id.text_view_expanded, "task completed")
                builder.setCustomContentView(collapsedView)
                    .setCustomBigContentView(expandedView)
                ringtone.play()
                stopProgressNotificationThread()
            } else {
                builder.setCustomContentView(collapsedView)
                    .setCustomBigContentView(expandedView)
            }

            NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, builder.build())

        }, 0, 1, TimeUnit.SECONDS)
    }

    fun stopProgressNotificationThread() {
        executor.shutdown()
        task.cancel(false)
        println("notification thread stopped")
        if(isAppInForeground){
            ringtone.stop()
        }
    }
}
