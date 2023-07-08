package com.example.websocket_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.websocket_app.data.TimeData
import com.example.websocket_app.ui.theme.Websocket_appTheme
import com.example.websocket_app.websocket_app.MyWebSocketClient
import com.google.gson.Gson
import kotlinx.coroutines.delay
import okhttp3.*
import kotlinx.coroutines.launch
import java.net.URI
import okio.ByteString

class MainActivity : ComponentActivity() {
    private lateinit var webSocketClient: MyWebSocketClient
    private var connectionStatus by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        val json = Gson().toJson(TimeData("Hello", "Android"))
        json.encodeToByteArray()
        super.onCreate(savedInstanceState)
        setContent {
            // Set ip here >>>>>>>>>>>>>>>>>>>>
    val serverUri = URI("ws://192.168.1.3:8888")
            webSocketClient = MyWebSocketClient(serverUri)

            LaunchedEffect(Unit) {
                webSocketClient.connect()
                connectionStatus = "Connecting..."
                delay(1000) // Optional delay for the connection to establish
                if (webSocketClient.connection.isOpen) {
                    connectionStatus = "Connected"
                    webSocketClient.send("HOOOOOOO")
                } else {
                    connectionStatus = "Connection failed"
                }
            }


            Websocket_appTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Connection Status: $connectionStatus", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Greeting("Android")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Websocket_appTheme {
        Greeting("Android")
    }
}