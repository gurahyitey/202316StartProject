package com.example.a202316startproject.taskrepeat

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a202316startproject.data.GetDate
import com.example.a202316startproject.data.TaskRepeatDataClass
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter.ofPattern

data class FormatDataSetTask(
    val task : MutableLiveData<String>,
    val time : MutableLiveData<String>
)

class VMTaskRepeat : ViewModel(){
    private val model = ModelTaskRepeat()
    private val handler = Handler(Looper.getMainLooper())

    //データのバックアップ
    var buckupHolderDataSet = ArrayList<Pair<String,FormatDataSetTask>>()

    //データの一時保存
    val temSaveEditTextValue = MutableLiveData<String>()
    val temSaveSelectedTime = MutableLiveData<String>()
    val temSaveDayOfWeek = mutableMapOf(
        "SUNDAY" to false,
        "MONDAY" to false,
        "TUESDAY" to false,
        "WEDNESDAY" to false,
        "THURSDAY" to false,
        "FRIDAY" to false,
        "SATURDAY" to false,
    )
    lateinit var temSaveSelectData : FormatDataSetTask

    //命令
    val showDialog = MutableLiveData<String?>()
    val showSnackBar = MutableLiveData<String?>()
    val showSelectTimeDialog = MutableLiveData<String?>()
    val deleteHolderDataSet = MutableLiveData<Int?>()
    val insertHolderDataSet = MutableLiveData<ArrayList<Pair<String,FormatDataSetTask>>?>()

    val recreate = MutableLiveData<String?>()

    fun setView(){
        GlobalScope.launch {
            model.setView()
            handler.post{
                buckupHolderDataSet = ArrayList<Pair<String,FormatDataSetTask>>()
                val tem = ArrayList<Pair<String,FormatDataSetTask>>()
                //liveDataで覆う
                model.buckupHolderDataSet.forEach {
                    tem += it.first to FormatDataSetTask(MutableLiveData(it.second.task),MutableLiveData(GetDate.localTimeToString(it.second.time)))
                }
                buckupHolderDataSet = tem
                insertHolderDataSet.value = buckupHolderDataSet
                insertHolderDataSet.value = null
            }
        }
    }

    //追加ボタンを押したら
    fun clickAddButton(){
        temSaveEditTextValue.value = ""
        temSaveSelectedTime.value = GetDate.timeByString

        showDialog.value = "add"
        showDialog.value = null
    }

    //時間選択ボタンを押したら
    fun clickSelectTimeButton(){
        showSelectTimeDialog.value = "show!!"
        showSelectTimeDialog.value = null
    }

    //時間選択ダイアログでOK押したら
    fun selectedTime(hour : Int,min : Int){
        temSaveSelectedTime.value = GetDate.localTimeToString(LocalTime.of(hour,min))
    }

    //曜日チェックしたら
    fun checkedSwitch(dayOfWeek : String,checked : Boolean){
        temSaveDayOfWeek[dayOfWeek] = checked
    }

    //OKボタンを押したら
    fun clickDialogPositiveButton(){
        GlobalScope.launch {
            //monday/sundayの形にする
            var dayOfweek = ""
            temSaveDayOfWeek.forEach {
                if(it.value){
                    dayOfweek += it.key+"/"
                }
            }

            val tem = TaskRepeatDataClass(
                0,
                temSaveEditTextValue.value!!,
                temSaveSelectedTime.value!!,
                dayOfweek,
                "",
                false
            )
            model.insert(tem)
            handler.post{
                insertHolderDataSet.value = TaskRepeatDataClassToFormatDataSetTaskRepeat(tem)
                insertHolderDataSet.value = null
            }
        }
    }
    fun TaskRepeatDataClassToFormatDataSetTaskRepeat(it : TaskRepeatDataClass) : ArrayList<Pair<String,FormatDataSetTask>>{
        val tem = ArrayList<Pair<String,FormatDataSetTask>>()
        it.dayOfWeek.split("/").forEach { dayOfWeek ->
            if(dayOfWeek != ""){
                tem += Pair(dayOfWeek,FormatDataSetTask(MutableLiveData(it.task),MutableLiveData(it.time)))
            }
        }
        return tem
    }

    //optionボタンを押したら
    fun clickOptionButton(data : FormatDataSetTask){
        temSaveEditTextValue.value = ""
        temSaveSelectedTime.value = GetDate.timeByString
        temSaveSelectData = data

        showDialog.value = "edit"
        showDialog.value = null
    }

    //updateボタンを押したら
    fun clickUpdateButton(){
    }

    //deleteボタンを押したら
    fun clickDeleteButton(){
        GlobalScope.launch {
            model.delete(temSaveSelectData)
            model.setView()
            handler.post{
                //もう一回作り直す？
                recreate.value = ""
                recreate.value = null

                val tem = ArrayList<Pair<String,FormatDataSetTask>>()
                model.buckupHolderDataSet.forEach {
                    tem += it.first to FormatDataSetTask(MutableLiveData(it.second.task),MutableLiveData(GetDate.localTimeToString(it.second.time)))
                }
                buckupHolderDataSet = tem
                insertHolderDataSet.value = buckupHolderDataSet
                insertHolderDataSet.value = null
            }
        }
    }
}