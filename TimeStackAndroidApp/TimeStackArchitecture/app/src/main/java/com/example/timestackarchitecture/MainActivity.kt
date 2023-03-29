package com.example.timestackarchitecture


import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.timestackarchitecture.compose.BaseScreen
import com.example.timestackarchitecture.service.TimerService
import com.example.timestackarchitecture.ui.theme.TimeStackArchitectureTheme
import com.example.timestackarchitecture.viewmodels.StackViewModel
import com.example.timestackarchitecture.viewmodels.StackViewModelFactory
import com.example.timestackarchitecture.viewmodels.TimerViewModel
import com.example.timestackarchitecture.viewmodels.TimerViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var stackViewModelFactory: StackViewModelFactory

    @Inject
    lateinit var timerViewModelFactory: TimerViewModelFactory

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted.
                Toast.makeText(
                    this,
                    "Notification permission granted.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                Toast.makeText(
                    this,
                    "Notification permission is required to use this app.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        TimerService.isAppInForeground = true
        stopService("onCreate")

        setContent {
            TimeStackArchitectureTheme {
                val context = LocalContext.current

                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // You can use the API that requires the permission.
                    }
                    shouldShowRequestPermissionRationale(
                        Manifest.permission.POST_NOTIFICATIONS
                    ) -> {
                        AlertDialog.Builder(context)
                            .setTitle("Permission required")
                            .setMessage("This permission is required to show notifications.")
                            .setPositiveButton("OK") { _, _ ->
                                // Request the permission
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    requestPermissionLauncher.launch(
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                }
                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                // Dismiss the dialog and do nothing
                                dialog.dismiss()
                            }
                            .show()
                    }
                    else ->   {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermissionLauncher.launch(
                                Manifest.permission.POST_NOTIFICATIONS

                            )
                        }
                    }
                }


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
                startService(stackViewModel.stackList[0].stackTime, stackViewModel.stackList[0].stackName)
                Timber.d("service started")
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
            startService(stackViewModel.stackList[0].stackTime, stackViewModel.stackList[0].stackName)
            Timber.d("service started on pause")
        }
    }
        TimerService.isAppInForeground = false
        super.onPause()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startService(duration: Long, stackName: String) {
        // Start the service
        val serviceIntent = Intent(this, TimerService::class.java)
        serviceIntent.putExtra("duration", duration)
        serviceIntent.putExtra("stackName", stackName)
        startForegroundService(serviceIntent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun stopService(LifecycleEvent: String) {
        val stackViewModel: StackViewModel =
            ViewModelProvider(this, stackViewModelFactory)[StackViewModel::class.java]
        val timerViewModel: TimerViewModel =
            ViewModelProvider(this, timerViewModelFactory)[TimerViewModel::class.java]
        if (stackViewModel.stackList.isNotEmpty()) {
            if (stackViewModel.stackList[0].isPlaying) {
                timerViewModel.stopTimer { pauseTime ->
                    timerViewModel.saveProgress(pauseTime)
                    Timber.d("view started service stopped $pauseTime")
                }

                timerViewModel.startTimer(timerViewModel.getProgress(), stackViewModel.stackList[0].stackTime.toInt())
                Timber.d("service started $LifecycleEvent")

                val serviceIntent = Intent(this, TimerService::class.java)
                stopService(serviceIntent)
                TimerService().stopProgressNotificationThread(LifecycleEvent)
            }
        }
    }
//
//
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
    stopService("onResume")
    TimerService.isAppInForeground = true
    super.onResume() }
}

