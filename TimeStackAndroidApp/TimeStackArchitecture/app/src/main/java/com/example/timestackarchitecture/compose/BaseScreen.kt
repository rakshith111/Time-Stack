package com.example.timestackarchitecture.compose

import androidx.compose.runtime.Composable
import com.example.timestackarchitecture.viewmodels.*

@Composable
fun BaseScreen(
    stackViewModelInstance: StackViewModel,
    timerViewModelInstance: TimerViewModel
) {
    val stackList = stackViewModelInstance.stacks
    val startTimerRef = timerViewModelInstance::startTimer
    val stopTimerRef = timerViewModelInstance::stopTimer
    val selectedItems = stackViewModelInstance.selectedItems
    Container(
        stackList, selectedItems,
        startTimerRef, stopTimerRef)
}