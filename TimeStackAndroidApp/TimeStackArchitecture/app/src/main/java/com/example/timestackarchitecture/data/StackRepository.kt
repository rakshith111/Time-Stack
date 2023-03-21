package com.example.timestackarchitecture.data

import kotlinx.coroutines.flow.Flow

interface StackRepository {
    suspend fun insertStack(stack: StackData)
    suspend fun updateStack(stack: StackData)
    suspend fun deleteStack(stack: StackData)
    suspend fun deleteAllStacks()
    fun getStacks(): Flow<List<StackData>>
}

