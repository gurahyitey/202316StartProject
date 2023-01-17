package com.example.a202316startproject.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskFutureDataClass(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name = "task")
    val task : String, //doSomething

    @ColumnInfo(name = "date")
    val date : String, //"2022/12/24"
    @ColumnInfo(name = "time")
    val time : String, //"21:23"
)