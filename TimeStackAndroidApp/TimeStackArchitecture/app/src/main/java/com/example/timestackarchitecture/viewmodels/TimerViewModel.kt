package com.example.timestackarchitecture.viewmodels

import androidx.lifecycle.ViewModel
import com.example.timestackarchitecture.ui.components.StackTimer


class TimerViewModel : ViewModel() {
        fun startTimer(totalPlayedTime: Int) {
            StackTimer.startTimer(totalPlayedTime)
        }

        fun stopTimer(pauseTimer: (Int) -> Unit) {
            StackTimer.stopTimer(pauseTimer)
        }

}