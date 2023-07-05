package com.example.websocket_app.websocket_app

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class MyWebSocketClient(serverUri: URI) : WebSocketClient(serverUri) {
    override fun onOpen(handshakedata: ServerHandshake?) {
        println("Connected to WebSocket server")
        Log.d("WebSocket onOpen", "Connected to WebSocket server")
        Log.d("WebSocket onOpen", "Server Address: ${uri.host}:${uri.port}")
        Log.d("WebSocket onOpen", "Local Address: ${localSocketAddress?.hostName}:${localSocketAddress?.port}")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("Disconnected from WebSocket server")
        Log.d("WebSocket onClose", "Connected to WebSocket server")
        Log.d("WebSocket onClose", "Server Address: ${uri.host}:${uri.port}")
        Log.d("WebSocket onClose", "Local Address: ${localSocketAddress?.hostName}:${localSocketAddress?.port}")
    }

    override fun onError(ex: Exception?) {
        println("Error: ${ex?.message}")
        Log.d("WebSocket onError", "Connected to WebSocket server")
        Log.d("WebSocket onError", "Server Address: ${uri.host}:${uri.port}")
        Log.d("WebSocket onError", "Local Address: ${localSocketAddress?.hostName}:${localSocketAddress?.port}")
    }

    override fun onMessage(message: String?) {
        println("Received message: $message")
        Log.d("WebSocket onMessage", "Connected to WebSocket server")
        Log.d("WebSocket onMessage", "Server Address: ${uri.host}:${uri.port}")
        Log.d("WebSocket onMessage", "Local Address: ${localSocketAddress?.hostName}:${localSocketAddress?.port}")
    }
}
