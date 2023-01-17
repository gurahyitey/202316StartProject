package com.example.a202316startproject

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.a202316startproject.data.Dao
import com.example.a202316startproject.data.UserDatabase
import com.example.a202316startproject.mainfragment.view.MainFragment

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var context : Context
        lateinit var userDao : Dao
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        userDao = Room.databaseBuilder(context, UserDatabase::class.java,"user_database").build().userDao()

        window.statusBarColor = Color.LTGRAY

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        //fragmentTransaction.addToBackStack("null")
        fragmentTransaction.replace(R.id.mainActivity, MainFragment())
        fragmentTransaction.commit()
    }
}