package com.example.timestackarchitecture.casualmode.compose


import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.timestackarchitecture.R


@Composable
fun Loader(
    totalPlayed: Long,
    totalTime: Long,
    play: Boolean,
    onFinishedChange: () -> Unit,
) {

    var speedTime = totalTime - 2000
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.progressbar_2))
    val videoLength = composition?.duration
    val progress: Float


    if (videoLength != null) {
        progress = if ( totalPlayed > totalTime) {
            speedTime = 1L
            1f
        } else {
            totalPlayed.toFloat() / totalTime
        }

        val progressAsState by animateLottieCompositionAsState(
            composition = composition,
            clipSpec = LottieClipSpec.Progress(progress, 1f),
            isPlaying = play, speed = videoLength / speedTime
        )
        LottieAnimation(
            composition = composition,
            progress = { progressAsState },
            modifier = Modifier.aspectRatio(7f).padding(start = 30.dp, end = 30.dp),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth,
        )
        LaunchedEffect(progressAsState) {
            if (progressAsState == 1F) {
                onFinishedChange()
            }
        }
    }
}
