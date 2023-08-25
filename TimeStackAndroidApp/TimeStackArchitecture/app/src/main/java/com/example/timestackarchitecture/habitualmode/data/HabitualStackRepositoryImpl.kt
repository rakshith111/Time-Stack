package com.example.timestackarchitecture.habitualmode.data

import kotlinx.coroutines.flow.Flow

class HabitualStackRepositoryImpl(private val dao: HabitualStackDAO) : HabitualStackRepository {
    override suspend fun insertStack(stack: HabitualStackData) {
        dao.insertStack(stack)
    }

    override suspend fun updateStack(stack: HabitualStackData) {
        dao.updateStack(stack)
    }

    override suspend fun deleteStack(stack: HabitualStackData) {
        dao.deleteStack(stack)
    }

    override suspend fun deleteAllStacks() {
        dao.deleteAll()
    }

    override fun getStacks(): Flow<List<HabitualStackData>>{
        return dao.getStacks()
    }

}

