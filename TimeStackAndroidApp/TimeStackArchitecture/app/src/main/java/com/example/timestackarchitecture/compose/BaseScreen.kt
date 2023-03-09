package com.example.timestackarchitecture.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timestackarchitecture.data.StackData
import com.example.timestackarchitecture.viewmodels.*

@Composable
fun BaseScreen(
    stackViewModelFactory: StackViewModelFactory,
    timerViewModel: TimerViewModel,
    stackViewModel: StackViewModel = viewModel(factory = stackViewModelFactory),
) {
    val list = stackViewModel.stackList.collectAsState(initial = emptyList())
    val stackList = list.value

    val selectedItems = stackViewModel.selectedItems

    Container(
        stackList, selectedItems,
        startTimer = { totalPlayedTime: Int ->
            timerViewModel.startTimer(totalPlayedTime)
        },
        stopTimer = { pauseTimer: (Int) -> Unit ->
            timerViewModel.stopTimer(pauseTimer)
        } ,
        insertStack = { stackData: StackData ->
            stackViewModel.insertStack(stackData) },
        removeStack = { stackData: StackData -> stackViewModel.removeStack(stackData) }
    ) { stackViewModel.clearAll() }
}