package com.example.timestackarchitecture.casualmode.data

import android.content.Context

class SharedPreferencesProgressRepository(context: Context) {
    private val sharedPrefTimer = context.getSharedPreferences("my_prefs_timer", Context.MODE_PRIVATE)
    private val currentTimePrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val alarmTriggered = context.getSharedPreferences( "MyPrefsAlarm", Context.MODE_PRIVATE)

    fun saveTimerProgress(progress: Long) {
        sharedPrefTimer.edit().putLong("progress", progress).apply()
    }

    fun getTimerProgress(): Long {
        return sharedPrefTimer.getLong("progress", 0L)
    }

    fun saveCurrentTime(currentTime: Long) {
        currentTimePrefs.edit().putLong("currentTime", currentTime).apply()
    }

    fun getStartTime(): Long {
        return currentTimePrefs.getLong("currentTime", 0L)
    }

    fun firstTime() : Boolean { return currentTimePrefs.getBoolean("firstTime", true)}

    fun saveFirstTime(isFirstTime: Boolean){currentTimePrefs.edit().putBoolean("firstTime", isFirstTime).apply()}

    fun getAlarmTriggered() : Boolean { return alarmTriggered.getBoolean("alarmTriggered", false)}

    fun saveAlarmTriggered(isAlarmTriggered: Boolean){alarmTriggered.edit().putBoolean("alarmTriggered", isAlarmTriggered).apply()}

}