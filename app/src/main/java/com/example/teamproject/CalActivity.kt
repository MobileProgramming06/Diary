package com.example.teamproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.DBHelper.TodoDBHelper
import com.example.teamproject.databinding.ActivityCalBinding
import com.example.teamproject.databinding.ItemCalBinding
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.Serializable


data class Cal (
    val id: Long,
    val content: String
):Serializable

class CalActivity : AppCompatActivity() {
    lateinit var binding: ActivityCalBinding
    lateinit var calAdapter: CalAdapter
    private val calDataMap = mutableMapOf<String, MutableList<Cal>>()
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calAdapter = CalAdapter()
        binding.calData.layoutManager = LinearLayoutManager(this)
        binding.calData.adapter = calAdapter

        binding.addBtn.setOnClickListener {
            val intent = Intent(this, EditCalActivity::class.java).apply {
                putExtra("type", "ADD")
            }
            requestActivity.launch(intent)
        }


        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedDate = "$year-${month + 1}-$dayOfMonth"
            updateCalList()
        }

        val calendar = java.util.Calendar.getInstance()
        selectedDate =
            "${calendar.get(java.util.Calendar.YEAR)}-${calendar.get(java.util.Calendar.MONTH) + 1}-${
                calendar.get(java.util.Calendar.DAY_OF_MONTH)
            }"
        updateCalList()
    }

    private val requestActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getSerializableExtra("schedule")?.let {
                    val schedule = it as Cal
                    addCalToSelectedDate(schedule)
                }


            }
        }



    private fun updateCalList() {
        val calList = calDataMap[selectedDate] ?: mutableListOf()
        calAdapter.updateCalList(calList)
    }

    private fun addCalToSelectedDate(cal: Cal) {
        val calList = calDataMap[selectedDate] ?: mutableListOf()
        calList.add(cal)
        calDataMap[selectedDate] = calList
        updateCalList()
    }

    private fun onCalClick(cal: Cal) {
        val intent = Intent(this, DetailCalActivity::class.java).apply {
            putExtra("cal", cal)
        }
        requestActivity.launch(intent)
    }
}


class CalAdapter : RecyclerView.Adapter<CalAdapter.MyViewHolder>(){
    private var list = mutableListOf<Cal>()

    inner class MyViewHolder(val binding: ItemCalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cal: Cal) {
            binding.diary.text = cal.content

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size

    fun updateCalList(newList: List<Cal>) {
        list = newList.toMutableList()
        notifyDataSetChanged()
    }
}


