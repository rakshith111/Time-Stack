package com.example.timestackarchitecture.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timestackarchitecture.data.SharedPreferencesManager
import com.example.timestackarchitecture.data.StackData
import com.example.timestackarchitecture.viewmodels.*

@Composable
fun BaseScreen(
    stackViewModelFactory: StackViewModelFactory,
    timerViewModel: TimerViewModel,
    stackViewModel: StackViewModel = viewModel(factory = stackViewModelFactory),
    sharedPreferencesManager: SharedPreferencesManager,
) {
    val list = stackViewModel.stackList.collectAsState(initial = emptyList())
    val stackList = list.value

    val selectedItems = stackViewModel.selectedItems

    if(stackList.isEmpty()){
        sharedPreferencesManager.saveProgress(0)
    }
    Container(
        stackList, selectedItems,
        startTimer = { playedTime: Int ->
            timerViewModel.startTimer(playedTime)
        },
        stopTimer = { pauseTimer: (Int) -> Unit ->
            timerViewModel.stopTimer(pauseTimer)
        } ,
        totalPlayedTime = { sharedPreferencesManager.getProgress()},
        updateProgress = { playedTime: Int -> sharedPreferencesManager.saveProgress(playedTime) },
        insertStack = { stackData: StackData ->
            stackViewModel.insertStack(stackData) },
        updateStack = { stackData: StackData ->
            stackViewModel.updateStack(stackData) },
        removeStack = { stackData: StackData -> stackViewModel.removeStack(stackData) }
    )
}