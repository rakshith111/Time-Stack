package com.example.timestackarchitecture.compose



import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timestackarchitecture.data.StackData
import com.example.timestackarchitecture.ui.components.AddInputDialog
import com.example.timestackarchitecture.ui.components.PlayPauseButton
import com.example.timestackarchitecture.ui.components.RemoveInputDialog

@Composable
fun Container(
    stackList: List<StackData>,
    selectedItems: MutableList<Int>,
    startTimer: (Int) -> Unit,
    stopTimer: ((Int) -> Unit) -> Unit,
    insertStack: (StackData) -> Unit,
    removeStack: (StackData) -> Unit,
    clearStack: () -> Unit,
    ) {
    var openDialogAdd by remember { mutableStateOf(false) }
    var openDialogRemove by remember { mutableStateOf(false) }
    var activityName by remember { mutableStateOf("") }
    var activityTime by remember { mutableStateOf("0") }
    var activeStack by remember { mutableStateOf(true) }
    var play by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    println("recomposing container")
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
                            Loader(stackList[index].totalPlayedTime, stackList[index].stackTime, stackList[index].isPlaying, stackList[index].activeStack) {
                                println("outside ${stackList[index].totalPlayedTime}")
                                removeStack(stackList[0])
                                stopTimer {play = false}

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

                PlayPauseButton(isPlaying = play) {
                    if(stackList.isEmpty()) return@PlayPauseButton
                    else{
                        play = it
                        stackList[0].isPlaying = it
                        if (it) {
                            startTimer(stackList[0].totalPlayedTime)
                            println("Timer started")
                        } else  {
                            stopTimer { time-> stackList[0].totalPlayedTime = (time)}
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
                    activeStack = stackList.isEmpty()
                    insertStack(StackData((0..100).random(), activityName, activityTime.toLong(), activeStack, 0, false))
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
                    val newActivityNameStackList = stackList.filterIndexed { index, _ ->
                        index !in selectedItems
                    }
                    clearStack()
                    for (i in newActivityNameStackList) {
                        insertStack(i)
                    }
                    openDialogRemove = false
                    if(selectedItems.contains(0) && play){
                        stopTimer{ play = false }
                    }
                    selectedItems.clear()
                },
                onDismiss = { openDialogRemove = false },
            )
        }
    }
}