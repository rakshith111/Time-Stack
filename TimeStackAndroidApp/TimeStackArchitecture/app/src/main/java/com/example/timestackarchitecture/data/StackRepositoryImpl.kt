package com.example.timestackarchitecture.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

class StackRepositoryImpl(private val dao: StackDAO) : StackRepository {
    override suspend fun insertStack(stack: StackData) {
        dao.insertStack(stack)
    }

    override suspend fun deleteStack(stack: StackData) {
        dao.deleteStack(stack)
    }

    override suspend fun deleteAllStacks() {
        dao.deleteAll()
    }

    override fun getStacks(): Flow<List<StackData>>{
        return dao.getStacks()
    }

}