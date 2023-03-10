package com.example.timestackarchitecture.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface StackDAO {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStack(stack: StackData)
    @Delete
    suspend fun deleteStack(stack: StackData)
    @Query("DELETE FROM stack_table")
    suspend fun deleteAll()
    @Query("SELECT * FROM stack_table")
    fun getStacks(): Flow<List<StackData>>
}

