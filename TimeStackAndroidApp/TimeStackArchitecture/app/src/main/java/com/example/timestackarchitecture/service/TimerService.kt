package com.example.timestackarchitecture.service

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.timestackarchitecture.MainActivity
import com.example.timestackarchitecture.R
import com.example.timestackarchitecture.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.timestackarchitecture.other.Constants.NOTIFICATION_ID
import com.example.timestackarchitecture.ui.components.StackTimer
import com.example.timestackarchitecture.viewmodels.TimerViewModel
import kotlinx.coroutines.*
import java.time.Duration
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
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val progress = TimerViewModel(this).getProgress()
        duration = intent?.getLongExtra("duration", 0)
        TimerViewModel(this).startTimer(progress, duration!!.toInt())
        println("service started")
        startForeground(NOTIFICATION_ID,  createNotification(progress))
        return START_STICKY
    }

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

        collapsedView.setTextViewText(R.id.tvCollapsedTitle, "Timer is running..")
        collapsedView.setTextViewText(R.id.tvCollapsedTime, "00:00:00")
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(RemoteViews(packageName, R.layout.notification_expanded))
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        NotificationManagerCompat.from(this).apply {
            //background service thread to update notification according to timer
            createThread(collapsedView, builder, progress)
            notify( NOTIFICATION_ID, builder.build())
        }
        return builder.build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createThread(
        collapsedView: RemoteViews,
        builder: NotificationCompat.Builder,
        progress: Int,
    ) {
        var i = progress
        executor = Executors.newSingleThreadScheduledExecutor()
        task = executor.scheduleAtFixedRate({
            println("$i")
            collapsedView.setTextViewText(R.id.tvCollapsedTime, "$i")
            i++
            println("duration $duration")
            if(i >= (duration?.toInt()!!/ 1000)) {

                collapsedView.setTextViewText(R.id.tvCollapsedTime, "task completed")
                builder.setCustomContentView(collapsedView)
                stopProgressNotificationThread()
            } else {
                builder.setCustomContentView(collapsedView)
            }

            NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, builder.build())

        }, 0, 1, TimeUnit.SECONDS)
    }

    fun stopProgressNotificationThread() {
        executor.shutdown()
        task.cancel(false)
        println("notification thread stopped")
    }
}