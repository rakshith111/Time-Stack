package com.example.timestackarchitecture


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timestackarchitecture.casualmode.compose.CasualBaseScreen
import com.example.timestackarchitecture.casualmode.data.SharedPreferencesProgressRepository
import com.example.timestackarchitecture.casualmode.service.TimerService
import com.example.timestackarchitecture.casualmode.viewmodels.StackViewModelFactory
import com.example.timestackarchitecture.casualmode.viewmodels.TimerViewModelFactory
import com.example.timestackarchitecture.habitualmode.compose.HabitualBaseScreen
import com.example.timestackarchitecture.home.compose.HomeScreen
import com.example.timestackarchitecture.ui.components.NewAlertDialogBox
import com.example.timestackarchitecture.ui.theme.TimeStackArchitectureTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
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

                val navController = rememberAnimatedNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = "home",
                    ) {
                        composable("home",
                            enterTransition = {
                            fadeIn(animationSpec = tween(300))},
                            exitTransition = {
                                fadeOut(animationSpec = tween(300))
                            }){
                            HomeScreen(navController, stackViewModelFactory, timerViewModelFactory, sharedPreferencesProgress)
                            Timber.d("back to home")
                        }
                        composable("casualMode",
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { 1000 },
                                    animationSpec = tween(500)
                                )},
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { -1000 },
                                    animationSpec = tween(500)
                                )}){
                            CasualBaseScreen(stackViewModel = viewModel(factory = stackViewModelFactory), timerViewModel = viewModel(factory = timerViewModelFactory))
                        }

                        composable("habitualMode",
                            enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { 1000 },
                                animationSpec = tween(500)
                            )},
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { -1000 },
                            animationSpec = tween(500)
                        )}) {

                            HabitualBaseScreen()
                        }
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