package com.example.timestackarchitecture.habitualmode.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import com.example.timestackarchitecture.habitualmode.data.HabitualStackData
import com.example.timestackarchitecture.habitualmode.viewmodel.HabitualStackViewModel
import com.example.timestackarchitecture.habitualmode.viewmodel.HabitualTimerViewModel
import com.example.timestackarchitecture.ui.components.NewAlertDialogBox
import timber.log.Timber

@Composable
fun HabitualBaseScreen(
    habitualStackViewModel: HabitualStackViewModel,
    habitualTimerViewModel: HabitualTimerViewModel
) {
    val selectedItems = habitualStackViewModel.selectedItems
    val alertDialogTriggered = remember { mutableStateOf(false) }

    Timber.d("timer: ${habitualTimerViewModel.getProgress()}")

    if (habitualTimerViewModel.getAlarmTriggered()) {
        alertDialogTriggered.value = true
    }

    if (alertDialogTriggered.value) {
        Dialog(onDismissRequest = {
            alertDialogTriggered.value = false
            habitualTimerViewModel.saveAlarmTriggered(false)
        }) {
            NewAlertDialogBox(
                onConfirm = {
                    alertDialogTriggered.value = false
                    habitualTimerViewModel.saveAlarmTriggered(false)
                },
                onDismiss = {
                    alertDialogTriggered.value = false
                    habitualTimerViewModel.saveAlarmTriggered(false)
                },
                title = "Activity removed",
                text = "Your activity was completed and has now been removed.",
                confirmButton = "OK",
                dismissButton = null,
            )
        }
    }

    HabitualContainer(
        habitualStackViewModel,
        selectedItems,
        getProgress = { habitualTimerViewModel.getProgress() },

        updateProgress = { playedTime: Long -> habitualTimerViewModel.saveProgress(playedTime)
            Timber.d("updateProgress: $playedTime") },

        insertStack = { habitualStackData: HabitualStackData ->
            habitualStackViewModel.insertStack(habitualStackData) },

        updateStack = { stackData: HabitualStackData ->
            habitualStackViewModel.updateStack(stackData) },

        removeStack = { habitualStackData: HabitualStackData ->
            habitualStackViewModel.removeStack(habitualStackData)},

        getStartTime = { habitualTimerViewModel.getStartTime() },

        {startTime: Long -> habitualTimerViewModel.saveCurrentTime(startTime)},

        { habitualTimerViewModel.firstTime() })

    { firstTime: Boolean -> habitualTimerViewModel.setFirstTime(firstTime) }

}
