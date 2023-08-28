package com.example.timestackarchitecture.habitualmode.data

import android.content.Context
import timber.log.Timber

class SharedPreferencesProgressRepositoryHabitual(context: Context) {
    private val sharedPrefTimer = context.getSharedPreferences("my_prefs_timer_habitual", Context.MODE_PRIVATE)
    private val currentTimePrefs = context.getSharedPreferences("MyPrefs_habitual", Context.MODE_PRIVATE)
    private val alarmTriggered = context.getSharedPreferences( "MyPrefsAlarm_habitual", Context.MODE_PRIVATE)

    //save the progress of the timer
    fun saveTimerProgress(progress: Long) {
        sharedPrefTimer.edit().putLong("progressHabitual", progress).apply()
        Timber.d("updateProgress: $progress")
    }

    //get the progress of the timer
    fun getTimerProgress(): Long {
        return sharedPrefTimer.getLong("progressHabitual", 0L)
    }

    //save the current time
    fun saveCurrentTime(currentTime: Long) {
        currentTimePrefs.edit().putLong("currentTimeHabitual", currentTime).apply()
    }
    //get the current time
    fun getStartTime(): Long {
        return currentTimePrefs.getLong("currentTimeHabitual", 0L)
    }
    fun firstTime() : Boolean { return currentTimePrefs.getBoolean("firstTimeHabitual", true)}

    fun saveFirstTime(isFirstTime: Boolean){currentTimePrefs.edit().putBoolean("firstTimeHabitual", isFirstTime).apply()}

    fun getAlarmTriggered() : Boolean { return alarmTriggered.getBoolean("alarmTriggeredHabitual", false)}

    fun saveAlarmTriggered(isAlarmTriggered: Boolean){alarmTriggered.edit().putBoolean("alarmTriggeredHabitual", isAlarmTriggered).apply()}

}