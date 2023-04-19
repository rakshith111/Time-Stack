package com.example.timestackarchitecture.compose


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.timestackarchitecture.data.StackData
import com.example.timestackarchitecture.viewmodels.*
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BaseScreen(
    stackViewModel: StackViewModel,
    timerViewModel: TimerViewModel
) {
    val stackList = stackViewModel.stackList
    val selectedItems = stackViewModel.selectedItems

    Timber.d("timer: ${timerViewModel.getProgress()}")

    Container(
        stackList, selectedItems,
        getProgress = { timerViewModel.getProgress() },
            updateProgress = { playedTime: Long -> timerViewModel.saveProgress(playedTime)
                         Timber.d("updateProgress: $playedTime") },
        insertStack = { stackData: StackData ->
            stackViewModel.insertStack(stackData) },
        updateStack = { stackData: StackData ->
            stackViewModel.updateStack(stackData) },
        removeStack = {stackData: StackData -> stackViewModel.removeStack(stackData)},
        getStartTime = { timerViewModel.getStartTime() },
        {startTime: Long -> timerViewModel.saveCurrentTime(startTime)},
     { timerViewModel.firstTime() })
        { firstTime: Boolean -> timerViewModel.setFirstTime(firstTime) }
}