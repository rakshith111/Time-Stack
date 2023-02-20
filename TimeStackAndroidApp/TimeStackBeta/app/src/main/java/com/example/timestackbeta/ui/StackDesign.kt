package com.example.timestackbeta.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.timestackbeta.R

@Composable
fun Loader(
    elapsedTime: Long,
    totalTime: Long,
    play: Boolean,
    activeStack: Boolean,
    finished: Boolean,
    onActiveStackChange: () -> Unit,
    onFinishedChange: () -> Unit
) {
    var speedTime = totalTime
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val videoLength = composition?.duration
    var progress: Float
    var isPlaying = play
    if (videoLength != null) {
        progress = if (elapsedTime > totalTime) {
            speedTime = 1L
            1f
        } else {
            elapsedTime.toFloat() / totalTime
        }
        if (finished) {
            progress = 1F
        }
        if (!activeStack and !finished) {
            progress = 0F
            isPlaying = false
        }
        if(!activeStack and finished and !isPlaying){
            progress = 1F
            isPlaying = true
        }
        val progressAsState by animateLottieCompositionAsState(
            composition = composition,
            clipSpec = LottieClipSpec.Progress(progress, 1f),
            isPlaying = isPlaying, speed = videoLength / speedTime
        )

        LottieAnimation(
            composition = composition,
            progress = { progressAsState },
            modifier = Modifier.requiredHeight(350.dp),
            contentScale = ContentScale.FillHeight
        )
        if (progressAsState == 1F) {
            onActiveStackChange()
            onFinishedChange()
        }
    }
}
