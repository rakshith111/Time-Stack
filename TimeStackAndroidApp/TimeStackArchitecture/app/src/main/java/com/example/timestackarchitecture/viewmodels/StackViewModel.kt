package com.example.timestackarchitecture.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timestackarchitecture.data.StackData
import com.example.timestackarchitecture.data.StackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class StackViewModel(private val stackRepository: StackRepository) : ViewModel() {
    val selectedItems =  mutableStateListOf<Int>()

    val stackList =  stackRepository.getStacks()
    fun insertStack(stack: StackData) {
        viewModelScope.launch(Dispatchers.IO) {
            stackRepository.insertStack(stack)
        }
    }

    fun removeStack(stack: StackData) {
        viewModelScope.launch(Dispatchers.IO) {
            stackRepository.deleteStack(stack)
        }
    }

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            stackRepository.deleteAllStacks()
        }
    }

}