package com.example.timestackarchitecture.habitualmode.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.timestackarchitecture.habitualmode.data.SharedPreferencesProgressRepositoryHabitual
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HabitualTimerViewModel @Inject constructor(context : Context) : ViewModel(){

    private var sharedPreferencesManager = SharedPreferencesProgressRepositoryHabitual(context)
    fun getProgress() = sharedPreferencesManager.getTimerProgress()
    fun saveProgress(currentTime: Long) = sharedPreferencesManager.saveTimerProgress(currentTime)
    fun getStartTime() = sharedPreferencesManager.getStartTime()
    fun saveCurrentTime(currentTime: Long) = sharedPreferencesManager.saveCurrentTime(currentTime)

    fun firstTime() = sharedPreferencesManager.firstTime()

    fun setFirstTime(firstTime: Boolean) = sharedPreferencesManager.saveFirstTime(firstTime)

    fun getAlarmTriggered() = sharedPreferencesManager.getAlarmTriggered()

    fun saveAlarmTriggered(isAlarmTriggered: Boolean) = sharedPreferencesManager.saveAlarmTriggered(isAlarmTriggered)

}