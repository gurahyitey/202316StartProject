package com.example.a202316startproject.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(TaskFutureDataClass::class,TaskRepeatDataClass::class), version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): Dao
}