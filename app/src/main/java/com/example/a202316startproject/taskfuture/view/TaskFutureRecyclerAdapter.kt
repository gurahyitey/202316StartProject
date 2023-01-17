package com.example.a202316startproject.taskfuture.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.a202316startproject.databinding.RecyclerHolderTaskFutureBinding
import com.example.a202316startproject.taskfuture.VMTaskFuture
import com.example.a202316startproject.tasktoday.VMTaskToday
import com.example.a202316startproject.tasktoday.view.TaskTodayRecyclerHolder

class TaskFutureRecyclerAdapter(val viewModel : VMTaskFuture, val life : LifecycleOwner) : RecyclerView.Adapter<TaskFutureRecyclerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskFutureRecyclerHolder {
        val binding = RecyclerHolderTaskFutureBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = life
        return TaskFutureRecyclerHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskFutureRecyclerHolder, position: Int) {
        holder.binding.data = viewModel.buckUpHolderDataSet[position]
    }

    override fun getItemCount(): Int {
        return viewModel.buckUpHolderDataSet.size
    }
}