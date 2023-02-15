import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInputDialog(
                   onConfirm: () -> Unit,
                   onDismiss: () -> Unit){
    var activityName by remember { mutableStateOf("") }
    var activityTime by remember { mutableStateOf("") }
    AlertDialog(

        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button.
            onDismiss()
        },
        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
        title = {
            Text(text = "Title")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = activityName,
                    onValueChange = { activityName = it },
                    label = { Text("Activity Name") }
                )
                OutlinedTextField(
                    value = activityTime,
                    onValueChange = { activityTime = it },
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