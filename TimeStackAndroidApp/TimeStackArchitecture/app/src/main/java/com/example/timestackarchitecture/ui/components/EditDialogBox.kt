package com.example.timestackarchitecture.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.timestackarchitecture.casualmode.data.StackData
import timber.log.Timber

@Composable
fun EditDialogBox(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    activityName: String,
    onActivityNameChange: (String) -> Unit,
    onActivityTimeChange: (String) -> Unit,
    selectedItems: MutableList<Int>,
    stackList: List<StackData>
) {
    var timeInMilli = "0"
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = "Edit Activities", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = activityName,
                    onValueChange = onActivityNameChange,
                    label = { Text("Activity Name", style = MaterialTheme.typography.bodyMedium) }
                )
                TimePicker(onTimeSelected = { timeInMilli = it.toString()})
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
                        .padding(8.dp)) {
                    Column {
                        if (selectedItems.size == 0) {
                            Text(
                                text = "You are editing first activity \"${stackList[0].stackName}\"",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
                            )
                        } else {
                            Text(
                                text = "You are editing ${stackList[selectedItems[0]].stackName}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                            )
                        }
                        Text(text = "Changing activity time will reset the timer",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(activityName.isBlank() && (timeInMilli == "0" || timeInMilli == stackList[if(selectedItems.size == 0) {0} else {selectedItems[0]}].stackTime.toString())){
                        onDismiss()
                        Timber.d("1")
                    } else if (activityName.isBlank() && timeInMilli != "0"){
                        onActivityTimeChange(timeInMilli)
                        onConfirm()
                        Timber.d("2")
                    } else if (activityName.isNotBlank() && timeInMilli == "0"){
                        Timber.d("3")
                        onConfirm()
                    } else {
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
                    onDismiss()
                }
            ) {
                Text("Dismiss", style = MaterialTheme.typography.bodyMedium)
            }
        }
    )
}