package com.example.a202316startproject.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Insert
    suspend fun insertToFuture(tem : TaskFutureDataClass)
    @Insert
    suspend fun insertToRepeat(tem : TaskRepeatDataClass)

    @Query("SELECT *  FROM TaskFutureDataClass")
    suspend fun getAllFromFuture() : List<TaskFutureDataClass>
    @Query("SELECT *  FROM TaskRepeatDataClass")
    suspend fun getAllFromRepeat() : List<TaskRepeatDataClass>

    @Query("SELECT * FROM TaskFutureDataClass WHERE date = :date")
    suspend fun getByDateFromFuture(date : String) : List<TaskFutureDataClass>

    @Query("DELETE FROM TaskFutureDataClass WHERE task = :task")
    suspend fun deleteByTaskFromFuture(task : String)
    @Query("DELETE FROM TaskRepeatDataClass WHERE task = :task")
    suspend fun deleteByTaskFromRepeat(task : String)

    @Query("UPDATE TaskFutureDataClass SET task =:after,date =:date,time =:time WHERE task =:before")
    suspend fun updateAtFuture(before : String,after : String,date : String,time : String)

    @Query("UPDATE TaskRepeatDataClass SET lastDay =:lastDay WHERE task =:task")
    suspend fun updateLastDayAtRepeat(task : String,lastDay : String)
    @Query("UPDATE TaskRepeatDataClass SET doneToday =:doneToday WHERE task =:task")
    suspend fun updateDoneTodayAtRepeat(task : String,doneToday : Boolean)
}