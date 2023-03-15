package com.example.timestackarchitecture.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timestackarchitecture.data.StackData
import com.example.timestackarchitecture.data.StackRepository
import com.example.timestackarchitecture.data.StackRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class StackViewModel(private val stackRepository: StackRepository) : ViewModel() {
    var stackList: List<StackData> by mutableStateOf(listOf())
    init {

        viewModelScope.launch(Dispatchers.IO) {
            stackRepository.getStacks().collect { list ->
                stackList = list
            }
        }
    }
    val selectedItems =  mutableStateListOf<Int>()

    fun insertStack(stack: StackData) {
        viewModelScope.launch(Dispatchers.IO) {
            stackRepository.insertStack(stack)
        }
    }

    fun updateStack(stack: StackData) {
        viewModelScope.launch(Dispatchers.IO) {
            stackRepository.updateStack(stack)
        }
    }

    fun removeStack(stack: StackData) {
        viewModelScope.launch(Dispatchers.IO) {
            stackRepository.deleteStack(stack)
        }
    }

}