package com.example.a202316startproject.tasktoday.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.a202316startproject.databinding.RecyclerHolderTaskTodayBinding
import com.example.a202316startproject.tasktoday.VMTaskToday

class TaskTodayRecyclerAdapter(val viewModel : VMTaskToday, val life : LifecycleOwner) : RecyclerView.Adapter<TaskTodayRecyclerHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskTodayRecyclerHolder {
        val binding = RecyclerHolderTaskTodayBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = life
        return TaskTodayRecyclerHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskTodayRecyclerHolder, position: Int) {
        holder.binding.data = viewModel.backUpHolderDataSet[position]
    }

    override fun getItemCount(): Int {
        return viewModel.backUpHolderDataSet.size
    }
}