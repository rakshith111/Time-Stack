package com.example.websocket_app

import android.app.Application
import timber.log.Timber

class NetworkPrototype : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}