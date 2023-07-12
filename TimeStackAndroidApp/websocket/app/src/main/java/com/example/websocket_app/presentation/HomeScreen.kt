package com.example.websocket_app.presentation

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.websocket_app.components.Screen
import com.example.websocket_app.data.TimeData
import com.example.websocket_app.viewmodel.QrViewModel
import com.example.websocket_app.websocket_app.MyWebSocketClient
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.net.URI


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(
    navController: NavHostController,
    qrViewModel: QrViewModel,
    sendCode: Boolean,
) {
    val serverUri = "ij"
    val context = LocalContext.current
    var connectionStatus by remember { mutableStateOf("Not Connected") }
    var buttonClicked by remember { mutableStateOf(true) }
    Timber.d("HomeScreen: sendCode: ${qrViewModel.qrCode} $sendCode")


    if (sendCode) {
        CoroutineScope(Dispatchers.IO).launch {
            connectToQrServer(qrViewModel){
                connectionStatus = it
            }
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Connection Status: $connectionStatus",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                connectToServer(serverUri, context){
                    connectionStatus = it
                }
            }
            buttonClicked = false
        }, enabled = buttonClicked) {
            Text(text = "Test (Connect to Server)", style = MaterialTheme.typography.bodyLarge)
        }

        Button(onClick = {
            navController.navigate(Screen.QRScannerScreen.route)

        }) {
            Text(text = "Scan QR Code", style = MaterialTheme.typography.bodyLarge)
        }

    }
}

suspend fun connectToQrServer(qrViewModel: QrViewModel, connectionStatus: (String) -> Unit) {
    try {
        val (Secret, IP, Port) = JSONObject(qrViewModel.qrCode).run {
            Triple(getString("Secret"), getString("IP"), getString("Port"))
        }
        val secretJsonObject = JSONObject().apply {
            put("challenge_code", Secret)
        }.toString()
        val serverUri = "ws://$IP:$Port"
        qrViewModel.serverUI = URI(serverUri)
        Timber.d("Connecting to : $serverUri")
        val webSocketClient = MyWebSocketClient.getInstance(qrViewModel.serverUI)
        webSocketClient.connect()
        delay(1000)
        if (webSocketClient.isOpen) {
            webSocketClient.send(secretJsonObject)
            Timber.d("Sent message: ${qrViewModel.qrCode}")
            connectionStatus("Connected")
        } else {
            Timber.e("Error sending message: WebSocket is not open")
        }
    } catch (e: Exception) {
        Timber.e("Error sending message: ${e.message}")
    }
}

suspend fun connectToServer(
    serverUri: String,
    context: Context,
    connectionStatus: (String) -> Unit
) {
    try {
        val webSocketClient = MyWebSocketClient.getInstance(URI(serverUri))
        Timber.d("Connecting to : $serverUri")
        webSocketClient.connect()
        delay(1000) // give some time for the connection to establish

        when {
            webSocketClient.isOpen -> {
                connectionStatus("Connected")
            }
            webSocketClient.isClosed -> {
                connectionStatus("Closed")
            }
            else -> {
                connectionStatus("Error")
            }
        }
        if (webSocketClient.isOpen) {
            val json = Gson().toJson(TimeData("Hello", "Android"))
            webSocketClient.send(json.toString())
            Timber.d("Sent message: $json")
        } else {
            webSocketClient.onError(Exception("Connection failed"))
            Timber.d("Connecting to server failed")
        }
    }
    catch (
        e: Exception
    ) {
        Timber.e("Error sending message: ${e.message}")
        connectionStatus("Error")
    }


}
