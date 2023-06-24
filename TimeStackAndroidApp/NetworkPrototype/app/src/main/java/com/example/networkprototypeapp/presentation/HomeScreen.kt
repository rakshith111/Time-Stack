package com.example.networkprototypeapp.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.networkprototypeapp.viewmodel.ApiViewModel
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    ApiViewModel: ApiViewModel,
    mSocket: Socket
) {
    var counter by remember { mutableStateOf(0)}
    Text(text = "Home Screen")
//
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
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)){
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

            Button(onClick = {
                mSocket.emit("counter")
                mSocket.on("counter") { args ->
                    if (args[0] != null) {
                        counter = args[0] as Int
                    }
                }
            }, modifier = Modifier.padding(top = 20.dp)) {
                Text(text = "Increment Counter")
            }

            Text(text = counter.toString(), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top =20.dp))
        }
    }
}