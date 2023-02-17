import android.widget.NumberPicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun TimePicker(onTimeSelected: (Int) -> Unit) {
    var selectedTime by remember { mutableStateOf(0) }
    AndroidView(factory = { context ->
        NumberPicker(context).apply {
            minValue = 0
            maxValue = 100
            setOnValueChangedListener { _, _, newVal ->
                selectedTime = newVal
            }
        }
    })

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Select time in minutes",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
        )

        Text(
            text = "$selectedTime minutes",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
        )

        Button(
            onClick = { onTimeSelected(selectedTime) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Save")
        }
    }
}
