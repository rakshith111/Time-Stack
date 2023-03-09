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
import com.example.timestackarchitecture.viewmodels.StackViewModelFactory
import com.example.timestackarchitecture.viewmodels.TimerViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var stackViewModelFactory: StackViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {

// Use the factory to create an instance of TimerViewModel
        val timerViewModelFactory = TimerViewModelFactory()

// Use the factory to create an instance of TimerViewModel
        val timerViewModelInstance = ViewModelProvider(this, timerViewModelFactory).get(TimerViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContent {
            TimeStackArchitectureTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaseScreen(stackViewModelFactory = stackViewModelFactory, timerViewModel = timerViewModelInstance)

                }
            }
        }
    }
}

