package com.example.websocket_app.websocket_app


import com.example.websocket_app.data.TimeData
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import timber.log.Timber
import java.net.URI

class MyWebSocketClient(serverUri: URI) : WebSocketClient(serverUri) {
    override fun onOpen(handshakedata: ServerHandshake?) {
        println("Connected to WebSocket server")
        Timber.d("WebSocket onOpen", "Connected to WebSocket server")
        Timber.d("WebSocket onOpen", "Server Address: ${uri.host}:${uri.port}")
        Timber.d(
            "WebSocket onOpen",
            "Local Address: ${localSocketAddress?.hostName}:${localSocketAddress?.port}"
        )
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("Disconnected from WebSocket server")
        Timber.d("WebSocket onClose", "Connected to WebSocket server")
        Timber.d("WebSocket onClose", "Server Address: ${uri.host}:${uri.port}")
        Timber.d(
            "WebSocket onClose",
            "Local Address: ${localSocketAddress?.hostName}:${localSocketAddress?.port}"
        )
    }

    override fun onError(ex: Exception?) {
        Timber.d("Error: ${ex?.message}")
        Timber.d("Not Connected to WebSocket server")
        Timber.d("Server Address: ${uri.host}:${uri.port}")
        Timber.d("Local Address: ${localSocketAddress?.hostName}:${localSocketAddress?.port}")
    }

    override fun onMessage(message: String?) {
        println("Received message: $message")
        Timber.d("WebSocket onMessage", "Connected to WebSocket server")
        Timber.d("WebSocket onMessage", "Server Address: ${uri.host}:${uri.port}")
        Timber.d(
            "WebSocket onMessage",
            "Local Address: ${localSocketAddress?.hostName}:${localSocketAddress?.port}"
        )
    }

    companion object {
        private var instance: MyWebSocketClient? = null
        fun getInstance(serverUri: URI): MyWebSocketClient {
            if (instance == null) {
                instance = MyWebSocketClient(serverUri)
            }
            return instance!!
        }
    }
}