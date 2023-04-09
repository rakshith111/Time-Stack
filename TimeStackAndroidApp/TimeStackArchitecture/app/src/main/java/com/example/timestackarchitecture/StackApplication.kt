package com.example.timestackarchitecture

import android.app.Application
import com.example.timestackarchitecture.service.CreateNotificationChannel
import dagger.hilt.android.HiltAndroidApp
@HiltAndroidApp
class StackApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        CreateNotificationChannel(this).createNotificationChannel()
    }
}
