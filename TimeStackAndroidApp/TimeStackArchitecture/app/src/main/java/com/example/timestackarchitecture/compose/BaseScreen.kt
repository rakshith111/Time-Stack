package com.example.timestackarchitecture.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timestackarchitecture.data.StackData
import com.example.timestackarchitecture.viewmodels.*
import com.google.android.gms.tagmanager.Container
import kotlin.reflect.KFunction1

@Composable
fun BaseScreen(
    stackViewModelFactory: StackViewModelFactory,
    timerViewModel: TimerViewModel,
    stackViewModel: StackViewModel = viewModel(factory = stackViewModelFactory),
) {
    val stackList = stackViewModel.stackList.collectAsState(initial = emptyList())
    val startTimerRef = { totalPlayedTime: Int ->
        timerViewModel.startTimer(totalPlayedTime)
    }
    val stopTimerRef = { pauseTimer: (Int) -> Unit ->
        timerViewModel.stopTimer(pauseTimer)
    }

    val selectedItems = stackViewModel.selectedItems

    Container(
        stackList, selectedItems,
        startTimerRef,
        stopTimerRef , insertStack = { stackData: StackData ->
            stackViewModel.insertStack(stackData) },
        removeStack = { stackData: StackData -> stackViewModel.removeStack(stackData) }
    ) { stackViewModel.clearAll() }
}