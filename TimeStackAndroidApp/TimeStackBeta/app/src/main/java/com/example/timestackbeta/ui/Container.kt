package com.example.timestackbeta.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

@Composable
fun Container(){
    Box(
        modifier = Modifier
            .background(color = androidx.compose.ui.graphics.Color.Blue)
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Box(contentAlignment = Alignment.Center,modifier = Modifier
            .size(200.dp, 500.dp)
            .clip(shape = RoundedCornerShape(size = 12.dp))
            .background(color = Color(0xFF82D8FF))) {
            LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                items(19) { item ->
                    Loader()
                }
            }
        }}

}




