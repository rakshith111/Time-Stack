package com.example.timestackarchitecture.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.timestackarchitecture.R

@Composable
fun PlayPauseButton(
    isPlaying: Boolean,
    onClick: (Boolean) -> Unit
) {
    FloatingActionButton(onClick = {
        onClick(!isPlaying)},
        shape = CircleShape,
        containerColor = Color(0xff000000),
        modifier = Modifier.size(80.dp),) {
        if(isPlaying){
            Icon(painter = painterResource(id = R.drawable.baseline_stop_24),
                contentDescription = "Stop",Modifier.size(40.dp, 40.dp), tint = Color.White)
        } else{
            Icon(imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play",
                Modifier.size(40.dp, 40.dp),
                tint = Color.White)
        }
    }
}
