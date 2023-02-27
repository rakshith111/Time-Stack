package com.example.timestackarchitecture.viewmodels

import androidx.lifecycle.ViewModel
import com.example.timestackarchitecture.data.StackData


class StackViewModel : ViewModel() {
    val stacks = mutableListOf<StackData>()

}