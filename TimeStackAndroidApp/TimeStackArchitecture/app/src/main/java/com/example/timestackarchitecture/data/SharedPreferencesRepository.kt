package com.example.timestackarchitecture.data

import android.content.Context

class SharedPreferencesProgressRepository(context: Context){
    private val sharedPrefTimer = context.getSharedPreferences("my_prefs_timer", Context.MODE_PRIVATE)
    private val completedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveTimerProgress(progress: Int) {
        sharedPrefTimer.edit().putInt("progress", progress).apply()
    }

    fun getTimerProgress(): Int {
        return sharedPrefTimer.getInt("progress", 0)
    }

}