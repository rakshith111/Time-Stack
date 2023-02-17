package com.example.timestackbeta.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.timestackbeta.R

@Composable
fun Loader(elapsedTime: Long, totalTime: Long) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val videoLength = composition?.duration
    println(videoLength)
    val progress:Float
    if (videoLength != null) {
        progress = if (elapsedTime > totalTime){
            1f
        } else {
            elapsedTime.toFloat() / totalTime
        }


        LottieAnimation(
            composition,
            modifier = Modifier.requiredHeight(350.dp),
            clipSpec = LottieClipSpec.Progress(progress, 1f),
            speed = videoLength/totalTime,
            contentScale = ContentScale.FillHeight,
        )
    }
}
