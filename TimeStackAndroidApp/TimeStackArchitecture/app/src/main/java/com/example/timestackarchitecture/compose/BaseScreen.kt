package com.example.timestackarchitecture.compose


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timestackarchitecture.data.StackData
import com.example.timestackarchitecture.viewmodels.*

@Composable
fun BaseScreen(
    stackViewModelFactory: StackViewModelFactory,
    timerViewModelFactory: TimerViewModelFactory,
    stackViewModel: StackViewModel = viewModel(factory = stackViewModelFactory),
    timerViewModel: TimerViewModel = viewModel(factory = timerViewModelFactory),
) {
    val stackList = stackViewModel.stackList
    val selectedItems = stackViewModel.selectedItems

    if(stackList.isEmpty()){
        timerViewModel.saveProgress(0)
    }

    Container(
        stackList, selectedItems,
        startTimer = { playedTime: Int, duration: Int ->
            timerViewModel.startTimer(playedTime, duration)
        },
        stopTimer = { pauseTimer: (Int) -> Unit ->
            timerViewModel.stopTimer(pauseTimer)
        } ,
        totalPlayedTime = { timerViewModel.getProgress() },
        updateProgress = { playedTime: Int -> timerViewModel.saveProgress(playedTime) },
        insertStack = { stackData: StackData ->
            stackViewModel.insertStack(stackData) },
        updateStack = { stackData: StackData ->
            stackViewModel.updateStack(stackData) }
    ) { stackData: StackData -> stackViewModel.removeStack(stackData) }
}