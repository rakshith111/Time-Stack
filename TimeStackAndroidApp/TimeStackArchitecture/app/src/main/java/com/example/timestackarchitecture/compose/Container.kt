package com.example.timestackarchitecture.compose



import androidx.compose.runtime.*
import com.example.timestackarchitecture.data.StackData
import kotlin.reflect.KFunction1

@Composable
fun Container(
    stackList: MutableList<StackData>,
    startTimer: (Int) -> Unit,
    stopTimer: KFunction1<(Int) -> Unit, Unit>,
    ) {
}







