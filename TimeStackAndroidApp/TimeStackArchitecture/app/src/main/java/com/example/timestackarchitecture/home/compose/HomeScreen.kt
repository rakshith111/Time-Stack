package com.example.timestackarchitecture.home.compose

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.timestackarchitecture.casualmode.data.SharedPreferencesProgressRepository
import com.example.timestackarchitecture.casualmode.service.TimerService
import com.example.timestackarchitecture.casualmode.viewmodels.StackViewModel
import com.example.timestackarchitecture.casualmode.viewmodels.StackViewModelFactory
import com.example.timestackarchitecture.casualmode.viewmodels.TimerViewModel
import com.example.timestackarchitecture.casualmode.viewmodels.TimerViewModelFactory
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    stackViewModelFactory: StackViewModelFactory,
    timerViewModelFactory: TimerViewModelFactory,
    sharedPreferencesProgress: SharedPreferencesProgressRepository,
    stackViewModel: StackViewModel = viewModel(factory = stackViewModelFactory),
    timerViewModel: TimerViewModel = viewModel(factory = timerViewModelFactory)
) {
    val context = LocalContext.current

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
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF8EC5FC),
            Color(0xFFE0C3FC)
        )
    )

    Scaffold(modifier = Modifier.fillMaxSize()) {
        println(it)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundGradient),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                onClick = {
                    navController.navigate("casualMode") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(0.8f),
                elevation = CardDefaults.cardElevation(8.dp),

            ) {
                Text(
                    text = "Casual Mode",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Card(
                onClick = {
                    navController.navigate("habitualMode") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(0.8f),
                elevation = CardDefaults.cardElevation(8.dp),
            ) {
                Text(
                    text = "Habitual Mode",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

