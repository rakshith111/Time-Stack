package com.example.timestackbeta.ui


import AddInputDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Container(context:Context){

    var openDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(Modifier.fillMaxHeight(), Arrangement.Center) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .size(200.dp, 500.dp)
                    .clip(shape = RoundedCornerShape(size = 12.dp))
                    .background(color = Color(0xFF82D8FF))
            ) {
                LazyColumn(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                    items(19) { item ->
                        Loader()
                    }
                }
            }
            Spacer(modifier = Modifier.size(30.dp))
            Row() {
                ElevatedButton(onClick = {
                    openDialog = true
                }, Modifier.size(70.dp,50.dp)) {
                        Icon(Icons.Filled.Add, "menu",
                            Modifier.fillMaxSize(),
                            tint = Color.White)
                }

                Spacer(modifier = Modifier.width( 60.dp))
                ElevatedButton(onClick = {
                    openDialog = true
                }, Modifier.size(70.dp, 50.dp)){
                        Text(text = "-", Modifier.fillMaxHeight(),
                            Color.White,
                            25.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold)
                    }

            }

        }


    }
    if (openDialog) {
        AddInputDialog(
            onConfirm = {
                Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { openDialog = false }
        )

    }


}






