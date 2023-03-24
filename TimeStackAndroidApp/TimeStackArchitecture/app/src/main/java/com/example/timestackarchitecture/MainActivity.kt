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
import androidx.lifecycle.ViewModelProvider
import com.example.timestackarchitecture.compose.BaseScreen
import com.example.timestackarchitecture.service.TimerService
import com.example.timestackarchitecture.ui.theme.TimeStackArchitectureTheme
import com.example.timestackarchitecture.viewmodels.StackViewModel
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        TimerService.isAppInForeground = true
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        val stackViewModel: StackViewModel = ViewModelProvider(this,stackViewModelFactory )[StackViewModel::class.java]
        val timerViewModel: TimerViewModel = ViewModelProvider(this,timerViewModelFactory )[TimerViewModel::class.java]
        // Start the service
        if(stackViewModel.stackList.isNotEmpty()){
            if (stackViewModel.stackList[0].isPlaying) {
                timerViewModel.stopTimer { pauseTime ->
                    timerViewModel.saveProgress(pauseTime)
                }
                startService(stackViewModel.stackList[0].stackTime)
                println("service started")
            }
        }
        TimerService.isAppInForeground = false
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
    val stackViewModel: StackViewModel = ViewModelProvider(this,stackViewModelFactory )[StackViewModel::class.java]
    val timerViewModel: TimerViewModel = ViewModelProvider(this,timerViewModelFactory )[TimerViewModel::class.java]
    // Start the service
    if(stackViewModel.stackList.isNotEmpty()){
        if (stackViewModel.stackList[0].isPlaying) {
            timerViewModel.stopTimer { pauseTime ->
                timerViewModel.saveProgress(pauseTime)
            }
            startService(stackViewModel.stackList[0].stackTime)
            println("service started")
        }
    }
        TimerService.isAppInForeground = false
        super.onPause()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startService(duration: Long) {
        // Start the service
        val serviceIntent = Intent(this, TimerService::class.java)
        serviceIntent.putExtra("duration", duration)
        startForegroundService(serviceIntent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun stopService() {
        val stackViewModel: StackViewModel =
            ViewModelProvider(this, stackViewModelFactory)[StackViewModel::class.java]
        val timerViewModel: TimerViewModel =
            ViewModelProvider(this, timerViewModelFactory)[TimerViewModel::class.java]
        if (stackViewModel.stackList.isNotEmpty()) {
            if (stackViewModel.stackList[0].isPlaying) {

                timerViewModel.stopTimer { pauseTime ->
                    timerViewModel.saveProgress(pauseTime)
                    println("view started service stopped $pauseTime")
                }
                timerViewModel.startTimer(timerViewModel.getProgress(), stackViewModel.stackList[0].stackTime.toInt())
                println("service started")
                val serviceIntent = Intent(this, TimerService::class.java)
                stopService(serviceIntent)
                TimerService().stopProgressNotificationThread()
            }
        }

    }
//
//
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
    stopService()
    TimerService.isAppInForeground = true
    super.onResume() }
}

