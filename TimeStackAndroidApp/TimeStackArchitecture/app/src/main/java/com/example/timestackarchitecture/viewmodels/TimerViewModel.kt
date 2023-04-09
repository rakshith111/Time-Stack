package com.example.timestackarchitecture.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.timestackarchitecture.data.SharedPreferencesProgressRepository


class TimerViewModel(context : Context) : ViewModel(){

    private var sharedPreferencesManager = SharedPreferencesProgressRepository(context)

    fun getTimer() = sharedPreferencesManager.getTimerProgress()
    fun saveTimer(currentTime: Int) = sharedPreferencesManager.saveTimerProgress(currentTime)

}