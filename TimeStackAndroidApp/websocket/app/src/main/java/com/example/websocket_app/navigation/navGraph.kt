package com.example.websocket_app.navigation

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.websocket_app.components.Screen
import com.example.websocket_app.presentation.HomeScreen
import com.example.websocket_app.presentation.QrScanner
import com.example.websocket_app.viewmodel.QrViewModel
import com.google.common.util.concurrent.ListenableFuture


@Composable
fun NavGraph(
    navController: NavHostController,
    connectionStatus: String,
    hasCamPermission: Boolean,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    lifecycleOwner: LifecycleOwner,
    qrViewModel: QrViewModel,
    ) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route){
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController = navController,
                connectionStatus = connectionStatus,
                qrViewModel = qrViewModel
            )
        }
        composable(route = Screen.QRScannerScreen.route){
            QrScanner(
                hasCamPermission = hasCamPermission,
                cameraProviderFuture = cameraProviderFuture,
                lifecycleOwner = lifecycleOwner,
                qrViewModel = qrViewModel
            )
        }
    }
}