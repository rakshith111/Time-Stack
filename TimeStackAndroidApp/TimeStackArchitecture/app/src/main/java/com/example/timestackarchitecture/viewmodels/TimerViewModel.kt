package com.example.timestackarchitecture.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.timestackarchitecture.data.SharedPreferencesProgressRepository
import com.example.timestackarchitecture.ui.components.StackTimer


class TimerViewModel(context : Context) : ViewModel(){

    private var sharedPreferencesManager = SharedPreferencesProgressRepository(context)

    fun startTimer(totalPlayedTime: Int, duration: Int) {
        StackTimer.startTimer(totalPlayedTime, duration)
    }

    fun stopTimer(pauseTimer: (Int) -> Unit) {
        StackTimer.stopTimer(pauseTimer)
    }

    fun getProgress() = sharedPreferencesManager.getProgress()
    fun saveProgress(progress: Int) = sharedPreferencesManager.saveProgress(progress)

}