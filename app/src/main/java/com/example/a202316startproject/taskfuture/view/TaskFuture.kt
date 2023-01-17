package com.example.a202316startproject.taskfuture.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a202316startproject.MainActivity
import com.example.a202316startproject.R
import com.example.a202316startproject.data.GetDate
import com.example.a202316startproject.databinding.DialogAddTaskFutureBinding
import com.example.a202316startproject.databinding.FragmentTaskFutureBinding
import com.example.a202316startproject.taskfuture.VMTaskFuture
import com.example.a202316startproject.tasktoday.VMTaskToday
import com.example.a202316startproject.tasktoday.view.TaskTodayRecyclerAdapter
import com.google.android.material.snackbar.Snackbar
import java.util.*

class TaskFuture : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModel : VMTaskFuture by viewModels()

        val binding = FragmentTaskFutureBinding.inflate(inflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(MainActivity.context)
        binding.recyclerView.adapter = TaskFutureRecyclerAdapter(viewModel,viewLifecycleOwner)

        viewModel.insertHolderDataSet.observe(viewLifecycleOwner){
            if(it != null){
                binding.recyclerView.adapter?.notifyItemInserted(it)
            }
        }

        viewModel.deleteHolderDataSet.observe(viewLifecycleOwner){
            if(it != null){
                binding.recyclerView.adapter?.notifyItemRemoved(it)
            }
        }

        viewModel.updateHolderDataSet.observe(viewLifecycleOwner){
            if(it != null){
                binding.recyclerView.adapter?.notifyDataSetChanged()
            }
        }

        viewModel.showSnackBar.observe(viewLifecycleOwner){
            if(it != null){
                Snackbar.make(binding.root,it, Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.showDialog.observe(viewLifecycleOwner){
            if(it != null){
                val dialogAdd = AlertDialog.Builder(MainActivity.context)
                val bindingDialog = DialogAddTaskFutureBinding.inflate(inflater,container,false)
                bindingDialog.viewModel = viewModel
                bindingDialog.lifecycleOwner = viewLifecycleOwner
                dialogAdd.setView(bindingDialog.root)
                if(it == "add"){
                    dialogAdd.setPositiveButton("OK"){_,_ ->
                        viewModel.clickDialogPositiveButton()
                    }
                    dialogAdd.setNeutralButton("cancel"){_,_->
                    }
                }else if(it == "edit"){
                    dialogAdd.setPositiveButton("Update"){_,_ ->
                        viewModel.clickUpdateButton()
                    }
                    dialogAdd.setNegativeButton("delete"){_,_->
                        viewModel.clickDeleteButton()
                    }
                    dialogAdd.setNeutralButton("cancel"){_,_->
                    }
                }
                dialogAdd.show()
            }
        }

        viewModel.showSelectDateDialog.observe(viewLifecycleOwner){
            if(it != null){
                val dialogSelectDate = DatePickerDialog(
                    MainActivity.context,
                    { view, year, month, dayOfMonth ->
                        //0-11で帰ってくる
                        viewModel.selectedDate(year,month+1,dayOfMonth)
                    },
                    GetDate.year,
                    GetDate.month-1,
                    GetDate.date)
                dialogSelectDate.show()
            }
        }

        viewModel.showSelectTimeDialog.observe(viewLifecycleOwner){
            if(it != null){
                val dialogSelectDate = TimePickerDialog(
                    MainActivity.context,
                    { view, hour, min->
                        viewModel.selectedTime(hour,min)
                    },
                    0,
                    0,
                    true)
                dialogSelectDate.show()
            }
        }

        viewModel.setView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val viewModel : VMTaskFuture by viewModels()
        viewModel.setView()
    }
}