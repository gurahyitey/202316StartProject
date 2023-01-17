package com.example.a202316startproject.data

import androidx.room.Entity

//時間割など、確定した、繰り返し、repeatじゃだめなの？

@Entity
data class Schedule(
    val task : String
)
