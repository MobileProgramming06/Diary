package com.example.teamproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.teamproject.databinding.ActivityCalBinding
import java.io.FileInputStream
import java.io.FileOutputStream

class CalActivity : AppCompatActivity() {
    lateinit var binding : ActivityCalBinding
    var userID: String = "userID"
    lateinit var fname: String
    lateinit var str: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            binding.calTextView.visibility = View.VISIBLE
            binding.saveBtn.visibility = View.VISIBLE
            binding.contentEdit.visibility = View.VISIBLE
            binding.content.visibility = View.INVISIBLE
            binding.modifyBtn.visibility = View.INVISIBLE
            binding.delBtn.visibility = View.INVISIBLE
            binding.calTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            binding.contentEdit.setText("")
            checkDay(year, month, dayOfMonth, userID)
        }

        binding.saveBtn.setOnClickListener {
            saveDiary(fname)
            binding.contentEdit.visibility = View.INVISIBLE
            binding.saveBtn.visibility = View.INVISIBLE
            binding.modifyBtn.visibility = View.VISIBLE
            binding.delBtn.visibility = View.VISIBLE
            str = binding.contentEdit.text.toString()
            binding.content.text = str
            binding.content.visibility = View.VISIBLE
        }
    }

    fun checkDay (cYear: Int, cMonth: Int, cDay: Int, userID: String) {
        //저장할 파일 이름설정
        fname = "" + userID + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"

        var fileInputStream: FileInputStream
        try {
            fileInputStream = openFileInput(fname)
            val fileData = ByteArray(fileInputStream.available())
            fileInputStream.read(fileData)
            fileInputStream.close()
            str = String(fileData)
            binding.contentEdit.visibility = View.INVISIBLE
            binding.content.visibility = View.VISIBLE
            binding.content.text = str
            binding.saveBtn.visibility = View.INVISIBLE
            binding.modifyBtn.visibility = View.VISIBLE
            binding.delBtn.visibility = View.VISIBLE

            binding.modifyBtn.setOnClickListener {
                binding.contentEdit.visibility = View.VISIBLE
                binding.content.visibility = View.INVISIBLE
                binding.contentEdit.setText(str)
                binding.saveBtn.visibility = View.VISIBLE
                binding.modifyBtn.visibility = View.INVISIBLE
                binding.delBtn.visibility = View.INVISIBLE
                binding.content.text = binding.contentEdit.text
            }

            binding.delBtn.setOnClickListener {
                binding.content.visibility = View.INVISIBLE
                binding.modifyBtn.visibility = View.INVISIBLE
                binding.delBtn.visibility = View.INVISIBLE
                binding.contentEdit.setText("")
                binding.contentEdit.visibility = View.VISIBLE
                binding.saveBtn.visibility = View.VISIBLE
                removeDiary(fname)
            }
            if (binding.content.text == null) {
                binding.content.visibility = View.INVISIBLE
                binding.modifyBtn.visibility = View.INVISIBLE
                binding.delBtn.visibility = View.INVISIBLE
                binding.content.visibility = View.VISIBLE
                binding.saveBtn.visibility = View.VISIBLE
                binding.contentEdit.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    fun removeDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = ""
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    // 달력 내용 추가
    @SuppressLint("WrongConstant")
    fun saveDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = binding.contentEdit.text.toString()
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}