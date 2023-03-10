package com.example.timestackarchitecture.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

interface StackRepository {
    suspend fun insertStack(stack: StackData)
    suspend fun deleteStack(stack: StackData)
    suspend fun deleteAllStacks()
    fun getStacks(): Flow<List<StackData>>
}

