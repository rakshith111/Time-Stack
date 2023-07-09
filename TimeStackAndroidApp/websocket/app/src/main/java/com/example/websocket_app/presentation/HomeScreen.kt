package com.example.websocket_app.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.websocket_app.components.Screen
import com.example.websocket_app.data.TimeData
import com.example.websocket_app.viewmodel.QrViewModel
import com.example.websocket_app.websocket_app.MyWebSocketClient
import com.google.gson.Gson
import kotlinx.coroutines.delay
import org.java_websocket.client.WebSocketClient
import java.net.URI

@Composable
fun HomeScreen(
    navController: NavHostController,
    qrViewModel: QrViewModel,
    sendCode: Boolean
) {
    val serverUri = URI("ws://192.168.0.108:8000")
    val webSocketClient = remember { MyWebSocketClient(serverUri) }
    val context = androidx.compose.ui.platform.LocalContext.current
    var connectionStatus by remember { mutableStateOf("Not connected") }

    LaunchedEffect(webSocketClient) {
        delay(1000)
        println("Connecting to : $serverUri")

        webSocketClient.connectBlocking()
        connectionStatus = when {
            webSocketClient.isOpen -> {
                "Connected"
            }
            webSocketClient.isClosed -> {
                "Connection closed"
            }
            else -> {
                "Connection failed"
            }
        }
        if (webSocketClient.isOpen) {
            val json = Gson().toJson(TimeData("Hello", "Android"))
            webSocketClient.send(json.toString())
            println("Sent message: $json")
        } else {
            Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show()
            webSocketClient.onError(Exception("Connection failed"))
        }
    }

    if (sendCode && webSocketClient.isOpen) {
        try {
            webSocketClient.send(qrViewModel.qrCode)
            println("Sent message: ${qrViewModel.qrCode}")
        } catch (e: Exception) {
            println("Error sending message: ${e.message}")
            Toast.makeText(context, "Error sending message: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Connection Status: $connectionStatus",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate(Screen.QRScannerScreen.route)
        }) {
            Text(text = "Scan QR Code", style = MaterialTheme.typography.bodyLarge)
        }
    }

}