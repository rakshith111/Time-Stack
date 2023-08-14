package com.example.timestackarchitecture

import android.app.Application
import com.example.timestackarchitecture.casualmode.service.CreateNotificationChannel
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class StackApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        CreateNotificationChannel(this).createNotificationChannel()
        Timber.plant(Timber.DebugTree())
    }
}
