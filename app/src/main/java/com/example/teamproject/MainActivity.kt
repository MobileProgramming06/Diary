package com.example.teamproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.teamproject.CalActivity
import com.example.teamproject.LoginActivity
import com.example.teamproject.TodoActivity
import com.example.teamproject.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 기존의 클릭 리스너들을 유지하고, 감정 텍스트뷰를 초기화합니다.
        binding.emotionTextView.text = "오늘의 감정: ${getEmotionForToday()}"

        // 기존 클릭 리스너들을 유지합니다.
        binding.toLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.toTodo.setOnClickListener {
            val intent = Intent(this, TodoActivity::class.java)
            startActivity(intent)
        }

        binding.toCal.setOnClickListener {
            val intent = Intent(this, CalActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getEmotionForToday(): String {
        // 현재 날짜
        val currentDate = Calendar.getInstance().time

        // 실제 데이터베이스나 API를 통해 해당 날짜에 대한 감정을 가져와야 함
        // 간단히 임시 데이터를 사용
        val emotionsMap = hashMapOf(
            getDate(-1) to "행복",
            getDate(0) to "슬픔",
            getDate(1) to "기쁨"
        )

        // 오늘 날짜에 해당하는 감정을 가져옴
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDate = dateFormat.format(currentDate)

        return emotionsMap[todayDate] ?: "감정 없음"
    }

    // 현재 날짜를 기준으로 offset을 적용하여 날짜를 계산하는 함수
    private fun getDate(offset: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, offset)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }
}