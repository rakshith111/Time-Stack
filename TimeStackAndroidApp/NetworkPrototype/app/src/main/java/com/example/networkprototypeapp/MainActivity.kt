package com.example.networkprototypeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.networkprototypeapp.presentation.HomeScreen
import com.example.networkprototypeapp.ui.theme.NetworkPrototypeAppTheme
import com.example.networkprototypeapp.viewmodel.ApiViewModel
import com.example.networkprototypeapp.viewmodel.SocketHandler
import io.socket.client.Socket


class MainActivity : ComponentActivity() {
    private lateinit var mSocket: Socket
    private val ApiViewModel by viewModels<ApiViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        val mSocket = SocketHandler.getSocket()


        setContent {
            NetworkPrototypeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(ApiViewModel, mSocket)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
    }
}
