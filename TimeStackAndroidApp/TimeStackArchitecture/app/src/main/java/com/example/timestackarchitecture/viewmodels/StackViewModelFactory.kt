package com.example.timestackarchitecture.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timestackarchitecture.data.StackRepository
import javax.inject.Inject


class StackViewModelFactory @Inject constructor(private val repository: StackRepository
                                                ):
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>):
            T = StackViewModel(repository) as T
}

class TimerViewModelFactory :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>):
            T = TimerViewModel() as T
}
