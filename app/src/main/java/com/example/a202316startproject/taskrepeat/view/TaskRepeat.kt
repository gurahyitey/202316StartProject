package com.example.a202316startproject.taskrepeat.view

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
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a202316startproject.MainActivity
import com.example.a202316startproject.R
import com.example.a202316startproject.databinding.*
import com.example.a202316startproject.taskfuture.VMTaskFuture
import com.example.a202316startproject.taskfuture.view.TaskFutureRecyclerAdapter
import com.example.a202316startproject.taskrepeat.FormatDataSetTask
import com.example.a202316startproject.taskrepeat.VMTaskRepeat
import com.google.android.material.snackbar.Snackbar
import java.util.*

class TaskRepeat : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModel : VMTaskRepeat by viewModels()

        val binding = FragmentTaskRepeatBinding.inflate(inflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.recreate.observe(viewLifecycleOwner){
            if(it != null){
                binding.linearSunday.removeAllViews()
                binding.linearMonday.removeAllViews()
                binding.linearTuesday.removeAllViews()
                binding.linearWednesday.removeAllViews()
                binding.linearThursday.removeAllViews()
                binding.linearFriday.removeAllViews()
                binding.linearSaturday.removeAllViews()
            }
        }

        viewModel.insertHolderDataSet.observe(viewLifecycleOwner){
            if(it != null){
                val g = mutableMapOf(
                    "SUNDAY" to binding.linearSunday,
                    "MONDAY" to binding.linearMonday,
                    "TUESDAY" to binding.linearTuesday,
                    "WEDNESDAY" to binding.linearWednesday,
                    "THURSDAY" to binding.linearThursday,
                    "FRIDAY" to binding.linearFriday,
                    "SATURDAY" to binding.linearSaturday,
                )
                it.forEach {
                    val bindingLinear = LinearHolderBinding.inflate(inflater,g[it.first]!!,false)
                    bindingLinear.viewModel = viewModel
                    bindingLinear.lifecycleOwner = viewLifecycleOwner
                    bindingLinear.data = it.second
                    g[it.first]!!.addView(bindingLinear.root)
                }
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
                val bindingDialog = DialogAddTaskRepeatBinding.inflate(inflater,container,false)
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
                    dialogAdd.setNegativeButton("deleteAll"){_,_->
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
                    { view, hour, min->
                        viewModel.selectedTime(hour,min)
                    },0,0,true)
                dialogSelectDate.show()
            }
        }

        viewModel.setView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val viewModel : VMTaskRepeat by viewModels()
        viewModel.setView()
    }
}