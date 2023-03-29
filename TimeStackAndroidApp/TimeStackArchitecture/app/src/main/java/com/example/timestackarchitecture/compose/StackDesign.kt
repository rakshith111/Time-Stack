package com.example.timestackarchitecture.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.timestackarchitecture.R


@Composable
fun Loader(
    totalPlayed: Int,
    totalTime: Long,
    play: Boolean,
    onFinishedChange: () -> Unit,
) {
    var speedTime = totalTime - 2000
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.water_loading))
    val videoLength = composition?.duration
    val progress: Float
    val totalPlayedTime = totalPlayed * 1000

    if (videoLength != null) {
        progress = if ( totalPlayedTime > totalTime) {
            speedTime = 1L
            1f
        } else {
            totalPlayedTime.toFloat() / totalTime

        }
        val progressAsState by animateLottieCompositionAsState(
            composition = composition,
            clipSpec = LottieClipSpec.Progress(progress, 1f),
            isPlaying = play, speed = videoLength / speedTime
        )
        LottieAnimation(
            composition = composition,
            progress = { progressAsState },
            modifier = Modifier.requiredHeight(350.dp).fillMaxWidth(),
            contentScale = ContentScale.FillHeight,
        )
        LaunchedEffect(progressAsState) {
            if (progressAsState == 1F) {
                onFinishedChange()
            }
        }
    }
}
