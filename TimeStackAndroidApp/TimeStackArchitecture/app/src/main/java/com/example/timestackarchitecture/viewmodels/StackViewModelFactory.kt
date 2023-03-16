package com.example.timestackarchitecture.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timestackarchitecture.data.StackRepository
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class StackViewModelFactory @Inject constructor(private val repository: StackRepository
                                                ):
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>):
            T = StackViewModel(repository) as T
}

@Suppress("UNCHECKED_CAST")
class TimerViewModelFactory @Inject constructor(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>):
            T = TimerViewModel(context) as T
}
