package com.example.a202316startproject.tasktoday

import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.a202316startproject.MainActivity
import com.example.a202316startproject.R
import com.example.a202316startproject.data.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayList
import kotlin.concurrent.withLock

//suspendをつける

data class FormatDataSetTaskTodayModel(
    val task : String,
    val time : LocalTime,
    val backColor : Drawable,
    val futureOrRepeat: String
)

class ModelTaskToday {
    private val userDao = MainActivity.userDao

    //データの保持
    //これ本当にmodelにいる？
    var saveFutureDataSet = ArrayList<FormatDataSetTaskTodayModel>()

    var insertPosition : Int? = null

    //このmutexを毎回新しいの作ったらいけない
    private val mutex = Mutex()

    //イベント
    suspend fun setView(){
        mutex.withLock {
            saveFutureDataSet = ArrayList<FormatDataSetTaskTodayModel>()
            userDao.getAllFromRepeat().forEach {
                //今日の曜日が設定されているか確認
                if(it.dayOfWeek.split("/").contains(GetDate.dayOfWeek.toString())){
                    //今日の曜日が入っていなかったら、まだ初期化されていないってこと
                    if(it.lastDay != GetDate.dayOfWeek.toString()){
                        //曜日をアップデートする
                        userDao.updateLastDayAtRepeat(it.task,GetDate.dayOfWeek.toString())
                        userDao.updateDoneTodayAtRepeat(it.task,false)
                        //そして初めてなので入れる
                        saveFutureDataSet += FormatDataSetTaskTodayModel(
                            it.task,
                            GetDate.stringToLocalTime(it.time),
                            ResourcesCompat.getDrawable(MainActivity.context.resources, R.drawable.border_pink, null)!!,
                            "repeat")
                    }else{
                        //今日の曜日が入っていたら、完了したか確認
                        if(it.doneToday){
                            //終わっていたら入れない
                        }else{
                            //終わっていなかったら入れる
                            saveFutureDataSet += FormatDataSetTaskTodayModel(
                                it.task,
                                GetDate.stringToLocalTime(it.time),
                                ResourcesCompat.getDrawable(MainActivity.context.resources, R.drawable.border_pink, null)!!,
                                "repeat")
                        }
                    }
                }
            }

            //futureから今日のをとってくる
            userDao.getByDateFromFuture(GetDate.dateByString).forEach {
                saveFutureDataSet += FormatDataSetTaskTodayModel(
                    it.task,
                    GetDate.stringToLocalTime(it.time),
                    //futureからとってきたので、必ずfuture
                    ResourcesCompat.getDrawable(MainActivity.context.resources, R.drawable.border_pink, null)!!,
                    "future")
            }

            //今を追加
            saveFutureDataSet += FormatDataSetTaskTodayModel(
                "NOW",
                LocalTime.now(),
                ResourcesCompat.getDrawable(MainActivity.context.resources, R.drawable.border_blue, null)!!,
                "now"
            )
            //並べ替える
            saveFutureDataSet.sortWith(compareBy<FormatDataSetTaskTodayModel>{it.time})
        }
    }

    //Okボタンを押したときの処理
    suspend fun clickDialogPositiveButton(data : TaskFutureDataClass){
        mutex.withLock {
            //taskも時間も同じなら追加しない
            var flag = false
            saveFutureDataSet.forEach {
                if(it.task == data.task && GetDate.localTimeToString(it.time) == data.time){
                    flag = true
                }
            }
            if(!flag){
                userDao.insertToFuture(data)
                saveFutureDataSet += FormatDataSetTaskTodayModel(
                    data.task,
                    GetDate.stringToLocalTime(data.time),
                    ResourcesCompat.getDrawable(MainActivity.context.resources, R.drawable.border_pink, null)!!,
                    "future")
                //並べ替える
                saveFutureDataSet.sortWith(compareBy<FormatDataSetTaskTodayModel>{it.time})
                insertPosition = saveFutureDataSet.size-1
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

    //deleteボタンを押したときの処理
    suspend fun clickOptionButton(task : String,futureOrRepeat: String,position : Int) {
        mutex.withLock {
            if(futureOrRepeat == "future"){
                userDao.deleteByTaskFromFuture(task)
                saveFutureDataSet.removeAt(position)
            }else if(futureOrRepeat == "repeat"){
                userDao.updateDoneTodayAtRepeat(task,true)
                saveFutureDataSet.removeAt(position)
            }else{
                //nowなら消さない
            }
        }

    }
}