package com.example.timestackarch.viewmodels

import androidx.lifecycle.ViewModel
import com.example.timestackarch.data.StackData

class StackViewModel : ViewModel() {
    val stacks = mutableListOf<StackData>()

}