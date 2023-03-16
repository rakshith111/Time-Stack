package com.example.timestackarchitecture.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun snackBarMessage(
    message: String,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState
    ) {
    scope.launch {
        snackBarHostState.currentSnackbarData?.dismiss()
        snackBarHostState.showSnackbar(
            message = message,
        )
    }
}