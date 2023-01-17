package com.example.a202316startproject.taskfuture

import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a202316startproject.R
import com.example.a202316startproject.data.GetDate
import com.example.a202316startproject.data.TaskFutureDataClass
import com.example.a202316startproject.tasktoday.FormatDataSetTaskToday
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.util.*
import kotlin.collections.ArrayList

//ifを使うな

data class FormatDataSetTaskFuture(
    val task : MutableLiveData<String>,
    val year : MutableLiveData<String>,
    val date : MutableLiveData<String>,
    val time : MutableLiveData<String>,
    val backColor : Drawable,
    val now : String
)

class VMTaskFuture : ViewModel(){
    private val model = ModelTaskFuture()
    private val handler = Handler(Looper.getMainLooper())

    //ViewとModelに必要なデータの保存
    var buckUpHolderDataSet = ArrayList<FormatDataSetTaskFuture>()

    //Viewに必要なデータの一時保存
    val temSaveEditTextValue = MutableLiveData<String>()
    val temSaveSelectedDate = MutableLiveData<String>() //"2022/02/05"
    val temSaveSelectedTime = MutableLiveData<String>() //"12:00"
    lateinit var temSaveSelectData : FormatDataSetTaskFuture

    //命令
    val showDialog = MutableLiveData<String?>()
    val showSnackBar = MutableLiveData<String?>()
    val showSelectDateDialog = MutableLiveData<String?>()
    val showSelectTimeDialog = MutableLiveData<String?>()
    val insertHolderDataSet = MutableLiveData<Int?>()
    val deleteHolderDataSet = MutableLiveData<Int?>()
    val updateHolderDataSet = MutableLiveData<String?>()

    //読み込んだら
    fun setView(){
        GlobalScope.launch {
            model.setView()
            handler.post{
                buckUpHolderDataSet = ArrayList<FormatDataSetTaskFuture>()
                model.buckUpHolderDataSet.forEach {
                    val year = it.date.format(ofPattern("yyyy"))
                    val date = it.date.format(ofPattern("MM/dd"))
                    val time = GetDate.localTimeToString(it.time)
                    buckUpHolderDataSet += FormatDataSetTaskFuture(
                        MutableLiveData(it.task),
                        MutableLiveData(year),
                        MutableLiveData(date),
                        MutableLiveData(time),
                        it.backColor,
                        it.now)
                }
                updateHolderDataSet.value = "update"
                updateHolderDataSet.value = null
            }
        }
    }

    //追加ボタンを押したら
    fun clickAddButton(){
        temSaveEditTextValue.value = ""
        temSaveSelectedDate.value = GetDate.dateByString
        temSaveSelectedTime.value = GetDate.timeByString

        showDialog.value = "add"
        showDialog.value = null
    }

    //日時選択ボタンを押したら
    fun clickSelectDateButton(){
        showSelectDateDialog.value = "show"
        showSelectDateDialog.value = null
    }

    //時間選択ボタンを押したら
    fun clickSelectTimeButton(){
        showSelectTimeDialog.value = "show"
        showSelectTimeDialog.value = null
    }

    //日時選択ダイアログでOKを押したら
    fun selectedDate(year : Int,month : Int,dayOfMonth : Int){
        temSaveSelectedDate.value = GetDate.localDateToString(LocalDate.of(year,month,dayOfMonth))
    }

    //時間選択ダイアログでOKを押したら
    fun selectedTime(hour : Int,min : Int){
        temSaveSelectedTime.value = GetDate.localTimeToString(LocalTime.of(hour,min))
    }

    //Okボタンを押したら
    fun clickDialogPositiveButton(){
        //TODO:入ってない処理
        GlobalScope.launch {
            val tem = TaskFutureDataClass(0,
                temSaveEditTextValue.value!!,
                temSaveSelectedDate.value!!,
                temSaveSelectedTime.value!!)
            model.clickDialogPositiveButton(tem)
            handler.post{
                val a = ArrayList<FormatDataSetTaskFuture>()
                model.buckUpHolderDataSet.forEach {
                    val year = it.date.format(ofPattern("yyyy"))
                    val date = it.date.format(ofPattern("MM/dd"))
                    val time = GetDate.localTimeToString(it.time)
                    a += FormatDataSetTaskFuture(
                        MutableLiveData(it.task),
                        MutableLiveData(year),
                        MutableLiveData(date),
                        MutableLiveData(time),
                        it.backColor,
                        it.now)
                }
                buckUpHolderDataSet = a
                //TODO:
                updateHolderDataSet.value = ""
                updateHolderDataSet.value = null
            }
        }
    }

    //optionボタンを押したら
    fun clickOptionButton(data : FormatDataSetTaskFuture){
        temSaveSelectData = data
        temSaveEditTextValue.value = data.task.value
        temSaveSelectedDate.value = data.year.value!!+"/"+data.date.value!!
        temSaveSelectedTime.value = data.time.value
        //TODO:kese!!
        if(data.now != "now"){
            showDialog.value = "edit"
            showDialog.value = null
        }
    }

    fun clickUpdateButton(){
        GlobalScope.launch {
            val before = TaskFutureDataClass(
                0,
                temSaveSelectData.task.value!!,
                temSaveSelectData.year.value!!+"/"+temSaveSelectData.date.value!!,
                temSaveSelectData.time.value!!)
            val after = TaskFutureDataClass(
                0,
                temSaveEditTextValue.value!!,
                temSaveSelectedDate.value!!,
                temSaveSelectedTime.value!!,
            )
            model.clickUpdateButton(before,after)
            handler.post{
                //消されたやつをmodelからとってきて、view用に入れる
                val tem = ArrayList<FormatDataSetTaskFuture>()
                model.buckUpHolderDataSet.forEach {
                    tem += FormatDataSetTaskFuture(
                        MutableLiveData(it.task),
                        MutableLiveData(it.date.format(ofPattern("yyyy"))),
                        MutableLiveData(it.date.format(ofPattern("MM/dd"))),
                        MutableLiveData(GetDate.localTimeToString(it.time)),
                        it.backColor,
                        it.now)
                }
                buckUpHolderDataSet = tem
                updateHolderDataSet.value = ""
                updateHolderDataSet.value = null
            }
        }
    }

    fun clickDeleteButton(){
        GlobalScope.launch {
            val task = temSaveSelectData.task.value!!
            val position = buckUpHolderDataSet.indexOf(temSaveSelectData)
            model.clickOptionButton(task,temSaveSelectData.now,position)
            handler.post{
                val a = ArrayList<FormatDataSetTaskFuture>()
                model.buckUpHolderDataSet.forEach {
                    val year = it.date.format(ofPattern("yyyy"))
                    val date = it.date.format(ofPattern("MM/dd"))
                    val time = GetDate.localTimeToString(it.time)
                    a += FormatDataSetTaskFuture(
                        MutableLiveData(it.task),
                        MutableLiveData(year),
                        MutableLiveData(date),
                        MutableLiveData(time),
                        it.backColor,
                        it.now)
                }
                buckUpHolderDataSet = a
                //TODO:
                updateHolderDataSet.value = ""
                updateHolderDataSet.value = null
            }
        }
    }
}