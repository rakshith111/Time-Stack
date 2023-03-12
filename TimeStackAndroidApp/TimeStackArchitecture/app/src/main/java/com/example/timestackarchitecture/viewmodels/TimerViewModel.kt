package com.example.timestackarchitecture.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.timestackarchitecture.data.SharedPreferencesRepository
import com.example.timestackarchitecture.ui.components.StackTimer
import java.io.Serializable


class TimerViewModel(context : Context) : ViewModel(), Serializable {
    private var sharedPreferencesManager = SharedPreferencesRepository(context)
    fun startTimer(totalPlayedTime: Int) {
        StackTimer.startTimer(totalPlayedTime)
    }

    fun stopTimer(pauseTimer: (Int) -> Unit) {
        StackTimer.stopTimer(pauseTimer)
    }

    fun getProgress() = sharedPreferencesManager.getProgress()
    fun saveProgress(progress: Int) = sharedPreferencesManager.saveProgress(progress)
}