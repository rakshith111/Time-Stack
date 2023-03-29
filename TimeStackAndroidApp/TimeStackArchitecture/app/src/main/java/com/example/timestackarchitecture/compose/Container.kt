package com.example.timestackarchitecture.compose


import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timestackarchitecture.R
import com.example.timestackarchitecture.data.StackData
import com.example.timestackarchitecture.service.TimerService
import com.example.timestackarchitecture.ui.components.*
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Container(
    stackList: List<StackData>,
    selectedItems: MutableList<Int>,
    startTimer: (Int, Int) -> Unit,
    stopTimer: ((Int) -> Unit) -> Unit,
    totalPlayedTime: () -> Int,
    updateProgress: (Int) -> Unit,
    insertStack: (StackData) -> Unit,
    updateStack: (StackData) -> Unit,
    removeStack: (StackData) -> Unit,
    ) {
    var openDialogAdd by remember { mutableStateOf(false) }
    var openDialogRemove by remember { mutableStateOf(false) }
    var activityName by remember { mutableStateOf("") }
    var activityTime by remember { mutableStateOf("0") }
    val activeStack by remember { mutableStateOf(true) }
    var play by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    Timber.d("timePlayed: ${totalPlayedTime()}")
    play = if (stackList.isNotEmpty()) {
        stackList[0].isPlaying
    } else {
        false
    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { paddingValues ->
        Timber.d("paddingValues: $paddingValues")
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
//                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Center
                    ) {
                        repeat(stackList.size) { index ->
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .border(
                                        if (selectedItems.contains(index))
                                            5.dp else 0.dp,
                                        if (selectedItems.contains(index))
                                            Color(0x1FFFFFFF) else Color.Transparent,
                                        if (selectedItems.contains(index))
                                            RoundedCornerShape(58.dp) else RoundedCornerShape(0.dp)
                                    )
                                    .background(
                                        if (selectedItems.contains(index))
                                            Color(0x1FFFFFFF) else Color.Transparent,
                                        shape = RoundedCornerShape(50.dp)
                                    )
                                    .shadow(
                                        if (selectedItems.contains(index)){
                                            7.dp
                                        } else {
                                            0.dp
                                        },
                                        if (selectedItems.contains(index)){
                                            RoundedCornerShape(50.dp)
                                        } else {
                                            RoundedCornerShape(0.dp)
                                        },
                                        ambientColor = Color(0xFFFFFFFF),
                                        spotColor = Color(0xFFFFFFFF)
                                    ) //shadow
//
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = {
                                                if (!selectedItems.contains(index)) {
                                                    Timber.d("pressed $index")
                                                    selectedItems.add(index)
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                }
                                            },
                                            onTap = {
                                                if (selectedItems.contains(index)) {
                                                    Timber.d("deselected $index")
                                                    selectedItems.remove(index)
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                }
                                            }
                                        )
                                    }
                            ) {

                                Loader(
                                    totalPlayedTime(),
                                    stackList[index].stackTime,
                                    stackList[index].isPlaying
                                ) {
                                    Timber.d("outside ${totalPlayedTime()}")
                                    stopTimer { play = false }
                                    removeStack(stackList[index])
                                    updateProgress(0)
                                    snackBarMessage(
                                        message = "Stack removed",
                                        scope = scope,
                                        snackBarHostState = snackBarHostState
                                    )
                                }
                            }
                            Text(
                                stackList[index].stackName, textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                color = Color.Black)

                            //convert milliseconds to hours and minutes
                            val time = stackList[index].stackTime/1000
                            val hours = time.div(3600)
                            val remainingSeconds = time.minus((hours.times(3600)))
                            val minutes = remainingSeconds.div(60)

                            Text(
                                convertTime(hours, minutes),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.dm_sans)),
                                color = Color.Black)
                        }
                    }
                }
                Spacer(modifier = Modifier.size(30.dp))

                Row {
                    ElevatedButton(
                        onClick = {
                            openDialogAdd = true
                        },
                        Modifier.size(60.dp, 50.dp),
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ) {
                        Icon(
                            Icons.Filled.Add, "menu",
                            Modifier.fillMaxSize(),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    PlayPauseButton(isPlaying = play) {
                        if (stackList.isEmpty()) return@PlayPauseButton
                        else {
                            play = it
                            stackList[0].isPlaying = it
                            updateStack(
                                StackData(
                                    stackList[0].id,
                                    stackList[0].stackName,
                                    stackList[0].stackTime,
                                    activeStack,
                                    it
                                )
                            )
                            if (it) {
                                startTimer(totalPlayedTime(), stackList[0].stackTime.toInt())
                                Timber.d("Timer started")
                            } else {
                                stopTimer { time -> updateProgress(time) }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    ElevatedButton(
                        onClick = {
                            if (stackList.isEmpty()) {
                                return@ElevatedButton
                            } else {
                                openDialogRemove = true
                            }
                        },
                        Modifier.size(60.dp, 50.dp),
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ) {
                        Text(
                            text = "-", Modifier.fillMaxHeight(),
                            Color.White,
                            23.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(
                                Font(R.font.dm_sans)
                            )
                        )
                    }
                }
            }
        }
        when {
            openDialogAdd -> {
                AddInputDialog(
                    onConfirm = {
                        insertStack(
                            StackData(
                                0,
                                activityName,
                                activityTime.toLong(),
                                activeStack,
                                false
                            )
                        )
                        snackBarMessage(
                            message = "$activityName stack added",
                            scope = scope,
                            snackBarHostState = snackBarHostState
                        )
                        openDialogAdd = false
                    },
                    onDismiss = { openDialogAdd = false
                        if (activityName.isEmpty()){
                            snackBarMessage(
                                message = "Please enter activity name",
                                scope = scope,
                                snackBarHostState = snackBarHostState)

                        } else {
                            snackBarMessage(
                                message = "Time is set to 0",
                                scope = scope,
                                snackBarHostState = snackBarHostState)
                        }
                    },
                    activityName = activityName,
                    onActivityNameChange = { activityName = it },
                    onActivityTimeChange = { activityTime = it }
                )
            }

            openDialogRemove -> {
                RemoveInputDialog(
                    onConfirm = {
                        if (selectedItems.size > 0) {
                            selectedItems.sortedDescending().forEach { index ->
                                removeStack(stackList[index])
                            }
                            if (selectedItems.contains(0)) {
                                Timber.d("contains 0")
                                if (play) {
                                    stopTimer { play = false }
                                }
                                updateProgress(0)
                            }
                            selectedItems.clear()
                        } else {
                            if (stackList.isNotEmpty()) {
                                removeStack(stackList[0])
                                if (play) {
                                    stopTimer { play = false }
                                }
                                updateProgress(0)
                            }

                        }
                        snackBarMessage(
                            message = if (selectedItems.size > 1) "${selectedItems.size} Stacks removed"
                            else "Stack removed",
                            scope = scope,
                            snackBarHostState = snackBarHostState
                        )
                        openDialogRemove = false
                    },
                    onDismiss = { openDialogRemove = false },
                    selectedItems = selectedItems,
                )
            }
        }
    }
}


