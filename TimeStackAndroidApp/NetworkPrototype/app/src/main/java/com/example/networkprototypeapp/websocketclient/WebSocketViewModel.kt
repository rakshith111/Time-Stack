package com.example.networkprototypeapp.websocketclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import okhttp3.*
import okio.ByteString

class WebSocketClient(private val url: String) {
    private var webSocket: WebSocket? = null

    fun connect() {
        val request = Request.Builder().url(url).build()
        val webSocketListener = MyWebSocketListener()
        val okHttpClient = OkHttpClient()

        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
    }

    fun disconnect() {
        webSocket?.cancel()
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    inner class MyWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            println("WebSocket connected")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            println("Received message: $text")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            println("WebSocket closed")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            println("WebSocket failure: ${t.message}")
        }
    }
}
