package com.example.timestackarchitecture.ui.components

import android.os.Build
import android.widget.NumberPicker
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
fun TimePicker(onTimeSelected: (Int) -> Unit) {
    var selectedTimeHours by remember { mutableStateOf(0) }
    var selectedTimeMinutes by remember { mutableStateOf(0) }
    val textColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
    Row{
        AndroidView(factory = { context ->
            NumberPicker(context).apply {
                minValue = 0
                maxValue = 24
                setOnValueChangedListener { _, _, newVal ->
                    selectedTimeHours = newVal
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (textColor == Color.White) {
                        setTextColor(ContextCompat.getColor(context, android.R.color.white))
                    } else {
                        setTextColor(ContextCompat.getColor(context, android.R.color.black))
                    }
                }
            }
        })

        AndroidView(factory = { context ->
            NumberPicker(context).apply {
                minValue = 0
                maxValue = 59
                setOnValueChangedListener { _, _, newVal ->
                    selectedTimeMinutes = newVal
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (textColor == Color.White) {

                        setTextColor(ContextCompat.getColor(context, android.R.color.white))
                    } else {
                        setTextColor(ContextCompat.getColor(context, android.R.color.black))
                    }
                }
            }
        })
    }


    Column(modifier = Modifier.fillMaxWidth()) {
        Row{
            Text(
                text = "$selectedTimeHours Hour     $selectedTimeMinutes minute",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
            )
        }

        onTimeSelected(((selectedTimeHours * 60 * 60 * 1000) + (selectedTimeMinutes * 60 * 1000)))
    }
}
