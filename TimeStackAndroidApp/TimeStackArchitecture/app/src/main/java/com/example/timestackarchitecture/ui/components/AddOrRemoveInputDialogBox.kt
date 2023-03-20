package com.example.timestackarchitecture.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun AddInputDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    activityName: String, onActivityNameChange: (String) -> Unit, onActivityTimeChange: (String) -> Unit){
    var timeInMilli = "0"
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = "New Activity")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = activityName,
                    onValueChange = onActivityNameChange,
                    label = { Text("Activity Name") }
                )
                TimePicker(onTimeSelected = { timeInMilli = it.toString()})
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(timeInMilli == "0"){
                        onDismiss()
                    } else {
                        onActivityTimeChange(timeInMilli)
                        onConfirm()
                    }

                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun RemoveInputDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    selectedItems: MutableList<Int>
){
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            if (selectedItems.size == 1 || selectedItems.size == 0) {
                Text(text = "Remove top activity?")
            } else {
                Text(text = "Remove ${selectedItems.size} activities?")
            }

        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}