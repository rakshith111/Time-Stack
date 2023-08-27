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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HabitualStackViewModel @Inject constructor(private val habitualStackRepository: HabitualStackRepository) :
    ViewModel() {
    private var _stackList = MutableStateFlow<List<HabitualStackData>>(listOf())
    val stackList = _stackList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            habitualStackRepository.getStacks().collect { list ->
                _stackList.value = list
            }
        }
    }

    val selectedItems = mutableStateListOf<Int>()
    suspend fun getStacks(): List<HabitualStackData> {
        val list = habitualStackRepository.getStacks().first()
        _stackList.value = list
        return list
    }

    fun insertStack(stack: HabitualStackData) {
        viewModelScope.launch(Dispatchers.IO) {
            habitualStackRepository.insertStack(stack)
        }
    }

    suspend fun updateStack(stack: HabitualStackData) {
        habitualStackRepository.updateStack(stack)
    }

    suspend fun removeStack(stack: HabitualStackData) {
        Timber.d("removeStack started")
        habitualStackRepository.deleteStack(stack)
        Timber.d("removeStack after deleteStack call")
        Timber.d("removeStack finished")
    }
}