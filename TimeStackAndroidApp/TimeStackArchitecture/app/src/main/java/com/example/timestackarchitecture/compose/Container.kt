package com.example.timestackarchitecture.compose



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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timestackarchitecture.R
import com.example.timestackarchitecture.data.StackData
import com.example.timestackarchitecture.ui.components.AddInputDialog
import com.example.timestackarchitecture.ui.components.RemoveInputDialog
import kotlin.reflect.KFunction1

@Composable
fun Container(
    stackList: MutableList<StackData>,
    selectedItems: MutableList<Int>,
    startTimer: (Int) -> Unit,
    stopTimer: KFunction1<(Int) -> Unit, Unit>,
    ) {
    var openDialogAdd by remember { mutableStateOf(false) }
    var openDialogRemove by remember { mutableStateOf(false) }
    var activityName by remember { mutableStateOf("") }
    var activityTime by remember { mutableStateOf("0") }
    var activeStack by remember { mutableStateOf(true) }
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
                    repeat(stackList.size){ index ->
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
                            Loader(stackList[index].totalPlayedTime, stackList[index].stackTime, true, stackList[index].activeStack, false,{
//                            println("active stack false")
                            }){
//                            println("finish stack false")

                            }
                        }
                        Text(
                            stackList[index].stackName, textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "${stackList[index]} milliseconds",
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
//                PlayPauseButton(isPlaying = play) {
//                    play = it
//                    buttonPressed = true
//                    println("1play")
//                    if (it) {
//                        startTimer(totalPlayedTime)
//                        println("Timer started")
//                        threadStarted = true
//                    } else if (threadStarted) {
//                        StackTimer.stopTimer {time-> totalPlayedTime = (time) }
//                        threadStarted = false
//                    }
//
//                }
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
                    activeStack = stackList.size == 1
                    stackList.add(StackData(3, activityName, activityTime.toLong(), activeStack, 0))
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
                    val newActivityNameList = stackList.filterIndexed { index, _ ->

                        index !in selectedItems
                    }

                    stackList.clear()
                    stackList.addAll(newActivityNameList)
                    openDialogRemove = false
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









