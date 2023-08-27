package com.example.timestackarchitecture.habitualmode.compose


import android.content.Intent
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timestackarchitecture.R
import com.example.timestackarchitecture.casualmode.service.TimerAlarmReceiver
import com.example.timestackarchitecture.casualmode.service.TimerService
import com.example.timestackarchitecture.habitualmode.data.HabitualStackData
import com.example.timestackarchitecture.habitualmode.viewmodel.HabitualStackViewModel
import com.example.timestackarchitecture.ui.components.AddInputDialog
import com.example.timestackarchitecture.ui.components.EditDialogBoxHabitual
import com.example.timestackarchitecture.ui.components.PlayPauseButton
import com.example.timestackarchitecture.ui.components.RemoveInputDialogHabitual
import com.example.timestackarchitecture.ui.components.ResetDialogBox
import com.example.timestackarchitecture.ui.components.convertTime
import com.example.timestackarchitecture.ui.components.snackBarMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitualContainer(
    habitualStackViewModel: HabitualStackViewModel,
    selectedItems: MutableList<Int>,
    getProgress: () -> Long,
    updateProgress: (Long) -> Unit,
    insertStack: suspend (HabitualStackData) -> Unit,
    updateStack: suspend (HabitualStackData) -> Unit,
    removeStack: suspend (HabitualStackData) -> Unit,
    getStartTime: () -> Long,
    saveCurrentTime: (Long) -> Unit,
    getFirstTime:() -> Boolean,
    saveFirstTime: (Boolean) -> Unit,
) {
    val stackList = habitualStackViewModel.stackList.collectAsState().value
    LaunchedEffect(Unit){
        habitualStackViewModel.getStacks()
    }
    var openDialogAdd by remember { mutableStateOf(false) }
    var openDialogRemove by remember { mutableStateOf(false) }
    var activityName by remember { mutableStateOf("") }
    var activityTime by remember { mutableStateOf("0") }
    val activeStack by remember { mutableStateOf(true) }
    var play by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    var showEditMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    var resetDialog by remember { mutableStateOf(false) }
    var editDialog by remember { mutableStateOf(false) }

    Timber.d("timePlayed: ${getProgress()}")
    play = if (stackList.isNotEmpty()) {
        Timber.d("play")
        Timber.d("stackList.size: ${stackList.size}")
        Timber.d("stackList[0].isPlaying: ${stackList[0].isPlaying}")
        stackList[0].isPlaying
    } else {
        false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { paddingValues ->
        Timber.d("paddingValues: $paddingValues")
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF466B8A),
                            Color(0xFF7B598D)
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, bottom = 20.dp)
            ) {
                Text(
                    "Habitual Mode",
                    fontSize = 30.sp,
                    color = Color(0xA3FFFFFF),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {

                    },
                    Modifier
                        .size(70.dp, 50.dp)
                        .padding(end = 15.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFF000000)
                    )
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.inventory
                        ),
                        contentDescription = "Remove",
                        Modifier.size(25.dp, 25.dp),
                        tint = Color.White
                    )
                }

            }


            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                BoxWithConstraints(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .aspectRatio(0.82f)
                        .clip(shape = RoundedCornerShape(size = 12.dp))
                        .background(color = Color(0x3F82D8FF))
                        .border(5.dp, Color(0x7f000000), shape = RoundedCornerShape(12.dp))
                ) {
                    Column(
                        Modifier
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
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
                                        if (selectedItems.contains(index)) {
                                            7.dp
                                        } else {
                                            0.dp
                                        },
                                        if (selectedItems.contains(index)) {
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
                                                    showEditMenu = true
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
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    HabitualInfiniteAnimation(play = stackList[index].isPlaying)
                                    HabitualLoader(
                                        getProgress(),
                                        stackList[index].stackTime,
                                        stackList[index].isPlaying
                                    ) {

                                        Timber.d("outside ${getProgress()}")
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            val serviceIntent = Intent(context, TimerService::class.java)
                                            context.stopService(serviceIntent)
                                        }
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                Timber.d("Coroutine started")
                                                Timber.d("Before saveFirstTime")
                                                saveFirstTime(true)
                                                Timber.d("After saveFirstTime")
                                                Timber.d("Before removeStack")
                                                removeStack(stackList[0])
                                                Timber.d("After removeStack")
                                                Timber.d("stackList.size inside: ${stackList.size}")
//                                                if (stackList.isNotEmpty()) {
//                                                    Timber.d("stackList[0].isPlaying inside: ${stackList[0].isPlaying} ${stackList[0].stackName}")
//                                                }
                                                val stackList = habitualStackViewModel.getStacks()
                                                Timber.d("stackList.size inside: ${stackList.size}")
                                                Timber.d("before updateStack")
                                                if (stackList.isNotEmpty()) {
                                                    Timber.d("before updateStack inside")
                                                    updateStack(
                                                        HabitualStackData(
                                                            stackList[0].id,
                                                            stackList[0].stackName,
                                                            stackList[0].stackTime,
                                                            activeStack,
                                                            true
                                                        )
                                                    )
                                                }
                                                Timber.d("after updateStack")

                                                updateProgress(0)
                                                Timber.d("Timer stopped, play: $play, stackList.size: ${stackList.size}")

                                                // Move to the main thread to show the snackbar
                                                withContext(Dispatchers.Main) {
                                                    snackBarMessage(
                                                        message = "Activity removed",
                                                        scope = scope,
                                                        snackBarHostState = snackBarHostState
                                                    )
                                                }
                                                Timber.d("Coroutine finished")
                                            } catch (e: Exception) {
                                                Timber.e(e, "Error in coroutine")
                                            }
                                        }
                                    }

                                    Text(
                                        stackList[index].stackName, textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth(),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Black
                                    )

                                    //convert milliseconds to hours and minutes
                                    val time = stackList[index].stackTime / 1000
                                    val hours = time.div(3600)
                                    val remainingSeconds = time.minus((hours.times(3600)))
                                    val minutes = remainingSeconds.div(60)

                                    Text(
                                        convertTime(hours, minutes),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp, bottom = 10.dp),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Black
                                    )
                                    if (index != stackList.size - 1) {
                                        Divider(
                                            color = Color(0x1FFFFFFF),
                                            thickness = 2.dp,
                                            modifier = Modifier.padding(top = 10.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            if (stackList.isEmpty()) {
                                snackBarMessage(
                                    message = "No activities to reset",
                                    scope = scope,
                                    snackBarHostState = snackBarHostState
                                )
                                return@IconButton
                            } else if (selectedItems.isNotEmpty()) {
                                if(!selectedItems.contains(0)){
                                    snackBarMessage(
                                        message = "Selected activity is not the first one",
                                        scope = scope,
                                        snackBarHostState = snackBarHostState
                                    )
                                    return@IconButton
                                } else if (selectedItems.size == 1 && selectedItems.contains(0)) {
                                    resetDialog = true
                                } else {
                                    snackBarMessage(
                                        message = "Only the first activity can be reset",
                                        scope = scope,
                                        snackBarHostState = snackBarHostState
                                    )
                                    return@IconButton
                                }
                            } else {
                                resetDialog = true
                            }
                        },

                        Modifier
                            .size(60.dp, 70.dp)
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(30.dp)),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Black,
                        ),
                    ) {
                        Icon(
                            Icons.Filled.Refresh, "refresh",
                            Modifier.size(24.dp, 24.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            openDialogAdd = true
                        },
                        Modifier
                            .size(60.dp, 70.dp)
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(30.dp)),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Black,
                        )
                    ) {
                        Icon(
                            Icons.Filled.Add, "menu",
                            Modifier.size(24.dp, 24.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    PlayPauseButton(isPlaying = play) {
                        if (stackList.isEmpty()) return@PlayPauseButton
                        else {
                            play = it
                            stackList[0].isPlaying = it
                            CoroutineScope(Dispatchers.IO).launch {
                                updateStack(
                                    HabitualStackData(
                                        stackList[0].id,
                                        stackList[0].stackName,
                                        stackList[0].stackTime,
                                        activeStack,
                                        it
                                    )
                                )
                            }


                            if (it) {
                                Timber.d("Timer started")
                                //use system time to update progress
                                saveCurrentTime(System.currentTimeMillis())
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val serviceIntent = Intent(context, TimerService::class.java)
                                    serviceIntent.putExtra("duration", stackList[0].stackTime)
                                    serviceIntent.putExtra("stackName", stackList[0].stackName)
                                    context.startService(serviceIntent)
                                }

                                if (stackList.isNotEmpty()) {
                                    val remainingTime = stackList[0].stackTime - getProgress()
                                    TimerAlarmReceiver().setTimerAlarm(context, remainingTime)
                                    Timber.d("set alarm for $remainingTime")
                                    if (getFirstTime()) {
                                        updateProgress(0)
                                    }
                                }

                                saveFirstTime(false)
//                               //start notification
                            } else {
                                Timber.d("Timer paused")
                                //stop notification
                                val elapsed =
                                    (System.currentTimeMillis() - getStartTime()) + getProgress()
                                updateProgress(elapsed)

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val serviceIntent =
                                        Intent(context, TimerService::class.java)
                                    context.stopService(serviceIntent)
                                }
                                TimerAlarmReceiver().cancelTimerAlarm(context)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            if (stackList.isEmpty()) {
                                snackBarMessage(
                                    message = "No activity to remove, add a new activity",
                                    scope = scope,
                                    snackBarHostState = snackBarHostState
                                )
                                return@IconButton
                            } else {
                                openDialogRemove = true
                            }
                        },
                        Modifier
                            .size(60.dp, 70.dp)
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(30.dp)),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Black,
                        ),
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.minus
                            ),
                            contentDescription = "Remove",
                            Modifier.size(24.dp, 24.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            if (stackList.isEmpty()) {
                                snackBarMessage(
                                    message = "No activity to edit, add a new activity",
                                    scope = scope,
                                    snackBarHostState = snackBarHostState
                                )
                                return@IconButton
                            } else if (selectedItems.size > 1){
                                snackBarMessage(
                                    message = "Select only one activity to edit",
                                    scope = scope,
                                    snackBarHostState = snackBarHostState
                                )
                            }else {
                                editDialog = true
                            } },
                        Modifier
                            .size(60.dp, 70.dp)
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(30.dp)),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Black,
                        ),
                    ) {
                        Icon(
                            Icons.Filled.Settings, "settings",
                            Modifier.size(24.dp, 24.dp),
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    when {
        openDialogAdd -> {
            AddInputDialog(
                onConfirm = {
                    CoroutineScope(Dispatchers.IO).launch {
                        insertStack(
                            HabitualStackData(
                                0,
                                activityName,
                                activityTime.toLong(),
                                activeStack,
                                false
                            )
                        )
                    }
                    snackBarMessage(
                        message = "$activityName activity added",
                        scope = scope,
                        snackBarHostState = snackBarHostState
                    )
                    openDialogAdd = false
                },

                onDismiss = {message ->
                    openDialogAdd = false
                    if (activityName.isBlank() && message == "Blank") {
                        snackBarMessage(
                            message = "Please enter activity name",
                            scope = scope,
                            snackBarHostState = snackBarHostState
                        )
                    } else if (message == "0") {
                        snackBarMessage(
                            message = "Please enter activity time",
                            scope = scope,
                            snackBarHostState = snackBarHostState
                        )
                    }
                },
                activityName = activityName,
                onActivityNameChange = { activityName = it },
                onActivityTimeChange = { activityTime = it }
            )
        }

        openDialogRemove -> {
            RemoveInputDialogHabitual(
                onConfirm = {
                    if (selectedItems.size > 0) {
                        selectedItems.sortedDescending().forEach { index ->
                            CoroutineScope(Dispatchers.IO).launch {
                                removeStack(stackList[index])
                            }
                        }
                        if (selectedItems.contains(0)) {
                            Timber.d("contains 0")
                            if (play) {
                                //stop notification
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val serviceIntent =
                                        Intent(context, TimerService::class.java)
                                    context.stopService(serviceIntent)
                                    play = false
                                }
                            }
                            updateProgress(0)
                        }
                        selectedItems.clear()
                    } else {
                        if (stackList.isNotEmpty()) {
                            CoroutineScope(Dispatchers.IO).launch {
                                removeStack(stackList[0])
                            }
                            if (play) {
                                play = false
                                //stop notification
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val serviceIntent =
                                        Intent(context, TimerService::class.java)
                                    context.stopService(serviceIntent)
                                }
                                TimerAlarmReceiver().cancelTimerAlarm(context)
                            }
                            updateProgress(0)

                        }
                    }
                    snackBarMessage(
                        message = if (selectedItems.size > 1) "${selectedItems.size} activities removed"
                        else "activity removed",
                        scope = scope,
                        snackBarHostState = snackBarHostState
                    )
                    openDialogRemove = false
                    saveFirstTime(true)
                },
                onDismiss = { openDialogRemove = false },
                selectedItems = selectedItems,
                stackList = stackList
            )
        }

        resetDialog -> {
            ResetDialogBox(
                onConfirm = {
                    if (stackList.isNotEmpty()) {
                        //reset the progress
                        updateProgress(0)
                        //stop the timer service or notification
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val serviceIntent =
                                Intent(context, TimerService::class.java)
                            context.stopService(serviceIntent)
                        }
                        TimerAlarmReceiver().cancelTimerAlarm(context)
                        play = false
                        saveFirstTime(true)
                        stackList[0].isPlaying = false
                        snackBarMessage(
                            message = "Activity reset",
                            scope = scope,
                            snackBarHostState = snackBarHostState
                        )
                        resetDialog = false
                    }
                },
                onDismiss = { resetDialog = false },
            )
        }

        editDialog -> {
            EditDialogBoxHabitual(
                onConfirm = {
                    if (selectedItems.size > 0 && selectedItems.size == 1){
                        CoroutineScope(Dispatchers.IO).launch {
                            updateStack(
                                HabitualStackData(
                                    stackList[selectedItems[0]].id,
                                    activityName,
                                    activityTime.toLong(),
                                    activeStack,
                                    false
                                )
                            )
                        }

                        snackBarMessage(
                            message = "Activity updated",
                            scope = scope,
                            snackBarHostState = snackBarHostState
                        )
                    } else if (stackList.isNotEmpty()) {
                        stackList[0].stackName = activityName
                        stackList[0].stackTime = activityTime.toLong()
                        stackList[0].isPlaying = false
                        snackBarMessage(
                            message = "Activity updated",
                            scope = scope,
                            snackBarHostState = snackBarHostState
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            updateStack(
                                HabitualStackData(
                                    stackList[0].id,
                                    activityName,
                                    activityTime.toLong(),
                                    activeStack,
                                    false
                                )
                            )
                        }
                    }
                    if (activityTime != "0") {
                        updateProgress(0)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val serviceIntent =
                            Intent(context, TimerService::class.java)
                        context.stopService(serviceIntent)
                    }
                    TimerAlarmReceiver().cancelTimerAlarm(context)
                    editDialog = false
                },
                onDismiss = { message ->
                    editDialog = false
                    if (message == "activityName is blank and time is 0 or same as before") {
                        snackBarMessage(
                            message = "activityName is blank and time is 0 or same as before",
                            scope = scope,
                            snackBarHostState = snackBarHostState
                        )
                    } },
                activityName = activityName,
                onActivityNameChange = { activityName = it },
                onActivityTimeChange = { activityTime = it },
                selectedItems = selectedItems,
                stackList = stackList
            )
        }
    }
}