package com.example.networkprototypeapp.websocketclient
import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class PieSocketListener : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
           webSocket.send("Hello, it's SSaurel !")

        println("onOpen")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        output("Received : $text")
        println("Received : $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        output("Closing : $code / $reason")
    }


    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        output("Error : " + t.message)
        println("Error : " + t.message)
    }

    fun output(text: String?) {
        Log.d("PieSocket", text!!)
        println("PieSocket output: $text")
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}