package com.example.a202316startproject.data

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import com.bumptech.glide.load.model.ModelLoader.LoadData
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.min

object GetDate {
    //今日のデータをそれぞれLocalDateTimeで返す
    val year : Int = LocalDate.now().year //2022
    val month : Int = LocalDate.now().monthValue //12
    val date : Int = LocalDate.now().dayOfMonth //24
    val dayOfWeek : DayOfWeek = LocalDate.now().dayOfWeek //MONDAY

    val hour : Int = LocalTime.now().hour //17
    val minute : Int = LocalTime.now().minute //34
    val second : Int = LocalTime.now().second //22

    //今日のデータをstringで返す
    val dateByString = localToString("yyyy/MM/dd") //2022/12/24
    val dateWithoutYearByString = localToString("MM/dd") //12/24
    val timeByString = localToString("HH:mm") //17:34

    //今の時間を指定されたフォーマットの文字列で返す
    fun localToString(format : String) : String{
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format))
    }

    //stringをlocalDateへ
    fun stringToLocalDate(date : String) : LocalDate{
        return LocalDate.parse(date,DateTimeFormatter.ofPattern("yyyy/MM/dd"))
    }
    //stringをlocalTimeへ
    fun stringToLocalTime(time : String) : LocalTime{
        return LocalTime.parse(time,DateTimeFormatter.ofPattern("HH:mm"))
    }

    //localTimeをstringへ
    fun localDateToString(time : LocalDate) : String{
        return time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
    }
    //localTimeをstringへ
    fun localTimeToString(time : LocalTime) : String{
        return time.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}