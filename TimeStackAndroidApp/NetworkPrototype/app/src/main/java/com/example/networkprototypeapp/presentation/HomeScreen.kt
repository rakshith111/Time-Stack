package com.example.networkprototypeapp.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.networkprototypeapp.viewmodel.ApiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    ApiViewModel: ApiViewModel
) {
    Text(text = "Home Screen")
    var context = LocalContext.current

//    LaunchedEffect( key1 = Unit, block = {
//        ApiViewModel.makeGetApiRequest()
//    } )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Home")
                },
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ){
        println(it)
        Text(text = "Network Prototype Application")
    }
}