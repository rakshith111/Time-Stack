package com.example.timestackarchitecture.habitualmode.compose


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timestackarchitecture.R
import com.example.timestackarchitecture.ui.components.PlayPauseButton
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitualContainer() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Timber.d("paddingValues: $paddingValues")
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF466B8A),
                            Color(0xFF7B598D)
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, bottom = 20.dp)
            ) {
                Text(
                    "Habitual",
                    fontSize = 30.sp,
                    color = Color(0x7FFFFFFF),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start,
                )
                Text(
                    "Mode",
                    fontSize = 30.sp,
                    color = Color(0x7FFFFFFF),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 30.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                BoxWithConstraints(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .aspectRatio(0.82f)
                        .clip(shape = RoundedCornerShape(size = 12.dp))
                        .background(color = Color(0x3F82D8FF))
                        .border(5.dp, Color(0x3FFFFFFF), shape = RoundedCornerShape(12.dp))
                ) {
                    Column(
                        Modifier
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        repeat(1) { index ->
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 2.dp,
                                        color = Color(0x1FFFFFFF),
                                        shape = RoundedCornerShape(50.dp)
                                    )
                                    .background(
                                        if (true)
                                            Color(0x1FFFFFFF) else Color.Transparent,
                                        shape = RoundedCornerShape(50.dp)
                                    )
                                    .shadow(

                                        elevation = 10.dp,
                                        ambientColor = Color(0xFFFFFFFF),
                                        spotColor = Color(0xFFFFFFFF)
                                    ) //shadow
//
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = {

                                            },
                                            onTap = {

                                            }
                                        )
                                    }
                            ) {

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {



                                    Text(
                                        "sf", textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth(),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Black
                                    )

                                    //convert milliseconds to hours and minutes


                                    Text(
                                        "sd",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp, bottom = 10.dp),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Black
                                    )
                                    if (true) {
                                        Divider(
                                            color = Color(0x1FFFFFFF),
                                            thickness = 2.dp,
                                            modifier = Modifier.padding(top = 10.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {

                        },

                        Modifier
                            .size(60.dp, 70.dp)
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(30.dp)),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Black,
                        ),
                    ) {
                        Icon(
                            Icons.Filled.Refresh, "refresh",
                            Modifier.size(24.dp, 24.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {

                        },
                        Modifier
                            .size(60.dp, 70.dp)
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(30.dp)),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Black,
                        )
                    ) {
                        Icon(
                            Icons.Filled.Add, "menu",
                            Modifier.size(24.dp, 24.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    PlayPauseButton(isPlaying = true) {
                        if(true)  return@PlayPauseButton
                        else {


                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {

                        },
                        Modifier
                            .size(60.dp, 70.dp)
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(30.dp)),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Black,
                        ),
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.minus
                            ),
                            contentDescription = "Remove",
                            Modifier.size(24.dp, 24.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {

                                },
                        Modifier
                            .size(60.dp, 70.dp)
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(30.dp)),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Black,
                        ),
                    ) {
                        Icon(
                            Icons.Filled.Settings, "settings",
                            Modifier.size(24.dp, 24.dp),
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}