package com.example.a202316startproject.mainfragment.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.a202316startproject.MainActivity
import com.example.a202316startproject.databinding.FragmentMainBinding
import com.example.a202316startproject.mainfragment.VMMainFragment
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class MainFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModel : VMMainFragment by viewModels()

        val binding = FragmentMainBinding.inflate(inflater,container,false)
        binding.viewPager2.adapter = MainFragmentPagerAdapter(this)

        return binding.root
    }
}