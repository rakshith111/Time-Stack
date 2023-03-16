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
import com.example.timestackarchitecture.ui.components.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Container(
    stackList: List<StackData>,
    selectedItems: MutableList<Int>,
    startTimer: (Int) -> Unit,
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

    println("timePlayed: ${totalPlayedTime()}")
    if (stackList.isNotEmpty()) {
        play = stackList[0].isPlaying
    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        snackbarHost = {SnackbarHost(hostState = snackBarHostState)}){ paddingValues ->
        println(paddingValues)
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
                        verticalArrangement = Arrangement.Center
                    ) {
                        repeat(stackList.size) { index ->
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
                                    }) {

                                Loader(
                                    totalPlayedTime(),
                                    stackList[index].stackTime,
                                    stackList[index].isPlaying
                                ) {
                                    println("outside ${totalPlayedTime()}")
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
                                startTimer(totalPlayedTime())
                                println("Timer started")
                            } else {
                                stopTimer { time -> updateProgress(time) }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    ElevatedButton(
                        onClick = {
                            openDialogRemove = true
                        },
                        Modifier.size(60.dp, 50.dp),
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ) {
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
                            snackBarHostState = snackBarHostState)
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
                        selectedItems.sortedDescending().forEach { index ->
                            removeStack(stackList[index])
                        }
                        if (selectedItems.contains(0) && play) {
                            println("contains 0")
                            stopTimer { play = false }
                            updateProgress(0)
                        }
                        snackBarMessage(
                            message = if (selectedItems.size > 1) "${selectedItems.size} Stacks removed"
                            else "Stack removed",
                            scope = scope,
                            snackBarHostState = snackBarHostState)
                        selectedItems.clear()
                        openDialogRemove = false
                    },
                    onDismiss = { openDialogRemove = false },
                )
            }
        }
    }
}

