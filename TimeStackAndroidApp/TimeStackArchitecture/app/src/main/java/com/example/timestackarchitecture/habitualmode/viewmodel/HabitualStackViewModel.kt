package com.example.timestackarchitecture.habitualmode.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timestackarchitecture.habitualmode.data.HabitualStackData
import com.example.timestackarchitecture.habitualmode.data.HabitualStackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HabitualStackViewModel @Inject constructor(private val habitualStackRepository: HabitualStackRepository): ViewModel() {
    var stackList: List<HabitualStackData> by mutableStateOf(listOf())
    init {
        viewModelScope.launch(Dispatchers.IO) {
            habitualStackRepository.getStacks().collect { list ->
                withContext(Dispatchers.Main){
                    stackList = list
                }
            }
        }
    }
    val selectedItems =  mutableStateListOf<Int>()

    fun insertStack(stack: HabitualStackData) {
        viewModelScope.launch(Dispatchers.IO) {
            habitualStackRepository.insertStack(stack)
        }
    }

    fun updateStack(stack: HabitualStackData) {
        viewModelScope.launch(Dispatchers.IO) {
            habitualStackRepository.updateStack(stack)
        }
    }

    fun removeStack(stack: HabitualStackData) {
        viewModelScope.launch(Dispatchers.IO) {
            habitualStackRepository.deleteStack(stack)
        }
    }
}