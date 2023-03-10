package com.example.timestackarchitecture.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StackData::class], version = 1)
abstract class StackDatabase : RoomDatabase() {

    abstract val stackDAO: StackDAO

}
