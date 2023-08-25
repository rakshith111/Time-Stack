package com.example.timestackarchitecture.habitualmode.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HabitualStackData::class], version = 1)
abstract class HabitualStackDatabase : RoomDatabase() {

    abstract val habitualStackDAO: HabitualStackDAO

}
