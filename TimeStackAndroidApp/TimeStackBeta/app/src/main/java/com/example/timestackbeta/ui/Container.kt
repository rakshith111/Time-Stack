package com.example.timestackbeta.ui


import AddInputDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.timestackbeta.R
import com.example.timestackbeta.StackTimer


@Composable
fun Container(context:Context) {
    val activityNameList = remember { mutableStateListOf<String>() }
    val activityTimeList = remember { mutableStateListOf<Long>() }
    var activityName by remember { mutableStateOf("") }
    var activityTime by remember { mutableStateOf("") }
    var openDialogAdd by remember { mutableStateOf(false) }
    var openDialogRemove by remember { mutableStateOf(false) }
    val activeStack = remember { mutableStateListOf<Boolean>() }
    val finished = remember { mutableStateListOf<Boolean>() }
    var threadStarted by remember { mutableStateOf(false) }
    var totalPlayedTime by remember { mutableStateOf(0) }
    var play by remember { mutableStateOf(false) }
    var buttonPressed by remember{ mutableStateOf(false) }
    val stackObject by remember { mutableStateOf(StackTimer()) }
    Box(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(Modifier.fillMaxHeight(), Arrangement.Center) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .size(200.dp, 500.dp)
                    .clip(shape = RoundedCornerShape(size = 12.dp))
                    .background(color = Color(0xFF82D8FF))
            ) {
                Column(Modifier.fillMaxHeight().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center) {
                    activityNameList.forEachIndexed{ index, item ->
                        Log.i("start_time","${index + 1} $totalPlayedTime totalPlayedTime  ${activityNameList.size}")
                        Loader(totalPlayedTime, activityTimeList[index], play, activeStack[index], finished[index],{
//                            println("active stack false")
                            activeStack[index] = false
                        }){
//                            println("finish stack false")
                            finished[index] = true
                            if(activeStack.size > index + 1){
                                activeStack[index + 1] = true
                                println("afer finsihed $totalPlayedTime")
                                stackObject.stopTimer { time-> totalPlayedTime = 0}
                                play = false
                            }
                        }
                        Text(
                            item, textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "${activityTimeList[index]} milliseconds",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(30.dp))
            Row {
                ElevatedButton(onClick = {
                    openDialogAdd = true
                }, Modifier.size(60.dp, 50.dp), colors = ButtonDefaults.buttonColors(Color.Black)) {
                    Icon(
                        Icons.Filled.Add, "menu",
                        Modifier.fillMaxSize(),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))
                PlayPauseButton(isPlaying = play) {
                    play = it
                    buttonPressed = true
                    println("1play")
                    stackObject.apply {
                        if (it) {
                            startTimer(totalPlayedTime)
                            println("Timer started")
                            threadStarted = true
                        } else if (threadStarted) {
                            stopTimer {time-> totalPlayedTime = (time) }
                            threadStarted = false
                        }
                    }

                }
                Spacer(modifier = Modifier.width(10.dp))
                ElevatedButton(onClick = {
                    openDialogRemove = true
                }, Modifier.size(60.dp, 50.dp), colors = ButtonDefaults.buttonColors(Color.Black)) {
                    Text(
                        text = "-", Modifier.fillMaxHeight(),
                        Color.White,
                        23.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
    when {
        openDialogAdd -> {
            AddInputDialog(
                onConfirm = {
                    Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show()
                    activityNameList.add(activityName)
                    activityTimeList.add(activityTime.toLong())
                    finished.add(false)
                    if(activityNameList.size == 1){
                        activeStack.add(true)
                    } else {
                        activeStack.add(false)
                    }
                    openDialogAdd = false
                },
                onDismiss = { openDialogAdd = false },
                activityName = activityName,
                onActivityNameChange = { activityName = it },
                onActivityTimeChange = { activityTime = it }
            )
        }
        openDialogRemove -> {
            AddInputDialog(
                onConfirm = {
                    Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show()
                    activityNameList.removeLast()
                    activityTimeList.removeLast()
                    activeStack.removeLast()
                    finished.removeLast()
                    openDialogRemove = false
                },
                onDismiss = { openDialogRemove = false },
                activityName = activityName,
                onActivityNameChange = { activityName = it },
                onActivityTimeChange = { activityTime = it }
            )
        }
    }
}
@Composable
fun PlayPauseButton(
    isPlaying: Boolean,
    onClick: (Boolean) -> Unit
) {
    FloatingActionButton(onClick = {
        onClick(!isPlaying)},
        shape = CircleShape,
        containerColor = Color.Black,
        modifier = Modifier.size(60.dp)) {
        if(isPlaying){
            Icon(painter = painterResource(id = R.drawable.baseline_stop_24),
                contentDescription = "Stop", tint = Color.White)

        } else{
            Icon(imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play",
                tint = Color.White)
        }
    }
}







