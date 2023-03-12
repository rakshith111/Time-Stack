package com.example.timestackarchitecture


import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timestackarchitecture.compose.BaseScreen
import com.example.timestackarchitecture.service.TimerService
import com.example.timestackarchitecture.ui.theme.TimeStackArchitectureTheme
import com.example.timestackarchitecture.viewmodels.StackViewModelFactory
import com.example.timestackarchitecture.viewmodels.TimerViewModel
import com.example.timestackarchitecture.viewmodels.TimerViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var stackViewModelFactory: StackViewModelFactory

    @Inject
    lateinit var timerViewModelFactory: TimerViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        stopService()
        setContent {
            TimeStackArchitectureTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaseScreen(stackViewModelFactory = stackViewModelFactory, timerViewModelFactory = timerViewModelFactory)
                }
            }
        }
    }

//    override fun onDestroy() {
//        // Start the service
//        startService()
//        super.onDestroy()
//    }
//
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        startService()
        println("paused service started")
        super.onPause()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun startService() {
        // Start the service
        val serviceIntent = Intent(this, TimerService::class.java)
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
        val timerViewModel = ViewModelProvider(
            this,
            timerViewModelFactory
        )[TimerViewModel::class.java]
        startForegroundService(serviceIntent)
    }

    private fun stopService() {
        // Stop the service
        val serviceIntent = Intent(this, TimerService::class.java)
        println("service stopped")
        stopService(serviceIntent)
    }

    override fun onResume() {
        val serviceIntent = Intent(this, TimerService::class.java)
        stopService(serviceIntent)
        super.onResume()
    }

}

