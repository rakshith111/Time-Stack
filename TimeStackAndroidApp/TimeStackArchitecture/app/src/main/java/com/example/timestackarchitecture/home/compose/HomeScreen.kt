package com.example.timestackarchitecture.home.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF8EC5FC),
            Color(0xFFE0C3FC)
        )
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundGradient),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                onClick = {
                    navController.navigate("casualMode") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(0.8f),
                elevation = CardDefaults.cardElevation(8.dp),

            ) {
                Text(
                    text = "Casual Mode",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Card(
                onClick = {
                    navController.navigate("habitualMode") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(0.8f),
                elevation = CardDefaults.cardElevation(8.dp),
            ) {
                Text(
                    text = "Habitual Mode",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

