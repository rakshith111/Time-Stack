package com.example.timestackarch.viewmodels

import androidx.lifecycle.ViewModel
import com.example.timestackarch.ui.components.StackTimer

class TimerViewModel : ViewModel() {
    fun startTimer(totalPlayedTime: Int) {
        StackTimer.startTimer(totalPlayedTime)
    }

    fun stopTimer(pauseTimer: (Int) -> Unit) {
        StackTimer.stopTimer(pauseTimer)
    }
}