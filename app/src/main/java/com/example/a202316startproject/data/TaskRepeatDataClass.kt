package com.example.a202316startproject.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskRepeatDataClass(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name = "task")
    val task : String, //doSomething

    @ColumnInfo(name = "time")
    val time : String, //18:00

    @ColumnInfo(name = "dayOfWeek")
    val dayOfWeek : String, //"monday/friday/"

    @ColumnInfo(name = "lastDay")
    val lastDay : String, //monday 今日の曜日がdayOfWeekに入っているのにここが今日の曜日でないなら、アップデートされていないってこと
    @ColumnInfo(name = "doneToday")
    val doneToday : Boolean //true 終わったかどうかの判断
)