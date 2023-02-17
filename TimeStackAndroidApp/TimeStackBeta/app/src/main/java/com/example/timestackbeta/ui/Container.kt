package com.example.timestackbeta.ui


import AddInputDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp


@Composable
fun Container(context:Context) {
    val activityNameList = remember { mutableStateListOf<String>() }
    val activityTimeList = remember { mutableStateListOf<Long>() }
    val startTime = remember { mutableStateListOf<Long>() }
    var activityName by remember { mutableStateOf("") }
    var activityTime by remember { mutableStateOf("") }
    var openDialog by remember { mutableStateOf(false) }
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
                LazyColumn(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                    itemsIndexed(activityNameList){
                        index, item ->
                        val endTime = System.currentTimeMillis()
                        val elapsedTime = endTime - startTime[index]
                        Log.i("start_time","${index + 1} $elapsedTime elapsed time ${activityNameList.size}")
                        Loader(elapsedTime, activityTimeList[index])
                        Text(
                            item, textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "${activityTimeList[index]}",
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
                    openDialog = true
                }, Modifier.size(70.dp, 50.dp), colors = ButtonDefaults.buttonColors(Color.Black)) {
                    Icon(
                        Icons.Filled.Add, "menu",
                        Modifier.fillMaxSize(),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(60.dp))
                ElevatedButton(onClick = {
                    openDialog = true
                }, Modifier.size(70.dp, 50.dp), colors = ButtonDefaults.buttonColors(Color.Black)) {
                    Text(
                        text = "-", Modifier.fillMaxHeight(),
                        Color.White,
                        25.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
    if (openDialog) {
        AddInputDialog(
            onConfirm = {
                Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show()
                activityNameList.add(activityName)
                activityTimeList.add(activityTime.toLong())
                startTime.add(System.currentTimeMillis())
                for (i in startTime){
                    Log.i("start_time", "$i Hello")
                }
                openDialog = false
            },
            onDismiss = { openDialog = false },
            activityName = activityName, onActivityNameChange = { activityName = it },
            activityTime = activityTime, onActivityTimeChange = { activityTime = it }
        )
    }
}






