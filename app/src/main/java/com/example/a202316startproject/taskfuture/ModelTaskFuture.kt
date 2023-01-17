package com.example.a202316startproject.taskfuture

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.a202316startproject.MainActivity
import com.example.a202316startproject.data.GetDate
import com.example.a202316startproject.data.TaskFutureDataClass
import com.example.a202316startproject.data.UserDatabase
import com.example.a202316startproject.taskfuture.FormatDataSetTaskFuture
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import android.os.Handler
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.example.a202316startproject.R
import com.example.a202316startproject.tasktoday.FormatDataSetTaskTodayModel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern

//suspendをつけろ

data class Format(
    val task : String,
    val date : LocalDate,
    val time : LocalTime,
    val backColor : Drawable,
    val now : String
)

class ModelTaskFuture {
    private val userDao = MainActivity.userDao

    //Modelに必要なデータの保存
    //VMに渡すデータ、関数の返り値みたいな
    var buckUpHolderDataSet = ArrayList<Format>()

    private val mutex = Mutex()

    //modelはすべてsuspendか
    //読み込んだら
    suspend fun setView(){
        //初期設定をする
        mutex.withLock {
            buckUpHolderDataSet = ArrayList<Format>()
            userDao.getAllFromFuture().forEach{
                val date = GetDate.stringToLocalDate(it.date)
                val time = GetDate.stringToLocalTime(it.time)
                buckUpHolderDataSet += Format(
                    it.task,
                    date,
                    time,
                    ResourcesCompat.getDrawable(MainActivity.context.resources, R.drawable.border_pink, null)!!,
                    "")
            }
            //今を追加
            buckUpHolderDataSet += Format(
                "TODAY",
                //TODO:ここなんとかしたほうが
                LocalDate.now(),
                LocalTime.now(),
                ResourcesCompat.getDrawable(MainActivity.context.resources, R.drawable.border_blue, null)!!,
                "now"
            )
            //並べ替える
            buckUpHolderDataSet.sortWith(compareBy<Format>{it.date}.thenBy{it.time})
        }
    }

    //Okボタンを押したら
    suspend fun clickDialogPositiveButton(data : TaskFutureDataClass){
        //taskも時間も日付も同じなら追加しない
        mutex.withLock{
            var flag = false
            buckUpHolderDataSet.forEach {
                if(it.task == data.task && GetDate.localDateToString(it.date) == data.date && GetDate.localTimeToString(it.time) == data.time){
                    flag = true
                }
            }
            if(!flag){
                userDao.insertToFuture(data)
                buckUpHolderDataSet += Format(
                    data.task,
                    GetDate.stringToLocalDate(data.date),
                    GetDate.stringToLocalTime(data.time),
                    ResourcesCompat.getDrawable(MainActivity.context.resources, R.drawable.border_pink, null)!!,
                    "")
                //並べ替える

                buckUpHolderDataSet.sortWith(compareBy<Format>{it.date}.thenBy{it.time})
            }
        }
    }

    //updateボタン押したとき
    suspend fun clickUpdateButton(before : TaskFutureDataClass,after : TaskFutureDataClass){
        mutex.withLock {
            userDao.updateAtFuture(before.task,after.task,after.date,after.time)
        }
        setView()
    }

    //deleteボタンを押したら
    suspend fun clickOptionButton(task : String,isNow : String,position : Int){
        mutex.withLock {
            if(isNow != "now"){
                buckUpHolderDataSet.removeAt(position)
                userDao.deleteByTaskFromFuture(task)
            }
        }
    }
}