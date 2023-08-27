package com.example.timestackarchitecture.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.timestackarchitecture.casualmode.data.StackData
import com.example.timestackarchitecture.habitualmode.data.HabitualStackData

@Composable
fun AddInputDialog(
    onConfirm: () -> Unit,
    onDismiss: (message : String) -> Unit,
    activityName: String, onActivityNameChange: (String) -> Unit, onActivityTimeChange: (String) -> Unit){
    var timeInMilli = "0"
    AlertDialog(
        onDismissRequest = {
            onDismiss("Dismiss")
        },
        title = {
            Text(text = "New Activity", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = activityName,
                    onValueChange = onActivityNameChange,
                    label = { Text("Activity Name", style = MaterialTheme.typography.bodyMedium) }
                )
                TimePicker(onTimeSelected = { timeInMilli = it.toString()})
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(timeInMilli == "0"){
                        onDismiss("0")
                    } else if (activityName.isBlank()){
                        onDismiss("Blank")
                    }else {
                        onActivityTimeChange(timeInMilli)
                        onConfirm()
                    }

                }
            ) {
                Text("Confirm", style = MaterialTheme.typography.bodyMedium)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss("Dismiss")
                }
            ) {
                Text("Dismiss", style = MaterialTheme.typography.bodyMedium)
            }
        }
    )
}

@Composable
fun RemoveInputDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    selectedItems: MutableList<Int>,
    stackList: List<StackData>,
    ){

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            when (selectedItems.size) {
                1 -> {
                    Text(text = "Remove ${stackList[selectedItems[0]].stackName} activity?",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                0 -> {
                    Text(text = "Remove top activity?", style = MaterialTheme.typography.titleLarge)
                }
                else -> {
                    Text(text = "Remove ${selectedItems.size} activities?", style = MaterialTheme.typography.titleLarge)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Confirm", style = MaterialTheme.typography.bodyMedium)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Dismiss", style = MaterialTheme.typography.bodyMedium)
            }
        }
    )
}


@Composable
fun RemoveInputDialogHabitual(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    selectedItems: MutableList<Int>,
    stackList: List<HabitualStackData>,
){
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            when (selectedItems.size) {
                1 -> {
                    Text(text = "Remove ${stackList[selectedItems[0]].stackName} activity?",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                0 -> {
                    Text(text = "Remove top activity?", style = MaterialTheme.typography.titleLarge)
                }
                else -> {
                    Text(text = "Remove ${selectedItems.size} activities?", style = MaterialTheme.typography.titleLarge)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Confirm", style = MaterialTheme.typography.bodyMedium)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Dismiss", style = MaterialTheme.typography.bodyMedium)
            }
        }
    )
}

