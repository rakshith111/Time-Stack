package com.example.timestackarchitecture


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.timestackarchitecture.compose.BaseScreen
import com.example.timestackarchitecture.data.SharedPreferencesProgressRepository
import com.example.timestackarchitecture.service.TimerService
import com.example.timestackarchitecture.ui.components.NewAlertDialogBox
import com.example.timestackarchitecture.ui.theme.TimeStackArchitectureTheme
import com.example.timestackarchitecture.viewmodels.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity()  {

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
        Timber.d("onCreate")
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }


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
                        NewAlertDialogBox(
                            onConfirm = { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                requestPermissionLauncher.launch(
                                    Manifest.permission.POST_NOTIFICATIONS
                                )
                            }},
                            onDismiss = { /*do nothing*/ },
                            title = "Permission required",
                            text = "This permission is required to show notifications.",
                            confirmButton = "OK",
                            dismissButton = "Cancel"
                        )
                    }
                    else -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermissionLauncher.launch(
                                Manifest.permission.POST_NOTIFICATIONS

                            )
                        }
                    }
                }
                val sharedPreferencesProgress =  SharedPreferencesProgressRepository(this)
                val stackViewModel: StackViewModel by viewModels {
                    stackViewModelFactory
                }
                val timerViewModel: TimerViewModel by viewModels {
                    timerViewModelFactory
                }


                if(sharedPreferencesProgress.firstTime()) {
                    Timber.d("firstTime")
                    sharedPreferencesProgress.saveTimerProgress(0)
                } else {
                    if(stackViewModel.stackList.isNotEmpty()) {
                        if(stackViewModel.stackList[0].isPlaying) {
                            val elapsed = (System.currentTimeMillis() - sharedPreferencesProgress.getStartTime()) + sharedPreferencesProgress.getTimerProgress()
                            sharedPreferencesProgress.saveTimerProgress(elapsed)
                            sharedPreferencesProgress.saveCurrentTime(System.currentTimeMillis())
                        }
                    }
                }
                if(timerViewModel.getAlarmTriggered()) {
                    if (stackViewModel.stackList.isNotEmpty()) {
                        stackViewModel.removeStack(stackViewModel.stackList[0])
                        Timber.d("removeStack 0 in MainActivity")
                        timerViewModel.saveProgress(0)
                        timerViewModel.saveAlarmTriggered(false)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val serviceIntent =
                                Intent(context, TimerService::class.java)
                            context.stopService(serviceIntent)
                        }

                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    BaseScreen(stackViewModel = stackViewModel, timerViewModel = timerViewModel)
                }
            }
        }
    }

    override fun onPause() {
        Timber.d("onPause")
        TimerService.isDeviceActive = false
        super.onPause()
    }
    override fun onDestroy() {
        val sharedPreferencesProgress =  SharedPreferencesProgressRepository(this)

        val elapsed = (System.currentTimeMillis() - sharedPreferencesProgress.getStartTime()) + sharedPreferencesProgress.getTimerProgress()
        sharedPreferencesProgress.saveTimerProgress(elapsed)
        sharedPreferencesProgress.saveCurrentTime(System.currentTimeMillis())
        Timber.d("onDestroy")
        super.onDestroy()
    }

    override fun onResume() {
        Timber.d("onResume")
        TimerService.isDeviceActive = true
        TimerService().stopRingtone()
        super.onResume()
    }

}




