package com.example.websocket_app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner

import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.websocket_app.components.Screen
import com.example.websocket_app.data.TimeData
import com.example.websocket_app.navigation.NavGraph
import com.example.websocket_app.ui.theme.Websocket_appTheme
import com.example.websocket_app.viewmodel.QrViewModel
import com.example.websocket_app.websocket_app.MyWebSocketClient
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.net.URI

class MainActivity : ComponentActivity() {
    private lateinit var webSocketClient: MyWebSocketClient
    private var connectionStatus by mutableStateOf("")
    private lateinit var navController : NavHostController
    private val qrViewModel by viewModels<QrViewModel> ()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()

            // Set ip here >>>>>>>>>>>>>>>>>>>>
            val serverUri = URI("ws://192.168.0.108:8000")
            webSocketClient = MyWebSocketClient(serverUri)

            LaunchedEffect(Unit) {
                webSocketClient.connect()
                connectionStatus = "Connecting..."
                delay(1000) // Optional delay for the connection to establish
                if (webSocketClient.connection.isOpen) {
                    connectionStatus = "Connected"
                    val json = Gson().toJson(TimeData("Hello", "Android"))
                    webSocketClient.send(json.toString())
                    println("Sent message: $json")
                } else {
                    connectionStatus = "Connection failed"
                }
            }
            Websocket_appTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val lifecycleOwner = LocalLifecycleOwner.current
                    val cameraProviderFuture = remember {
                        ProcessCameraProvider.getInstance(context)
                    }
                    var hasCamPermission by remember {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        )
                    }
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { granted ->
                            hasCamPermission = granted
                        }
                    )
                    LaunchedEffect(key1 = true) {
                        launcher.launch(Manifest.permission.CAMERA)
                    }
                    NavGraph(navController = navController,
                        connectionStatus = connectionStatus,
                        hasCamPermission = true,
                        cameraProviderFuture = cameraProviderFuture,
                        lifecycleOwner = lifecycleOwner,
                        qrViewModel = qrViewModel,
                    )
                    navController.navigate(Screen.HomeScreen.route){
                    }
                }
            }
        }
    }
}



