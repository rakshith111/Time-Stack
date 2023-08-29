package com.example.timestackarchitecture.habitualmode.components

import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.timestackarchitecture.casualmode.service.TimerAlarmReceiver
import com.example.timestackarchitecture.casualmode.service.TimerService
import com.example.timestackarchitecture.habitualmode.data.HabitualStackData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

suspend fun afterPlay(
    play: Boolean,
    context: Context,
    stackList: List<HabitualStackData>,
    getProgress:  Long,
    updateProgress: suspend (Long) -> Unit,
    saveCurrentTime: suspend (Long) -> Unit,
    getStartTime: suspend () -> Long,
    saveFirstTime: suspend (Boolean) -> Unit,
    getFirstTime: () -> Boolean
) {
    if (play) {
        Timber.d("Timer started")
        //use system time to update progress
        saveCurrentTime(System.currentTimeMillis())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceIntent = Intent(context, TimerService::class.java)
            serviceIntent.putExtra("duration", stackList[0].stackTime)
            serviceIntent.putExtra("stackName", stackList[0].stackName)
            context.startService(serviceIntent)
        }

        if (stackList.isNotEmpty()) {
            val remainingTime = stackList[0].stackTime - getProgress
            TimerAlarmReceiver().setTimerAlarm(context, remainingTime)
            Timber.d("set alarm for $remainingTime")
            if (getFirstTime()) {
                CoroutineScope(Dispatchers.IO).launch {
                    updateProgress(0)
                }
            }
        }

        saveFirstTime(false)
//                               //start notification
    } else {
        Timber.d("Timer paused")
        //stop notification
        val elapsed =
            (System.currentTimeMillis() - getStartTime()) + getProgress
        CoroutineScope(Dispatchers.IO).launch {
            updateProgress(elapsed)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceIntent =
                Intent(context, TimerService::class.java)
            context.stopService(serviceIntent)
        }
        TimerAlarmReceiver().cancelTimerAlarm(context)
    }
}