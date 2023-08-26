package com.example.timestackarchitecture

import android.app.Application
import androidx.room.Room
import com.example.timestackarchitecture.casualmode.service.CreateNotificationChannel
import com.example.timestackarchitecture.habitualmode.data.HabitualStackDatabase
import dagger.hilt.android.HiltAndroidApp
@HiltAndroidApp
class StackApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        CreateNotificationChannel(this).createNotificationChannel()
    }
}
