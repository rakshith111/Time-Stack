package com.example.websocket_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.websocket_app.websocket_app.MyWebSocketClient
import java.net.URI

class QrViewModel:ViewModel() {
    var qrCode by mutableStateOf("")
    var serverUI by mutableStateOf(URI(""))
   companion object {
         var webSocketClient: MyWebSocketClient? = null
   }
}
