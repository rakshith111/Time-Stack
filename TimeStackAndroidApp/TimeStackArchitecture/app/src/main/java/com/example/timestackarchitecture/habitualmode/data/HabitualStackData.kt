package com.example.timestackarchitecture.habitualmode.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stack_table_habitual")
data class HabitualStackData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "stack_id")
    val id: Int,
    @ColumnInfo(name = "stack_name")
    var stackName: String,
    @ColumnInfo(name = "stack_time")
    var stackTime: Long,
    @ColumnInfo(name = "stack_active")
    var activeStack: Boolean,
    @ColumnInfo(name = "stack_is_playing")
    var isPlaying: Boolean,
)


