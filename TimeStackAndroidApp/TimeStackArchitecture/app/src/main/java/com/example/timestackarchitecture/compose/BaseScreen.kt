package com.example.timestackarchitecture.compose


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timestackarchitecture.data.StackData
import com.example.timestackarchitecture.viewmodels.*
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BaseScreen(
    stackViewModelFactory: StackViewModelFactory,
    timerViewModelFactory: TimerViewModelFactory,
    stackViewModel: StackViewModel = viewModel(factory = stackViewModelFactory),
    timerViewModel: TimerViewModel = viewModel(factory = timerViewModelFactory),
) {
    val stackList = stackViewModel.stackList
    val selectedItems = stackViewModel.selectedItems


    Timber.d("timer: ${timerViewModel.getTimer()}")

    Container(
        stackList, selectedItems,
        totalPlayedTime = { timerViewModel.getTimer() },
            updateProgress = { playedTime: Int -> timerViewModel.saveTimer(playedTime)
                         Timber.d("updateProgress: $playedTime") },
        insertStack = { stackData: StackData ->
            stackViewModel.insertStack(stackData) },
        updateStack = { stackData: StackData ->
            stackViewModel.updateStack(stackData) }
    ) { stackData: StackData -> stackViewModel.removeStack(stackData) }
}