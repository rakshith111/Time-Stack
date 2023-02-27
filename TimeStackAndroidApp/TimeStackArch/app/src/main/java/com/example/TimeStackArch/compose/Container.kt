package com.example.timestackarch.compose


import AddInputDialog
import RemoveInputDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
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
    val selectedItems = remember { mutableStateListOf<Int>() }
    val haptic = LocalHapticFeedback.current
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
                Column(
                    Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center) {

                    repeat(activityNameList.size){ index ->
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    if (selectedItems.contains(index))
                                        Color.LightGray else Color.Transparent
                                )
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            if (!selectedItems.contains(index)) {
                                                println("pressed $index")
                                                selectedItems.add(index)
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            }
                                        },
                                        onTap = {
                                            if (selectedItems.contains(index)) {
                                                println("deselect")
                                                selectedItems.remove(index)
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            }
                                        }
                                    )
                                }){
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
                                    StackTimer.stopTimer {totalPlayedTime = 0}
                                    play = false
                                }
                            }
                        }
                        Text(
                            activityNameList[index], textAlign = TextAlign.Center,
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
                        if (it) {
                            StackTimer.startTimer(totalPlayedTime)
                            println("Timer started")
                            threadStarted = true
                        } else if (threadStarted) {
                            StackTimer.stopTimer {time-> totalPlayedTime = (time) }
                            threadStarted = false
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
            RemoveInputDialog(
                onConfirm = {
                    Toast.makeText(context, "Stack Removed", Toast.LENGTH_SHORT).show()
                    val newActivityNameList = activityNameList.filterIndexed { index, _ ->  index !in selectedItems }
                    val newActivityTimeList = activityTimeList.filterIndexed { index, _ ->  index !in selectedItems }
                    val newActiveStack = activeStack.filterIndexed { index, _ ->  index !in selectedItems }
                    val newFinished = finished.filterIndexed { index, _ ->  index !in selectedItems }

                    activityNameList.clear()
                    activityNameList.addAll(newActivityNameList)

                    activityTimeList.clear()
                    activityTimeList.addAll(newActivityTimeList)

                    activeStack.clear()
                    activeStack.addAll(newActiveStack)

                    finished.clear()
                    finished.addAll(newFinished)

                    openDialogRemove = false
                    if (threadStarted and selectedItems.contains(0)){
                        StackTimer.stopTimer { totalPlayedTime = 0 }
                        play = false
                        if(activeStack.size >= 1){
                            activeStack[0] = true
                        }
                }
                    selectedItems.clear()
                },
                onDismiss = { openDialogRemove = false },
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







