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
                startService()
                println("service started")
            }
        }
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
            startService()
            println("service started")
        }
    }
        super.onPause()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startService() {
        // Start the service
        val serviceIntent = Intent(this, TimerService::class.java)
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
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
                }
                startService()
                println("service started")
            }
            val serviceIntent = Intent(this, TimerService::class.java)
            println("service stopped")
            stopService(serviceIntent)
        }
    }
//
//
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
    stopService()
    super.onResume()
}

}

