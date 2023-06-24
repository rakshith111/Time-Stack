package com.example.networkprototypeapp.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.networkprototypeapp.data.FakeData
import com.example.networkprototypeapp.viewmodel.ApiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    ApiViewModel: ApiViewModel
) {
    Text(text = "Home Screen")

    LaunchedEffect( key1 = Unit){
        ApiViewModel.makeGetApiRequest()
        ApiViewModel.makePostApiRequest()
    }

    val message = ApiViewModel.dashboardData.collectAsState().value
    val postMessage = ApiViewModel.postData.collectAsState().value
    println("array: $message")
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Home", style = MaterialTheme.typography.titleLarge)
                },
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ){
        paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)){
            Text(
                text = "Network Prototype Application",
                color = Color.White, modifier = Modifier
                    .padding(paddingValues),
                style = MaterialTheme.typography.bodyLarge
            )
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize().padding(10.dp)
//            ) {
//                items(array.size) {
//                    Text(text = array[it].body, style = MaterialTheme.typography.bodyLarge)
//                }
//            }
            Text(text = message.toString(), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top =20.dp))
            Text(text = "Post Message", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top =20.dp))
            Text(text = postMessage?.message.toString(), style = MaterialTheme.typography.bodyLarge)
        }
    }
}