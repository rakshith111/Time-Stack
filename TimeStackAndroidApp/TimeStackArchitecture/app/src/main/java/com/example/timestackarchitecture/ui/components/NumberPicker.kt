package com.example.timestackarchitecture.ui.components

import android.widget.NumberPicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun TimePicker(onTimeSelected: (Int) -> Unit) {
    var selectedTimeHours by remember { mutableStateOf(0) }
    var selectedTimeMinutes by remember { mutableStateOf(0) }
    Row{
        AndroidView(factory = { context ->
            NumberPicker(context).apply {
                minValue = 0
                maxValue = 24
                setOnValueChangedListener { _, _, newVal ->
                    selectedTimeHours = newVal
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
