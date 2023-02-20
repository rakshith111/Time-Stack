package com.example.timestackbeta.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.timestackbeta.R

@Composable
fun Loader(elapsedTime: Long, totalTime: Long, play: Boolean, activeStack: Boolean) {
    var speedTime = totalTime
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val videoLength = composition?.duration
    val progress:Float
    var isPlaying = play

    if (videoLength != null) {
        progress = if (elapsedTime > totalTime){
            speedTime = 1L
            1f
        } else {
            elapsedTime.toFloat() / totalTime
        }
        if(!activeStack){
            isPlaying = false
        }
        val progressAsState by animateLottieCompositionAsState(composition = composition,
            clipSpec = LottieClipSpec.Progress(progress, 1f),
            isPlaying = isPlaying,speed = videoLength/speedTime)

        LottieAnimation(
            composition = composition,
            progress = { progressAsState },
            modifier = Modifier.requiredHeight(350.dp),
            contentScale = ContentScale.FillHeight
        )
        if (progressAsState == 1F){
            println("THE END")
        }
    }

}
