package com.example.timestackarchitecture.habitualmode.compose


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import com.example.timestackarchitecture.casualmode.data.StackData
import com.example.timestackarchitecture.casualmode.viewmodels.StackViewModel
import com.example.timestackarchitecture.casualmode.viewmodels.TimerViewModel
import com.example.timestackarchitecture.ui.components.NewAlertDialogBox
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitualBaseScreen(
    stackViewModel: StackViewModel,
    timerViewModel: TimerViewModel
) {
    val stackList = stackViewModel.stackList
    val selectedItems = stackViewModel.selectedItems
    val alertDialogTriggered = remember { mutableStateOf(false) }

    Timber.d("timer: ${timerViewModel.getProgress()}")

    if (timerViewModel.getAlarmTriggered()) {
        alertDialogTriggered.value = true
    }

    if (alertDialogTriggered.value) {
        Dialog(onDismissRequest = {
            alertDialogTriggered.value = false
            timerViewModel.saveAlarmTriggered(false)
        }) {
            NewAlertDialogBox(
                onConfirm = {
                    alertDialogTriggered.value = false
                    timerViewModel.saveAlarmTriggered(false)
                },
                onDismiss = {
                    alertDialogTriggered.value = false
                    timerViewModel.saveAlarmTriggered(false)
                },
                title = "Activity removed",
                text = "Your activity was completed and has now been removed.",
                confirmButton = "OK",
                dismissButton = null,
            )
        }
    }

    Container(
        stackList,
        selectedItems,
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