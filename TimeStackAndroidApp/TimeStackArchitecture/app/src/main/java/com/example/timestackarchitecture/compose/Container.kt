package com.example.timestackarchitecture.compose



import androidx.compose.runtime.*
import com.example.timestackarchitecture.data.StackData

@Composable
fun Container(
    stackList: MutableList<StackData>,
    startTimer: (totalPlayedTime: Int) -> Unit,
    stopTimer: (pauseTimer: (Int) -> Unit) -> Unit
) {
    startTimer(0)
    stopTimer {  }
}







