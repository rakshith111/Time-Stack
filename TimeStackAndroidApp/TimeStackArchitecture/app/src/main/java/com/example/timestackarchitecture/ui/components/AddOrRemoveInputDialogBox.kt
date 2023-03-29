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
            Text(text = "New Activity", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = activityName,
                    onValueChange = onActivityNameChange,
                    label = { Text("Activity Name", style = MaterialTheme.typography.bodyLarge) }
                )
                TimePicker(onTimeSelected = { timeInMilli = it.toString()})
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(timeInMilli == "0" || activityName == ""){
                        onDismiss()
                    } else {
                        onActivityTimeChange(timeInMilli)
                        onConfirm()
                    }

                }
            ) {
                Text("Confirm", style = MaterialTheme.typography.bodyLarge)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Dismiss", style = MaterialTheme.typography.bodyLarge)
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
                Text(text = "Remove top activity?", style = MaterialTheme.typography.titleLarge)
            } else {
                Text(text = "Remove ${selectedItems.size} activities?", style = MaterialTheme.typography.titleLarge)
            }

        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Confirm", style = MaterialTheme.typography.bodyLarge)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Dismiss", style = MaterialTheme.typography.bodyLarge)
            }
        }
    )
}