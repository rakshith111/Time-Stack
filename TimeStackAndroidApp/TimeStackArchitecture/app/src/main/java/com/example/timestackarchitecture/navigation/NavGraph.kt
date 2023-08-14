package com.example.timestackarchitecture.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.timestackarchitecture.casualmode.compose.CasualBaseScreen
import com.example.timestackarchitecture.casualmode.data.SharedPreferencesProgressRepository
import com.example.timestackarchitecture.casualmode.viewmodels.StackViewModelFactory
import com.example.timestackarchitecture.casualmode.viewmodels.TimerViewModelFactory
import com.example.timestackarchitecture.habitualmode.compose.HabitualBaseScreen
import com.example.timestackarchitecture.home.compose.HomeScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import timber.log.Timber
@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun NavGraph(
    navController: NavHostController,
    stackViewModelFactory: StackViewModelFactory,
    timerViewModelFactory: TimerViewModelFactory,
    sharedPreferencesProgress: SharedPreferencesProgressRepository

) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable("home") {
            HomeScreen(
                navController,
                stackViewModelFactory,
                timerViewModelFactory,
                sharedPreferencesProgress
            )
            Timber.d("back to home")
        }
        composable("casualMode",
            enterTransition = {
                fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(500))
            }
        ) {
            CasualBaseScreen(
                stackViewModel = viewModel(factory = stackViewModelFactory),
                timerViewModel = viewModel(factory = timerViewModelFactory)
            )
        }

        composable("habitualMode",
            enterTransition = {
                fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(500))
            }) {
            HabitualBaseScreen(
                stackViewModel = viewModel(factory = stackViewModelFactory),
                timerViewModel = viewModel(factory = timerViewModelFactory)
            )
        }
    }
}