package com.example.timestackarchitecture.habitualmode.data

import kotlinx.coroutines.flow.Flow

interface HabitualStackRepository {
    suspend fun insertStack(stack: HabitualStackData)
    suspend fun updateStack(stack: HabitualStackData)
    suspend fun deleteStack(stack: HabitualStackData)
    suspend fun deleteAllStacks()
    fun getStacks(): Flow<List<HabitualStackData>>
}

