package com.example.timestackarchitecture.habitualmode.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec


@Database  (entities = [HabitualStackData::class], version = 1)
abstract class HabitualStackDatabase : RoomDatabase() {

    abstract val habitualStackDAO: HabitualStackDAO
}
