
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInputDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    activityName: String, onActivityNameChange: (String) -> Unit,
    activityTime: String, onActivityTimeChange: (String) -> Unit){
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
                OutlinedTextField(
                    value = activityTime,
                    onValueChange = onActivityTimeChange,
                    label = { Text("Activity Time") }
                )
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