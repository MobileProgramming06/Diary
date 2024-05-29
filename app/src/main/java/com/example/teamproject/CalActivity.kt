package com.example.teamproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.DBHelper.EmotionDBHelper
import com.example.teamproject.databinding.ActivityCalBinding
import com.example.teamproject.databinding.ItemCalBinding
import java.io.Serializable

data class Cal(
    val id: Long,
    val content: String
) : Serializable

class CalActivity : AppCompatActivity() {
    lateinit var binding: ActivityCalBinding
    lateinit var calAdapter: CalAdapter
    private val calDataMap = mutableMapOf<String, MutableList<Cal>>()
    private lateinit var dbHelper: EmotionDBHelper
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Calender"

        calAdapter = CalAdapter(this)
        binding.calData.layoutManager = LinearLayoutManager(this)
        binding.calData.adapter = calAdapter

        dbHelper = EmotionDBHelper(this)

        binding.addBtn.setOnClickListener {
            val intent = Intent(this, EditCalActivity::class.java).apply {
                putExtra("type", "ADD")
            }
            startActivityForResult(intent, ADD_CAL_REQUEST_CODE)
            requestActivity.launch(intent)
        }

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$year-${month + 1}-$dayOfMonth"
            onDateSelected(year, month, dayOfMonth)
            updateCalList()
        }

        val calendar = java.util.Calendar.getInstance()
        selectedDate = "${calendar.get(java.util.Calendar.YEAR)}-${calendar.get(java.util.Calendar.MONTH) + 1}-${calendar.get(java.util.Calendar.DAY_OF_MONTH)}"
        updateCalList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_EMOTION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val emotion = it.getStringExtra(EmotionActivity.EXTRA_EMOTION)
                emotion?.let { saveEmotionForDate(selectedDate, it) }
            }
        }
    }

    private fun saveEmotionForDate(date: String, emotion: String) {
        dbHelper.insertEmotion(date, emotion)
        showToast("Emotion saved for $date")
    }

    private fun updateCalList() {
        val calList = calDataMap[selectedDate] ?: mutableListOf()
        calAdapter.updateCalList(calList)

        val emotion = dbHelper.getEmotion(selectedDate)
        emotion?.let {
            showToast("Emotion for $selectedDate: $it")
        }
    }

    private fun addCalToSelectedDate(cal: Cal) {
        val calList = calDataMap[selectedDate] ?: mutableListOf()
        calList.add(cal)
        calDataMap[selectedDate] = calList
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


    private fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = "$year-${month + 1}-$dayOfMonth"
        val intent = Intent(this, EmotionActivity::class.java).apply {
            putExtra(EmotionActivity.EXTRA_DATE, selectedDate)
        }
        startActivityForResult(intent, ADD_EMOTION_REQUEST_CODE)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ADD_CAL_REQUEST_CODE = 1001
        private const val ADD_EMOTION_REQUEST_CODE = 1002
    }
}

class CalAdapter(private val context: Context) : RecyclerView.Adapter<CalAdapter.MyViewHolder>() {
    private var list = mutableListOf<Cal>()

    inner class MyViewHolder(val binding: ItemCalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cal: Cal) {
            binding.diary.text = cal.content
            binding.root.setOnClickListener {
                val intent = Intent(context, DetailCalActivity::class.java)
                intent.putExtra("cal", cal)
                context.startActivity(intent)
            }
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
