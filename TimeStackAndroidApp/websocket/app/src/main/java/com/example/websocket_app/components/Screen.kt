package com.example.websocket_app.components

sealed class Screen (
    val route: String
) {
    object HomeScreen: Screen("home_screen")
    object QRScannerScreen: Screen("qr_scanner_screen")
}
