package com.example.timestackarchitecture.casualmode.compose

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.timestackarchitecture.R

@Composable
fun InfiniteAnimation(
    play: Boolean,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bottle_filling))

    LottieAnimation(
        composition = composition,
        modifier = Modifier.aspectRatio(1.1f),
        isPlaying = play,
        iterations = LottieConstants.IterateForever
    )
}
