package com.example.timestackarchitecture.habitualmode.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface HabitualStackDAO {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStack(stack: HabitualStackData)
    @Update
    suspend fun updateStack(stack: HabitualStackData)
    @Delete
    suspend fun deleteStack(stack: HabitualStackData)
    @Query("DELETE FROM stack_table")
    suspend fun deleteAll()
    @Query("SELECT * FROM stack_table")
    fun getStacks(): Flow<List<HabitualStackData>>
}

