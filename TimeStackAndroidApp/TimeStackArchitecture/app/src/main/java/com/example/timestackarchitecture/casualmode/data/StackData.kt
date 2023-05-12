package com.example.timestackarchitecture.casualmode.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stack_table")
data class StackData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "stack_id")
    val id: Int,
    @ColumnInfo(name = "stack_name")
    val stackName: String,
    @ColumnInfo(name = "stack_time")
    val stackTime: Long,
    @ColumnInfo(name = "stack_active")
    var activeStack: Boolean,
    @ColumnInfo(name = "stack_is_playing")
    var isPlaying: Boolean,
)


