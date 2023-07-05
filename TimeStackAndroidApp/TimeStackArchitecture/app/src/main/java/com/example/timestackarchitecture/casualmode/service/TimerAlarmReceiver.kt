package com.example.timestackarchitecture.casualmode.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class TimerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Trigger the ringtone
        Timber.d("Alarm triggered")
        if(!TimerService.isDeviceActive){
            TimerService().startRingtone(context)
        } else {
            TimerService().startNotificationRingtone()
        }
        TimerService().updateNotificationContent(context)
    }

    fun setTimerAlarm(context: Context, duration: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TimerAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        // Set the alarm to trigger at the end of the timer duration
        val remainingTime = System.currentTimeMillis() + duration
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, remainingTime, pendingIntent)
    }

    fun cancelTimerAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TimerAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }
}
