package com.example.a202316startproject.tasktoday.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.a202316startproject.MainActivity
import com.example.a202316startproject.R
import com.example.a202316startproject.data.GetDate
import com.example.a202316startproject.databinding.DialogAddTaskTodayBinding
import com.example.a202316startproject.databinding.FragmentMainBinding
import com.example.a202316startproject.databinding.FragmentTaskTodayBinding
import com.example.a202316startproject.taskfuture.VMTaskFuture
import com.example.a202316startproject.tasktoday.VMTaskToday
import com.google.android.material.snackbar.Snackbar
import java.time.LocalTime
import java.util.*

//userへのデータの表示
//userからのデータの送信

//上位層に渡すときは、関数で
//下位層に渡すときは、liveDataもしくは、データを参照する
//つまり下位層が動けばいい、だが上位層のデータを書き換えてはならない

class TaskToday : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModel : VMTaskToday by viewModels()

        val binding = FragmentTaskTodayBinding.inflate(inflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(MainActivity.context)
        binding.recyclerView.adapter = TaskTodayRecyclerAdapter(viewModel,viewLifecycleOwner)

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
                val bindingDialog = DialogAddTaskTodayBinding.inflate(inflater,container,false)
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

        viewModel.showSelectTimeDialog.observe(viewLifecycleOwner){
            if(it != null){
                val dialogSelectDate = TimePickerDialog(
                    MainActivity.context,
                    { view, hour, min ->
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

        val viewModel : VMTaskToday by viewModels()
        viewModel.setView()
    }
}