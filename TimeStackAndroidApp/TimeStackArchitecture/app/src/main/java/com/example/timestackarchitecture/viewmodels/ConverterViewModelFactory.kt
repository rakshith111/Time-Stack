package com.example.timestackarchitecture.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ViewModelFactory<T : ViewModel>(private val viewModel: T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModel.javaClass)) {
            @Suppress("UNCHECKED_CAST")
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
