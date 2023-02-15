package com.example.timestackbeta.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.timestackbeta.R

@Composable
fun Loader(){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val videoLength = composition?.duration
    println(videoLength)
    if (videoLength != null) {
        LottieAnimation(
            composition,
            modifier = Modifier.requiredHeight(350.dp),
            iterations = LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
            speed = videoLength/100000,
            contentScale = ContentScale.FillHeight,
        )
    }
}
