package com.example.timestackarchitecture.casualmode.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timestackarchitecture.casualmode.data.StackData
import com.example.timestackarchitecture.casualmode.data.StackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StackViewModel constructor(private val stackRepository: StackRepository) : ViewModel() {
    var stackList: List<StackData> by mutableStateOf(listOf())
    init {
        viewModelScope.launch(Dispatchers.IO) {
            stackRepository.getStacks().collect { list ->
                withContext(Dispatchers.Main){
                    stackList = list
                }
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