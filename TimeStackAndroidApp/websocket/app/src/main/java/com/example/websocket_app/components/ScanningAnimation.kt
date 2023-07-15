package com.example.websocket_app.components


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun ScanningAnimation() {
    var animateUp by remember { mutableStateOf(true) }
    val scanLineY = animateFloatAsState(
        targetValue = if (animateUp) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2000,
            easing = LinearEasing
        )
    )

    LaunchedEffect(key1 = animateUp) {
        delay(2000)
        animateUp = !animateUp
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val lineY = size.height * scanLineY.value
        drawLine(
            color = Color.Green,
            start = Offset(0f, lineY),
            end = Offset(size.width, lineY),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}