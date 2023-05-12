package com.example.timestackarchitecture.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun NewAlertDialogBox( onConfirm: () -> Unit, onDismiss: () -> Unit,
    title: String, text: String, confirmButton: String,  dismissButton: String? = null
){
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(confirmButton, style = MaterialTheme.typography.bodyMedium)
            }
        },
        dismissButton = {
            if (dismissButton == null) return@AlertDialog
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(dismissButton, style = MaterialTheme.typography.bodyMedium)
            }
        },
        icon = { Icon(painter = painterResource(id = android.R.drawable.ic_dialog_info), contentDescription = "Warning") }
    )
}
