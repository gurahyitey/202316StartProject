package com.example.a202316startproject.taskrepeat

import android.os.Build
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import com.example.a202316startproject.MainActivity
import com.example.a202316startproject.data.GetDate
import com.example.a202316startproject.data.TaskRepeatDataClass
import com.example.a202316startproject.taskrepeat.view.TaskRepeat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern

data class FormatDataSetTaskRepeatModel(
    val task : String,
    val time : LocalTime
)

class ModelTaskRepeat {
    private val userDao = MainActivity.userDao


    //ていうか本当にこれ必要なの？？・ｗ
    var buckupHolderDataSet = ArrayList<Pair<String,FormatDataSetTaskRepeatModel>>()

    suspend fun setView(){
        buckupHolderDataSet = ArrayList<Pair<String,FormatDataSetTaskRepeatModel>>()
        userDao.getAllFromRepeat().forEach {
            it.dayOfWeek.split("/").forEach { dayOfWeek ->
                if(dayOfWeek != ""){
                    val time = GetDate.stringToLocalTime(it.time)
                    buckupHolderDataSet += dayOfWeek to FormatDataSetTaskRepeatModel(it.task,time)
                }
            }
        }
    }

    suspend fun insert(data : TaskRepeatDataClass){
        userDao.insertToRepeat(data)
        data.dayOfWeek.split("/").forEach { dayOfWeek ->
            if(dayOfWeek != ""){
                val time = GetDate.stringToLocalTime(data.time)
                buckupHolderDataSet += dayOfWeek to FormatDataSetTaskRepeatModel(data.task,time)
            }
        }
    }

    suspend fun delete(data : FormatDataSetTask){
        userDao.deleteByTaskFromRepeat(data.task.value!!)
    }
}