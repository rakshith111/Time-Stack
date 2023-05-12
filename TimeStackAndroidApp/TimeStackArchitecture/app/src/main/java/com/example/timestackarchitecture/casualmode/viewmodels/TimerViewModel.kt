package com.example.timestackarchitecture.casualmode.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.timestackarchitecture.casualmode.data.SharedPreferencesProgressRepository


class TimerViewModel(context : Context) : ViewModel(){

    private var sharedPreferencesManager = SharedPreferencesProgressRepository(context)
    fun getProgress() = sharedPreferencesManager.getTimerProgress()
    fun saveProgress(currentTime: Long) = sharedPreferencesManager.saveTimerProgress(currentTime)
    fun getStartTime() = sharedPreferencesManager.getStartTime()
    fun saveCurrentTime(currentTime: Long) = sharedPreferencesManager.saveCurrentTime(currentTime)

    fun firstTime() = sharedPreferencesManager.firstTime()

    fun setFirstTime(firstTime: Boolean) = sharedPreferencesManager.saveFirstTime(firstTime)

    fun getAlarmTriggered() = sharedPreferencesManager.getAlarmTriggered()

    fun saveAlarmTriggered(isAlarmTriggered: Boolean) = sharedPreferencesManager.saveAlarmTriggered(isAlarmTriggered)

}