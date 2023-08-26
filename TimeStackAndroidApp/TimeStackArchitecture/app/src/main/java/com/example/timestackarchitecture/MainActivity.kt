package com.example.timestackarchitecture


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.timestackarchitecture.casualmode.data.SharedPreferencesProgressRepository
import com.example.timestackarchitecture.casualmode.service.TimerService
import com.example.timestackarchitecture.casualmode.viewmodels.StackViewModelFactory
import com.example.timestackarchitecture.casualmode.viewmodels.TimerViewModelFactory
import com.example.timestackarchitecture.habitualmode.data.SharedPreferencesProgressRepositoryHabitual
import com.example.timestackarchitecture.habitualmode.viewmodel.HabitualStackViewModel
import com.example.timestackarchitecture.habitualmode.viewmodel.HabitualTimerViewModel
import com.example.timestackarchitecture.navigation.NavGraph
import com.example.timestackarchitecture.ui.components.NewAlertDialogBox
import com.example.timestackarchitecture.ui.theme.TimeStackArchitectureTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity()  {
    @Inject
    lateinit var stackViewModelFactory: StackViewModelFactory

    @Inject
    lateinit var timerViewModelFactory: TimerViewModelFactory

    private val habitualStackViewModel by viewModels<HabitualStackViewModel>()

    private val habitualTimerViewModel by viewModels<HabitualTimerViewModel>()

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

    @OptIn(ExperimentalAnimationApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                val navController = rememberAnimatedNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                when (navBackStackEntry?.destination?.route) {
                    "casualMode" -> {
                        window.statusBarColor = Color(0xFF8EC5FC).toArgb()

                    }
                    "habitualMode" -> {
                        window.statusBarColor = Color(0xFF466B8A).toArgb()
                    }
                    else -> {
                        window.statusBarColor = Color(0xFFa4e9f9).toArgb()
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        NavGraph(
                            navController = navController,
                            stackViewModelFactory = stackViewModelFactory,
                            timerViewModelFactory = timerViewModelFactory,
                            sharedPreferencesProgress = sharedPreferencesProgress,
                            habitualStackViewModel = habitualStackViewModel,
                            habitualTimerViewModel = habitualTimerViewModel
                        )
                    }
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
        val habitualSharedPreferencesProgress = SharedPreferencesProgressRepositoryHabitual(this)

        val elapsed = (System.currentTimeMillis() - sharedPreferencesProgress.getStartTime()) + sharedPreferencesProgress.getTimerProgress()
        sharedPreferencesProgress.saveTimerProgress(elapsed)
        sharedPreferencesProgress.saveCurrentTime(System.currentTimeMillis())
        Timber.d("onDestroy")


        val elapsedHabitual = (System.currentTimeMillis() - habitualSharedPreferencesProgress.getStartTime()) + habitualSharedPreferencesProgress.getTimerProgress()
        habitualSharedPreferencesProgress.saveTimerProgress(elapsedHabitual)
        habitualSharedPreferencesProgress.saveCurrentTime(System.currentTimeMillis())
        super.onDestroy()
    }

    override fun onResume() {
        Timber.d("onResume")
        TimerService.isDeviceActive = true
        TimerService().stopRingtone()
        super.onResume()
    }
}