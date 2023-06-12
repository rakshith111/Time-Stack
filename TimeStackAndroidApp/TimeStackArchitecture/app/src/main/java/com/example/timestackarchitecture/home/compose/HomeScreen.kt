package com.example.timestackarchitecture.home.compose

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.timestackarchitecture.R
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

    val glassBackgroundShape: Shape = MaterialTheme.shapes.medium

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF8EC5FC),
            Color(0xFFE0C3FC)
        )
    )

    val glassBackgroundModifier = Modifier
        .fillMaxWidth()
        .aspectRatio(2f)
        .clip(glassBackgroundShape)
        .background(Color.Transparent)
        .then(Modifier.drawBehind {
            drawRoundRect(
                color = Color.White.copy(alpha = 0.1f),
                blendMode = BlendMode.Xor,
            )
        })

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


    Scaffold(modifier = Modifier.fillMaxSize()) {
        println(it)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundGradient),
        ) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(top = 70.dp)){
                Spacer(modifier = Modifier.weight(1f))
                Image(painter = painterResource(
                    id = R.drawable.ic_stack_noti),
                    contentDescription = "brand logo",
                    modifier = Modifier
                        .size(50.dp)

                )
                Text(text = " TIME STACK",
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.lato_regular))
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.height(50.dp))
            IconButton(
                onClick = {
                    navController.navigate("casualMode") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .padding(20.dp)
                    .aspectRatio(2f)
                    .then(glassBackgroundModifier),
            ) {
                Row{
                    Image(
                        painter = painterResource(id = R.drawable.causal),
                        contentDescription = "causal",
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(start = 20.dp),
                    )

                    Text(
                        text = "Casual Mode",
                        color = Color(0x6FFFFFFF),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 20.dp, end = 10.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            IconButton(
                onClick = {
                    navController.navigate("habitualMode") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .padding(20.dp)
                    .aspectRatio(2f)
                    .then(glassBackgroundModifier),
            ) {
                Row{
                    Image(
                        painter = painterResource(id = R.drawable.habitual),
                        contentDescription = "habitual",
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(start = 20.dp),
                    )
                    Text(
                        text = "Habitual Mode",
                        color = Color(0x6F000000),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 20.dp, end = 10.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge

                    )
                }
            }
        }
    }
}

