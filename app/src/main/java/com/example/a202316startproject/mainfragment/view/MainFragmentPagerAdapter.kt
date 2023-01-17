package com.example.a202316startproject.mainfragment.view

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.a202316startproject.taskfuture.view.TaskFuture
import com.example.a202316startproject.taskrepeat.view.TaskRepeat
import com.example.a202316startproject.tasktoday.view.TaskToday

class MainFragmentPagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> {
                return TaskToday()
            }
            1 -> {
                return TaskFuture()
            }
            else -> {
                return TaskRepeat()
            }
        }
    }
}