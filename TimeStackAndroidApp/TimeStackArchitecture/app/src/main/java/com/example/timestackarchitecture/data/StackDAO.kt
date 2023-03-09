package com.example.timestackarchitecture.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import java.util.*

@Dao
interface StackDAO {
    @Insert
    suspend fun insertStack(stack: StackData)
    @Delete
    suspend fun deleteStack(stack: StackData)
    @Query("DELETE FROM stack_table")
    suspend fun deleteAll()
    @Query("SELECT * FROM stack_table")
    fun getStacks(): Flow<List<StackData>>
}