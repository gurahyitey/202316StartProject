package com.example.a202316startproject.tasktoday

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a202316startproject.data.GetDate
import com.example.a202316startproject.data.TaskFutureDataClass
import com.example.a202316startproject.taskfuture.FormatDataSetTaskFuture
import com.example.a202316startproject.tasktoday.ModelTaskToday
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.util.Calendar

//ifを使わない

data class FormatDataSetTaskToday(
    val task : MutableLiveData<String>,
    val date : String,
    val time : MutableLiveData<String>,
    val backColor : Drawable,
    val futureOrRepeat: String
)

class VMTaskToday : ViewModel(){
    private val model = ModelTaskToday()
    private val handler = Handler(Looper.getMainLooper())

    //上位層からデータをとってくる
    //modelのデータを加工して、保持しておく
    var backUpHolderDataSet = ArrayList<FormatDataSetTaskToday>()

    //viewだけに必要なデータ
    var temSaveEditTextValue = MutableLiveData<String>()
    var temSaveSelectedTime = MutableLiveData<String>()

    lateinit var temSaveSelectData : FormatDataSetTaskToday

    //modelの命令を加工して、保持しておく
    val showDialog = MutableLiveData<String?>()
    val showSnackBar = MutableLiveData<String?>()
    val showSelectTimeDialog = MutableLiveData<String?>()
    val insertHolderDataSet = MutableLiveData<Int?>()
    val deleteHolderDataSet = MutableLiveData<Int?>()
    val updateHolderDataSet = MutableLiveData<String?>()


    //上位層へデータを渡す
    //viewからのデータを加工して、modelへデータを送信
    fun setView(){
        GlobalScope.launch {
            //modelに初期値を設定させる
            model.setView()
            handler.post{
                backUpHolderDataSet = ArrayList<FormatDataSetTaskToday>()
                //modelからとってきて、view用に保存する
                model.saveFutureDataSet.forEach {
                    backUpHolderDataSet += FormatDataSetTaskToday(
                        MutableLiveData(it.task),
                        GetDate.localDateToString(LocalDate.now()),
                        MutableLiveData(GetDate.localTimeToString(it.time)),
                        it.backColor,
                        it.futureOrRepeat)
                }
                updateHolderDataSet.value = "update"
                updateHolderDataSet.value = null
            }
        }
    }

    //追加ボタンを押したときの処理
    fun clickAddButton(){
        temSaveEditTextValue.value = ""
        temSaveSelectedTime.value = GetDate.timeByString
        showDialog.value = "add"
        showDialog.value = null
    }

    //時間選択ボタンを押したときの処理
    fun clickSelectTimeButton(){
        showSelectTimeDialog.value = "show"
        showSelectTimeDialog.value = null
    }

    //時間を選択してokしたときの処理
    fun selectedTime(hour : Int,min : Int){
        //選択時間を一時保存しておく
        temSaveSelectedTime.value = GetDate.localTimeToString(LocalTime.of(hour,min))
    }

    //Okボタンを押したときの処理
    fun clickDialogPositiveButton(){
        GlobalScope.launch {
            //model用にデータを加工する
            val tem = TaskFutureDataClass(0,
                temSaveEditTextValue.value!!,
                GetDate.dateByString,
                temSaveSelectedTime.value!!)
            //modelに渡す
            model.clickDialogPositiveButton(tem)
            handler.post{
                //view用のデータを変える
                val tem = ArrayList<FormatDataSetTaskToday>()
                model.saveFutureDataSet.forEach {
                    tem += FormatDataSetTaskToday(
                        MutableLiveData(it.task),
                        GetDate.localDateToString(LocalDate.now()),
                        MutableLiveData(GetDate.localTimeToString(it.time)),
                        it.backColor,
                        it.futureOrRepeat)
                }
                backUpHolderDataSet = tem

                //TODO:insertにしてほしい
                updateHolderDataSet.value = ""
                updateHolderDataSet.value = null
            }
        }
    }

    //optionボタンを押したときの処理
    //ていうかfuture以外はボタンを見せないようにすれば
    fun clickOptionButton(data : FormatDataSetTaskToday){
        temSaveSelectData = data
        temSaveEditTextValue.value = data.task.value
        temSaveSelectedTime.value = data.time.value
        //TODO:keseee
        if(data.futureOrRepeat == "future"){
            showDialog.value = "edit"
            showDialog.value = null
        }
    }

    fun clickUpdateButton(){
        GlobalScope.launch {
            val before = TaskFutureDataClass(
                0,
                temSaveSelectData.task.value!!,
                temSaveSelectData.date,
                temSaveSelectData.time.value!!)
            val after = TaskFutureDataClass(
                0,
                temSaveEditTextValue.value!!,
                temSaveSelectData.date,
                temSaveSelectedTime.value!!,
            )
            model.clickUpdateButton(before,after)
            handler.post{
                //消されたやつをmodelからとってきて、view用に入れる
                val tem = ArrayList<FormatDataSetTaskToday>()
                model.saveFutureDataSet.forEach {
                    tem += FormatDataSetTaskToday(
                        MutableLiveData(it.task),
                        GetDate.localDateToString(LocalDate.now()),
                        MutableLiveData(GetDate.localTimeToString(it.time)),
                        it.backColor,
                        it.futureOrRepeat)
                }
                backUpHolderDataSet = tem
                updateHolderDataSet.value = ""
                updateHolderDataSet.value = null
            }
        }
    }

    fun clickDeleteButton(){
        GlobalScope.launch {
            val futureOrRepeat = temSaveSelectData.futureOrRepeat
            val position = backUpHolderDataSet.indexOf(temSaveSelectData)
            model.clickOptionButton(
                temSaveSelectData.task.value!!,
                futureOrRepeat,
                position)
            handler.post{
                //消されたやつをmodelからとってきて、view用に入れる
                val tem = ArrayList<FormatDataSetTaskToday>()
                model.saveFutureDataSet.forEach {
                    tem += FormatDataSetTaskToday(
                        MutableLiveData(it.task),
                        GetDate.localDateToString(LocalDate.now()),
                        MutableLiveData(GetDate.localTimeToString(it.time)),
                        it.backColor,
                        it.futureOrRepeat)
                }
                backUpHolderDataSet = tem

                //TODO:if消せ
                if(futureOrRepeat != "now"){
                    //TODO:insertにしてほしい
                    updateHolderDataSet.value = ""
                    updateHolderDataSet.value = null
                }
            }
        }
    }
}