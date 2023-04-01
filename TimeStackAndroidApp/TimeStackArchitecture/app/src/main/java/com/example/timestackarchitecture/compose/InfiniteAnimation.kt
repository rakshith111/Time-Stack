package com.example.timestackarchitecture.compose

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.timestackarchitecture.R

@Composable
fun InfiniteAnimation(
    play: Boolean,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bottle_filling))

    LottieAnimation(
        composition = composition,
        modifier = Modifier.size(500.dp, 330.dp),
        contentScale = ContentScale.FillWidth,
        isPlaying = play,
        iterations = LottieConstants.IterateForever
    )
}
