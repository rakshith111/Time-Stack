package com.example.websocket_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class QrViewModel:ViewModel() {
    var qrCode by mutableStateOf("")

}