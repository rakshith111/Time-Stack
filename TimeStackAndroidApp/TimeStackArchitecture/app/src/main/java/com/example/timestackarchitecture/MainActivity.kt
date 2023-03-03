package com.example.timestackarchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.timestackarchitecture.compose.BaseScreen
import com.example.timestackarchitecture.ui.theme.TimeStackArchitectureTheme
import com.example.timestackarchitecture.viewmodels.StackViewModel
import com.example.timestackarchitecture.viewmodels.TimerViewModel
import com.example.timestackarchitecture.viewmodels.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
// Create an instance of StackViewModelFactory
        val stackViewModelFactory = ViewModelFactory(StackViewModel())

// Use the factory to create an instance of StackViewModel
        val stackViewModelInstance = ViewModelProvider(this, stackViewModelFactory).get(StackViewModel::class.java)

// Create an instance of TimerViewModelFactory
        val timerViewModelFactory = ViewModelFactory(TimerViewModel())

// Use the factory to create an instance of TimerViewModel
        val timerViewModelInstance = ViewModelProvider(this, timerViewModelFactory).get(TimerViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContent {
            TimeStackArchitectureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaseScreen( stackViewModelInstance, timerViewModelInstance)

                }
            }
        }
    }
}

